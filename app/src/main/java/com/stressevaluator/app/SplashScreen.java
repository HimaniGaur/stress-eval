package com.stressevaluator.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    UserLocalStore userLocalStore;
    ResponseLocalStore responseLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        userLocalStore = new UserLocalStore(this);
        responseLocalStore = new ResponseLocalStore(this, userLocalStore.getLoggedInUser());

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2*1000);

                    responseLocalStore.setResponseId(1);

                    if (userLocalStore.getUserLoggedIn() == true) {
                        Intent intent = new Intent(getApplicationContext(), AllQuestionnaires.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } catch (InterruptedException e) {
                }
            }
        };

        background.start();
    }
}
