package com.example.bonk.zadanie61;

/**
 * Created by Bonk on 2018-02-01.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

//poprawić shareReferences, zapisać flagę i brać ją z repozytorium
public class SmsSender extends BroadcastReceiver {
    private String _number = "";

    public SmsSender() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(action)) {
            _number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            editor.putBoolean("isRinging", true);
        } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(action)) {
            if (pref.getBoolean("isRinging", true)) {
                editor.putBoolean("isRinging", false);
                String mess = "Nie moge odebrac. Zadzwon pozniej.";

                try {
                   SmsManager smsMgr = SmsManager.getDefault();
                    ArrayList<String> partSms = smsMgr.divideMessage(mess);
                    if (partSms.size() > 1) {
                        smsMgr.sendMultipartTextMessage(_number, null, partSms, null, null);
                    } else {
                        smsMgr.sendTextMessage(_number, null, mess, null, null);
                    }
                    System.out.println("Wysylam wiadomosc");
                    Toast.makeText(context, "Wysłano wiadomosc!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            editor.putBoolean("isRinging", false);
        }
    }
}