/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewebcomputing.printtexts;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author owner
 */
public class DebtorActivity extends ListActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.debtoractivity);
        boolean isCredit = this.getIntent().getBooleanExtra("isCredit",true);
        if(isCredit)    {
            ((TextView)findViewById(R.id.debtorsheader)).setText("Creditors");
        }
        else
            ((TextView)findViewById(R.id.debtorsheader)).setText("Debtors");
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        DBAdapter db = new DBAdapter(this);
        HashMap<String,Double> vals = db.getCreditorsOrDebtors(isCredit);

        for(Iterator i = vals.keySet().iterator(); i.hasNext();)   {
            String cust = (String)i.next();
            map = new HashMap<String, String>();
            map.put("Customer", cust);
            map.put("Amount", vals.get(cust).toString());
            mylist.add(map);
        }
        
        db.close();
        SimpleAdapter mSchedule = new SimpleAdapter(this, mylist, R.layout.debtorrow,
                new String[]{"Customer", "Amount"}, new int[]{R.id.CUSTOMER,R.id.AMOUNT});

        View inf = View.inflate(this, R.layout.debtorheader, null);
        getListView().addHeaderView(inf);
        this.getListView().setAdapter(mSchedule);
    }

}
