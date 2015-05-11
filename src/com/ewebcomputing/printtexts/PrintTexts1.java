/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ewebcomputing.printtexts;

import android.app.ExpandableListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ExpandableListView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author owner
 */
public class PrintTexts1 extends ExpandableListActivity {

    private static SimpleDateFormat fmt = new SimpleDateFormat("MMM dd hh:mm a");
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        
        

        getExpandableListView().setItemsCanFocus(true);
        String SORT_ORDER = "date DESC";
        int count = 0;
        ArrayList<Conversation> mylist = new ArrayList<Conversation>();
        HashMap<String, String> map = new HashMap<String, String>();
        Cursor cursor = this.getContentResolver().query(
                Uri.parse("content://sms/conversations/"),
                new String[] {"thread_id", "snippet"},
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
                    getThread(c);
                    HashMap<String, String> m = new HashMap<String, String>();
                    m.put("Address", c.getSnippet());
                    m.put("From", c.getSender());
                     m.put("Date", c.getDate().toString());
                    mylist.add(c);

                }
            } finally {
                cursor.close();
            }
        }


        //setListAdapter(new MessageArrayAdapter(this, R.layout.msgrow, mylist));
        getExpandableListView().setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);
        
    }

    private String [] getThread(Conversation c) {
        String [] tDetail = new String[]{"",""};
        Cursor cursor = this.getContentResolver().query(
                Uri.parse("content://sms/conversations/" + c.getId()),
                new String[] {"date","address","body"},
                null,
                null,
                "date DESC");
        if (cursor != null) {
            try {              
                while (cursor.moveToNext()) {
                    Message m = new Message();

                        
                        m.setDate(fmt.format(new Date(cursor.getLong(0))));
                        //m.setSnippet(cursor.getString(2));
                        m.setBody(cursor.getString(2));
                        m.setSender(cursor.getString(1));
                        c.getMessages().add(m);
                    
                }
            } finally {
                cursor.close();
                
            }
        }
        return tDetail;
    }
}
