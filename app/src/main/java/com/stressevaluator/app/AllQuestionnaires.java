package com.stressevaluator.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AllQuestionnaires extends AppCompatActivity {
    private Button btnSection1;
    private Button btnSection2;
    private Button btnSection3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_questionnaires);

        btnSection1 = findViewById(R.id.button_attempt_section_1);
        btnSection2 = findViewById(R.id.button_attempt_section_2);
        btnSection3 = findViewById(R.id.button_attempt_section_3);

        btnSection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
//                intent.putExtra("Attempt", 1);
                Log.e("AllQuestionnaires", "Attempting to start the activity");
                startActivity(intent);
            }
        });

        btnSection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
                intent.putExtra("Attempt", 2);
                startActivity(intent);
            }
        });

        btnSection3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
                intent.putExtra("Attempt", 3);
                startActivity(intent);
            }
        });
    }
}
