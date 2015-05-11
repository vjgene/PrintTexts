/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewebcomputing.printtexts;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

/**
 *
 * @author owner
 */
public class BalanceActivity extends Activity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.balanceactivity);
        DBAdapter a = new DBAdapter(this);
        double [] balances = a.getCurrentBalances();
        a.close();
        ((EditText)findViewById(R.id.bal_cash)).setText(new Double(balances[0]).toString());
        ((EditText)findViewById(R.id.bal_check)).setText(new Double(balances[1]).toString());
        // ToDo add your GUI initialization code here        
    }

}
