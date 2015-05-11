/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ewebcomputing.printtexts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author owner
 */
public class MailTo extends Activity {

    private String readTxnData() {

        EditText email = (EditText) findViewById(R.id.email);
        String emailTxt = email.getText().toString();
        if (emailTxt.trim().length() == 0) {
            new AlertDialog.Builder(MailTo.this).setMessage("Please enter email").show();
            return null;
        }
        return emailTxt;
    }
    private OnClickListener saveTxn = new OnClickListener() {

        public void onClick(View v) {
            String email = readTxnData();
            sendMail(email);
        }
    };

    private void sendMail(String mail) {
        final ProgressDialog pd = ProgressDialog.show(this,
                "Please wait", "Generating Pdf...", true, false);
        new Thread(new Runnable() {

            public void run() {
                File pdffile = generatePDF();
                //File pdffile = new File("temp.pdf");
                try {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                    emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{""});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Print Texts");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Sent using PrintTexts app");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdffile));
                    emailIntent.setType("application/pdf");

                    //Uri.fr
                    startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), MAIN_SCREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        }).start();
    }
    private static final int MAIN_SCREEN = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAIN_SCREEN) {
            start();
        }
    }

    private File generatePDF() {
        Object txn = this.getIntent().getSerializableExtra("messages");
        PrintService.printConversations((ArrayList) txn);

        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpPost post = new HttpPost("http://printtexts.com/printtexts/printtexts.php");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


        nameValuePairs.add(new BasicNameValuePair("xml", PrintService.toPlainText((List) txn)));
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();

            //this.getDir("printtexts", Context.MODE_WORLD_READABLE);

            File pdffile = null;
            FileOutputStream f = null;

            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                f = this.openFileOutput("printtexts.pdf", Context.MODE_WORLD_READABLE);
                pdffile = this.getFileStreamPath("printtexts.pdf");
            } else {
                File pdfDir = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/com.ewebcomputing.printtexts/printtexts");

                if (!pdfDir.exists()) {
                    pdfDir.mkdirs();
                }
                pdffile = new File(pdfDir, "printtexts.pdf");
                f = new FileOutputStream(pdffile);
            }

            InputStream in = entity.getContent();

            int chr = -1;
            while ((chr = in.read()) != -1) {
                f.write(chr);
            }
            f.flush();
            f.close();
            return pdffile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void start() {
        Intent i = new Intent(this, PrintTexts.class);
        //i.putExtra("messages", (ArrayList) m.getConversations());
        startActivity(i);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        //setContentView(R.layout.mailactivity);
        //Button b = (Button) findViewById(R.id.SaveTransaction);
        //b.setOnClickListener(saveTxn);

        sendMail("");

    }
}
