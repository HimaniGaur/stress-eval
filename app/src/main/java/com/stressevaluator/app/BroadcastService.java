package com.stressevaluator.app;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import static com.stressevaluator.app.Constants.questionnaireNames;

public class BroadcastService extends Service {

    private final static String TAG = "BroadcastService";

    /*public static final String COUNTDOWN_BR = "your_package_name.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);*/

    CountDownTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Created");
        cdt = new CountDownTimer(3*60*1000, 60*1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                /*Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);*/
                Log.i(TAG, "Tick");
            }

            @Override
            public void onFinish() {
                ResponseLocalStore responseLocalStore = new ResponseLocalStore(getApplicationContext(),
                        new UserLocalStore(getApplicationContext()).getLoggedInUser());

                if (responseLocalStore.getQuestionnairesCompletedCounter() < questionnaireNames.size()) {
                    responseLocalStore.setResponsesToNull();
                    Log.i(TAG, "Deleted user responses");
                }
            }
        };

        cdt.start();
    }

    @Override
    public void onDestroy() {

        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
