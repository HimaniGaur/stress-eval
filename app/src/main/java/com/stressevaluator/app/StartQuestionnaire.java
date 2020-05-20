package com.stressevaluator.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartQuestionnaire extends AppCompatActivity {
    private Button btnSection1, btnLogout;
    private TextView helloMessage;

    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_questionnaire);

        userLocalStore = new UserLocalStore(this);
        btnSection1 = findViewById(R.id.button_attempt_section_1);
        btnLogout = findViewById(R.id.button_logout);
        helloMessage = findViewById(R.id.text_view_hello_message);

        helloMessage.setText("Hello, " + userLocalStore.getLoggedInUser().username);

        btnSection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLocalStore.cleanUserData();
                userLocalStore.setUserLoggedIn(false);
                Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
