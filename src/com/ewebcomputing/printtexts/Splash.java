/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ewebcomputing.printtexts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {
   ProgressDialog pd = null;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);        
        pd = ProgressDialog.show(this,
                "Please wait", "Loading...", true);
        new Handler().post(new Runnable() {
            @Override
            public void run() {               
               Splash.this.startActivity(new Intent(Splash.this, PrintTexts.class));
               Splash.this.finish();               
            }
        });       
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pd.dismiss();
    }
}


