package com.example.bonk.zadanie61;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private MyService _ms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askForPermission(Manifest.permission.SEND_SMS,0x9);
        startService(new Intent(getApplicationContext(), MyService.class));
        bindService(new Intent(getApplicationContext(), MyService.class), this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        System.out.println("Wykonano resume");
        Intent bindIntent = new Intent(this,MyService.class);
        bindService(bindIntent,this,BIND_AUTO_CREATE);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        System.out.println("Wykonano onPause");
        if(_ms != null)
            unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        System.out.println("polaczono serwis");
        _ms = ((MyService.LocalBinder)iBinder).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        _ms = null;
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }
}

