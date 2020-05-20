package com.stressevaluator.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        userLocalStore = new UserLocalStore(this);

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2*1000);

                    if (userLocalStore.getUserLoggedIn() == true) {
                        Intent intent = new Intent(getApplicationContext(), StartQuestionnaire.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        background.start();
    }
}
