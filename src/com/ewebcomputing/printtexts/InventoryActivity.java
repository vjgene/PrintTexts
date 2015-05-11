/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewebcomputing.printtexts;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author owner
 */
public class InventoryActivity extends ListActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.inventoryactivity);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        DBAdapter db = new DBAdapter(this);        
        Cursor c = db.getInventory();

        while(c.moveToNext())   {
            map = new HashMap<String, String>();
           
            map.put("Item", c.getString(0));
            map.put("Qty", c.getString(1));
            
            mylist.add(map);
        }
        c.close();
        db.close();
        SimpleAdapter mSchedule = new SimpleAdapter(this, mylist, R.layout.invrow,
                new String[]{"Item", "Qty"}, new int[]{R.id.ITEM, R.id.QTY});

        View inf = View.inflate(this, R.layout.invheader, null);
        getListView().addHeaderView(inf);
        this.getListView().setAdapter(mSchedule);
    }

}
