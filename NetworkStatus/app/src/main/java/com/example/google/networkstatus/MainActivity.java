package com.example.google.networkstatus;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;


public class MainActivity extends Activity {
    private SignalStrength      signalStrength;
    private TelephonyManager    telephonyManager;
    TextView textViewNetworkstate;
    private final static String LTE_TAG             = "LTE_Tag";
    private final static String LTE_SIGNAL_STRENGTH = "getLteSignalStrength";
//fasdf
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         textViewNetworkstate= (TextView) findViewById(R.id.networkstate);
        Button btnCheck = (Button) findViewById(R.id.check);
    btnCheck.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(haveNetworkConnection()=='W')
            {
                //fdasasdfasf

                /*Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);

                snackbar.show();*/
                WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                int linkSpeed = wifiManager.getConnectionInfo().getRssi();
                if(linkSpeed<-70)
                {
                    textViewNetworkstate.setText("Weak Signal");
                    textViewNetworkstate.setHighlightColor(Color.parseColor("#FF0000"));
                }
                else if(linkSpeed>-50)
                {
                    textViewNetworkstate.setHighlightColor(Color.parseColor("#FF0000"));
                    textViewNetworkstate.setText("Exvellent");
                }
                else
                    textViewNetworkstate.setText("Good");
                Toast.makeText(getApplicationContext(),"wrifi"+linkSpeed,Toast.LENGTH_LONG).show();
            }
            else
            {

                telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                final PhoneStateListener mListener = new PhoneStateListener()
                {
                    @Override
                    public void onSignalStrengthsChanged(SignalStrength sStrength)
                    {
                        signalStrength = sStrength;
                        textViewNetworkstate.setText(sStrength.toString());

                        Toast.makeText(getApplicationContext(),"cellular"+sStrength,Toast.LENGTH_LONG).show();

                        getLTEsignalStrength();
                    }
                };

                // Register the listener for the telephony manager
                telephonyManager.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);


            }
        }
    });
    }
    private void getLTEsignalStrength()
    {
        try
        {
            Method[] methods = android.telephony.SignalStrength.class.getMethods();

            for (Method mthd : methods)
            {
                if (mthd.getName().equals(LTE_SIGNAL_STRENGTH))
                {
                    int LTEsignalStrength = (Integer) mthd.invoke(signalStrength, new Object[] {});
                    Log.i(LTE_TAG, "signalStrength = " + LTEsignalStrength);
                    return;
                }

            }

        }
        catch (Exception e)
        {
            Log.e(LTE_TAG, "Exception: " + e.toString());
        }
    }
    private char haveNetworkConnection() {
        char res = 'M';

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) {
                    res = 'W';
                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected())
                    res = 'M';
            }
        }
        return res;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
