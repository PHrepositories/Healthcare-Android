package com.a14roxgmail.prasanna.healthcareapp.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.a14roxgmail.prasanna.healthcareapp.Database.database;
import com.a14roxgmail.prasanna.healthcareapp.constants;

public class sync_service extends Service {
    private String message;
    private database sqlite_db;

    public sync_service() {}

    @Override
    public void onCreate() {
        Log.i(constants.TAG,"Service is started");
        //open or create if it is not exist
        sqlite_db = new database(this,"Health_Care",null,1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle args = intent.getExtras();
        message = args.getString("args");
        Log.i(constants.TAG, "Messages Successfully passed :- " + message);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (this) {
                        try {
                            if (isNetworkAvailable(getApplicationContext(),constants.Internet_Array)) {
                                Log.i(constants.TAG,"Internet Connection available");
                                //check is there any thing to upload to the server
                                //if yes send the request to the server
                                //save the status to the database

                            }else{
                                Log.i(constants.TAG,"No Internet Connection");
                            }
                            wait(3000);
                            Log.i(constants.TAG, "Service is working");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        Thread t1 = new Thread(r);
        //t1.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(constants.TAG,"Service is Destroying");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isNetworkAvailable(Context context, int[] networkTypes) {
        try {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo netInfo = cm.getNetworkInfo(networkType);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }

            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
