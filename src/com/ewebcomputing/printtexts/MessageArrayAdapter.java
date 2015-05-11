/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ewebcomputing.printtexts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TwoLineListItem;
import java.util.List;

/**
 *
 * @author owner
 */
public class MessageArrayAdapter  extends    ArrayAdapter    {

	private    final int resourceId;

    public MessageArrayAdapter(Context context, int textViewResourceId, List objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Conversation c = (Conversation)getItem(position);
        
        // if the array item is null, nothing to display, just return null
        if (c == null) {
            return null;
        }

        // We need the layoutinflater to pick up the view from xml
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Pick up the TwoLineListItem defined in the xml file
       TwoLineListItem view;
        if (convertView == null) {
            view = (TwoLineListItem) inflater.inflate(resourceId, parent, false);
        } else {
            view = (TwoLineListItem) convertView;
        }
        

        // Set value for the first text field
        if (view.getText1() != null) {
            view.getText1().setText(c.getSender() + "          " + c.getDate());
        }

        // set value for the second text field
        if (view.getText2() != null) {
            view.getText2().setText(c.getSnippet());
        }


        /*CheckedTextView chkBox = (CheckedTextView) view.getText1();
        chkBox.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v)
        {
            ((CheckedTextView) v).toggle();
        }
    });*/

        return view;
    }
}
