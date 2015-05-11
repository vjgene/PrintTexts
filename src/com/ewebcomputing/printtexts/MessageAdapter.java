/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewebcomputing.printtexts;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;
import java.util.List;

/**
 *
 * @author owner
 */
public class MessageAdapter extends BaseExpandableListAdapter {
    // Sample data set. children[i] contains the children (String[]) for
    // groups[i].
   

    private List conversations;
    private PrintTexts activity;
    
   public List getConversations()   {
       return conversations;
   }

    public MessageAdapter(List conversations, PrintTexts activity)    {
        this.conversations = conversations;
        this.activity = activity;
    }

    public Object getChild(int groupPosition, int childPosition) {       
        return ((Conversation)(conversations.get(groupPosition))).getMessages().get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        int i = 0;
        try {        
            i = ((Conversation)(conversations.get(groupPosition))).getMessages().size();
        } catch (Exception e) {
        }

        return i;
    }

    public TextView getGenericView() {        
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT, 64);

        TextView textView = new TextView(activity);
        textView.setLayoutParams(lp);        
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);        
        textView.setPadding(36, 0, 0, 0);
        return textView;
    }

  

    public View getChildView(int groupPosition, int childPosition,
        boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PrintTextsListItem view =
        (PrintTextsListItem) inflater.inflate(R.layout.msgrow, parent, false);
        //view.
        Message m = (Message)getChild(groupPosition, childPosition);
        view.getText1().setText(m.getSender() + "     " + m.getDate());        
        view.getText2().setText(m.getBody());
        view.setMsg(m);
        return view;        
    }

    public Object getGroup(int groupPosition) {
        return ((Conversation)(conversations.get(groupPosition)));
    }

    public int getGroupCount() {
        return conversations.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
        View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView =
        (TextView) inflater.inflate(R.layout.group, parent, false);        
        Conversation c = (Conversation)getGroup(groupPosition);
        String text = "     " + c.getSender() + "         " + c.getDate();
        textView.setText(text);
        return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

    }

