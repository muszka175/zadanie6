package com.example.bonk.zadanie61;

/**
 * Created by Bonk on 2018-02-01.
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.TelephonyManager;

public class MyService extends Service {
    private IBinder _lb = new LocalBinder();
    private BroadcastReceiver _br;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return _lb;
    }

    public MyService getService()
    {
        return MyService.this;
    }

    public class LocalBinder extends Binder
    {
        public MyService getService()
        {
            return MyService.this;
        }
    }

    @Override
    public void onDestroy()
    {
        if(_br != null)
        {
            unregisterReceiver(_br);
        }
        _br = null;
        System.out.println("Zniszczono usluge");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isRinging", false);
        _br = new SmsSender();
        registerReceiver(_br, intentFilter);

        System.out.println("Uruchomiono usluge");

        return super.onStartCommand(intent, flags, startId);
    }
}