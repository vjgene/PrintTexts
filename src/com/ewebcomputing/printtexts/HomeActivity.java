/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ewebcomputing.printtexts;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author owner
 */
public class HomeActivity extends ListActivity {

    public static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static SimpleDateFormat sf1 = new SimpleDateFormat("MM/dd hh:mm");
   

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.home);

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAllTxn();
        
        while(c.moveToNext())   {
            map = new HashMap<String, String>();
            try {                
                map.put("Date", sf1.format(sf.parse(c.getString(4))));
            }catch(Exception e){}

            map.put("Item", c.getString(0));
            map.put("Qty", c.getString(1));
            map.put("Amount", c.getString(2));
            map.put("Txn", Txn.TXNS[c.getInt(3)]);
            mylist.add(map);
        }
        c.close();
        db.close();
        //SimpleAdapter mSchedule = new SimpleAdapter(this, mylist, R.layout.txnrow,
                //new String[]{"Date", "Item", "Qty", "Amount", "Txn"}, new int[]{R.id.DATE, R.id.ITEM, R.id.QTY, R.id.AMOUNT, R.id.TXN_TXN});

        View inf = View.inflate(this, R.layout.txnheader, null);
        getListView().addHeaderView(inf);
        //this.getListView().setAdapter(mSchedule);

    }
}
