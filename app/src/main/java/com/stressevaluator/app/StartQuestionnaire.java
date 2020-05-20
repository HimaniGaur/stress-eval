package com.stressevaluator.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartQuestionnaire extends AppCompatActivity {
    private Button btnSection1;
    private Button btnSection2;
    private Button btnSection3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_questionnaire);

        btnSection1 = findViewById(R.id.button_attempt_section_1);

        btnSection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
                startActivity(intent);
            }
        });
    }
}
