package com.nigeldbrown.wifiautomater;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WifiAutomaterActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    WiFiScanReceiver wifiReciever;
    @BindView(R.id.wifitoggleButton)
    Switch toggle;
    @BindView(R.id.status)
    TextView status;
    WifiManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_automater);
        ButterKnife.bind(this);
        manager= (WifiManager)getSystemService(Context.WIFI_SERVICE);
        toggle.setOnCheckedChangeListener(this);
        wifiReciever = new WiFiScanReceiver();

        if(Build.VERSION.SDK_INT < 23){

                //Turn on Wifi and Connect

        }else{

            Dexter.checkPermissions(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {

                    //Turn on wifi and Connect

                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                }
            }, Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.WAKE_LOCK);

        }

    }
    @Override
    public void onCheckedChanged(CompoundButton v,boolean isChecked){

        if(isChecked){
            manager.setWifiEnabled(true);
            manager.startScan();
        }else{
            manager.setWifiEnabled(false);
            unregisterReceiver(wifiReciever);
        }

    }

    class WiFiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            //    wifiScanResult = wifiManager.getScanResults();


            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                List<ScanResult> wifiScanResultList = manager.getScanResults();
                for (int i = 0; i < wifiScanResultList.size(); i++) {
                    ScanResult accessPoint = wifiScanResultList.get(i);
                    String listItem = " Name: " + accessPoint.SSID +    " \n Mac: " + accessPoint.BSSID + "\n Signal Strenght: " + accessPoint.level+ "dBm";
                    Log.i("Wifi",listItem);


                }
            }


        }
    }

    @Override
    protected  void onResume(){
        super.onResume();
        IntentFilter wifi = new IntentFilter();
        wifi.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifi.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiReciever,wifi);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiReciever);
    }
}
