/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewebcomputing.printtexts;

import java.util.Iterator;
import java.util.Set;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager.*;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
/**
 *
 * @author owner
 */
public class SMSReceiver extends BroadcastReceiver  {
	int lat, lon;
	private void triggerAppLaunch(Context context)
	{
                System.out.println("app launch");
		Intent broadcast = new Intent("com.ewebcomputing.printtexts.WAKE_UP");
		broadcast.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(new Intent(broadcast));
	}
	/*private void getGPSData()
	{
		//Yes this is cheating, Go look at my other Android DevX article for the code that goes here
		lat = 2; lon = 3;
	}
	private void sendGPSData(Context context, Intent intent, SmsMessage inMessage)
	{
		String sendData = "Loc: lat: "+lat+"long: "+lon;
		SmsManager mng = SmsManager.getDefault();
		PendingIntent dummyEvent = PendingIntent.getBroadcast(context, 0, new Intent("com.ewebcomputing.printtexts.IGNORE_ME"), 0);

		String addr = inMessage.getOriginatingAddress();

		if(addr == null)
		{
			Log.i("SmsIntent", "Unable to get Address from Sent Message");
		}
		try{
			mng.sendTextMessage(addr, null, sendData, dummyEvent, dummyEvent);
		}catch(Exception e){
			Log.e("SmsIntent","SendException", e );
		}
	}*/
	private SmsMessage[] getMessagesFromIntent(Intent intent)
	{
		SmsMessage retMsgs[] = null;
		Bundle bdl = intent.getExtras();
		try{
			Object pdus[] = (Object [])bdl.get("pdus");
			retMsgs = new SmsMessage[pdus.length];
			for(int n=0; n < pdus.length; n++)
			{
				byte[] byteData = (byte[])pdus[n];
				retMsgs[n] = SmsMessage.createFromPdu(byteData);
			}

		}catch(Exception e)
		{
			Log.e("GetMessages", "fail", e);
		}
		return retMsgs;
	}
	public void onReceive(Context context, Intent intent)
	{
                System.out.println("on receive");
		if(!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
		{
			return;
		}
		SmsMessage msg[] = getMessagesFromIntent(intent);

		for(int i=0; i < msg.length; i++)
		{
			String message = msg[i].getDisplayMessageBody();
                        System.out.println(message);
			if(message != null && message.length() > 0)
			{
				Log.i("MessageListener:",  message);

				//Our trigger message must be generic and human redable because it will end up
				//In the SMS inbox of the phone.
				if(message.startsWith("test"))
				{
					triggerAppLaunch(context);
				}
				else if(message.startsWith("Hey, Phone, where did I forget you this time?"))
				{
					//getGPSData();
					//sendGPSData(context, intent, msg[i]);
				}
			}
		}
	}
}
