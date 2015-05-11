/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ewebcomputing.printtexts;

import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TwoLineListItem;
import java.util.ArrayList;

/**
 *
 * @author EWeb Computing (http://www.ewebcomputing.com)
 */
public class PrintTexts extends ExpandableListActivity {

    ProgressDialog pd = null;
    public static final int PRINT_ALL = 1;
    public static final int PRINT_SELECTED = 2;

    private class LoadList extends AsyncTask<Void, Integer, MessageAdapter> {
        ArrayList<Conversation> mylist = null;
        MessageAdapter ma = null;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected MessageAdapter doInBackground(Void... a) {

            try {
                mylist = PrintTextsUtil.getList(PrintTexts.this);
                //Thread.sleep(10000);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return ma;
        }

        @Override
        protected void onPostExecute(MessageAdapter res) {

            ma = new MessageAdapter(mylist, PrintTexts.this);
            getExpandableListView().setItemsCanFocus(true);
            if (ma != null) {
                setListAdapter(ma);
            }
            getExpandableListView().setFocusable(true);
            PrintTexts.this.setContentView(R.layout.main);
            PrintTexts.this.removeDialog(101);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 101: {
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("Please wait");
                dialog.setMessage("Loading...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                return dialog;
            }
            case 102: {
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("Please wait");
                dialog.setMessage("Printing messages...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                return dialog;
            }
        }
        return null;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        showDialog(101);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(icicle);
        LoadList l = new LoadList();
        l.execute();        
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (v instanceof PrintTextsListItem) {
            CheckedTextView tempView = (CheckedTextView) ((TwoLineListItem) v).getText1();
            tempView.setChecked(!tempView.isChecked());
            return true;
        }
        return super.onChildClick(parent, v, groupPosition, childPosition, id);
    }

    private void printAll() {
        PrintTextsUtil.printAll(this);
    }

    private void printSelected() {
        PrintTextsUtil.printSelected(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem i = null;
        i = menu.add(0, PRINT_ALL, 0, "Print All");
        //i.setIcon(AppUtils.resizeImage(this, R.drawable.shopping_cart, 32, 32));
        i = menu.add(0, PRINT_SELECTED, 0, "Print Selected");
        // i.setIcon(AppUtils.resizeImage(this, R.drawable.add, 32, 32));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {

            case PRINT_ALL:
                //printAll();
                showDialog(102);
                PrintAll p = new PrintAll();
                p.execute();
                return true;
            case PRINT_SELECTED:
                //printSelected();
                 showDialog(102);
                PrintSel p1 = new PrintSel();
                p1.execute();
                return true;
        }
        return false;
    }

    private class PrintAll extends AsyncTask<Void, Integer, Void> {
        ArrayList<Conversation> mylist = null;
        MessageAdapter ma = null;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... a) {

            try {
                PrintTexts.this.printAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            PrintTexts.this.removeDialog(102);
        }
    }

     private class PrintSel extends AsyncTask<Void, Integer, Void> {
        ArrayList<Conversation> mylist = null;
        MessageAdapter ma = null;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... a) {

            try {
                PrintTexts.this.printSelected();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            PrintTexts.this.removeDialog(102);
        }
    }
}
