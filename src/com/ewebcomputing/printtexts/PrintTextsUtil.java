/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ewebcomputing.printtexts;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.Phones;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TwoLineListItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
//import android.provider.ContactsContract;
//import android.provider.ContactsContract.PhoneLookup;

/**
 *
 * @author owner
 */
public class PrintTextsUtil {

    private static SimpleDateFormat fmt = new SimpleDateFormat("MMM dd hh:mm a");
    private static final String ADDRESS = "address";
    private static final String DATE = "date";
    private static final String BODY = "body";
    private static final String PROTOCOL = "protocol";
    private static final String ME = "Me";


    public static ArrayList<Conversation> getList(PrintTexts thi) {
        String SORT_ORDER = "date DESC";
        int count = 0;
        ArrayList<Conversation> mylist = new ArrayList<Conversation>();
        HashMap<String, String> map = new HashMap<String, String>();
        Cursor cursor = null;
        cursor = thi.getContentResolver().query(
                Uri.parse("content://sms/"),
                new String[]{"thread_id", "body"},
                null,
                null,
                SORT_ORDER);

        if (cursor != null) {
            try {
                count = cursor.getCount();
                while (cursor.moveToNext()) {
                    Conversation c = new Conversation();
                    c.setId(cursor.getString(0));
                    c.setSnippet(cursor.getString(1));
                    if(mylist.contains(c))  {
                        continue;
                    }
                    getThread(thi, c);                    
                    mylist.add(c);
                    //for(int i=0; i<100; i++) mylist.add(c);
                }
            } finally {
                cursor.close();
            }
        }
        return mylist;
    }

    private static void getThread(PrintTexts thi, Conversation c) {
        
        Cursor cursor = thi.getContentResolver().query(
                Uri.parse("content://sms/conversations/" + c.getId()),                
                null,
                null,
                null,
                "date DESC");
        if (cursor != null) {
            try {                
                while (cursor.moveToNext()) {
                    Message m = new Message();
                    String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
                    String body = cursor.getString(cursor.getColumnIndex(BODY));
                    Long date = cursor.getLong(cursor.getColumnIndex(DATE));
                    String protocol = cursor.getString(cursor.getColumnIndex(PROTOCOL));
                    m.setDate(fmt.format(new Date(date)));
                    Uri uri = Uri.withAppendedPath(Phones.CONTENT_FILTER_URL, Uri.encode(address));
                    Cursor cr = thi.getContentResolver().query(uri, new String[]{Phones.DISPLAY_NAME},null,null,null);
                    String person = null;                     
                    try    {
                    if(cr.moveToNext()) {
                        int nameFieldColumnIndex = cr.getColumnIndex(Phones.DISPLAY_NAME);
                        person = cr.getString(nameFieldColumnIndex);
                    }
                     }
                     catch(Exception e) {
                         e.printStackTrace();
                     }
                     finally   {
                         cr.close();
                     }
                    //m.setSnippet(cursor.getString(2));
                    m.setBody(body);                 
                    
                    if(person == null || person.trim().length() == 0)   {
                        person = address;
                    }
                        //System.out.println("person "+ person);
                        /*System.out.println("---------------------------------------------------------");
                        System.out.println("personActual "+person);
                        System.out.println("type " + cursor.getString(cursor.getColumnIndex("type")));
                        int type = cursor.getInt(cursor.getColumnIndex("type"));
                        System.out.println("type = " + type == android.telephony.TelephonyManager)
                        System.out.println("type " + cursor.getString(cursor.getColumnIndex("type")));
                        System.out.println("status " + cursor.getString(cursor.getColumnIndex("status")));
                        System.out.println("person " + cursor.getString(cursor.getColumnIndex("person")));
                        System.out.println("reply  " + cursor.getString(cursor.getColumnIndex("reply_path_present")));
                        System.out.println("protocol  " + cursor.getString(cursor.getColumnIndex("protocol")));
                        System.out.println("---------------------------------------------------------");*/
                    //}
                    if(protocol == null)    {
                        m.setSender(ME);
                    }
                    else
                        m.setSender(person);
                    if(c.getSender() == null)   {
                        c.setSender(person);
                    }
                    c.getMessages().add(m);
                }
            } finally {
                cursor.close();
            }
        }
        return;
    }

    public static void printAll(PrintTexts thi) {
        ExpandableListView v = thi.getExpandableListView();
        MessageAdapter m = (MessageAdapter) v.getExpandableListAdapter();
        Intent i = new Intent(thi, MailTo.class);
        i.putExtra("messages", (ArrayList) m.getConversations());
        thi.startActivity(i);
        //PrintService.printConversations(m.getConversations());
    }

     public static int getCheckedCount(PrintTexts thi) {
         int cnt_ = 0;
        ExpandableListView v = thi.getExpandableListView();
        int cnt = v.getChildCount();

        for (int i = 0; i < cnt; i++) {

            View child = v.getChildAt(i);
            if (child instanceof TwoLineListItem) {
                PrintTextsListItem tl = (PrintTextsListItem) child;
                CheckedTextView ctv = (CheckedTextView) tl.getText1();
                if (ctv.isChecked()) {
                    cnt_++;
                }
            }
        }
        return cnt_;
    }

    public static void printSelected(PrintTexts thi) {       
        ExpandableListView v = thi.getExpandableListView();
        MessageAdapter m = (MessageAdapter) v.getExpandableListAdapter();
        List c = m.getConversations();
        ArrayList msgs = new ArrayList();
        for (Iterator cm = c.iterator(); cm.hasNext();) {
            Conversation cn = (Conversation) cm.next();
            msgs.addAll(cn.getMessages());
        }
        ArrayList newmsgs = new ArrayList();
        int cnt = v.getChildCount();
       
        for (int i = 0; i < cnt; i++) {
            
            View child = v.getChildAt(i);
            if (child instanceof TwoLineListItem) {
                PrintTextsListItem tl = (PrintTextsListItem) child;
                CheckedTextView ctv = (CheckedTextView) tl.getText1();                
                if (ctv.isChecked()) {
                    newmsgs.add(tl.getMsg());
                }
            }
        }
        //PrintService.printConversations(newmsgs);
        Intent i = new Intent(thi, MailTo.class);
        i.putExtra("messages", (ArrayList) newmsgs);
        thi.startActivity(i);
    }
}