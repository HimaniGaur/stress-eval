package com.stressevaluator.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StartQuestionnaire extends AppCompatActivity {
    private Button btnSection1, btnLogout;
    private TextView helloMessage;

    private UserLocalStore userLocalStore;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_questionnaire);

        userLocalStore = new UserLocalStore(this);
        btnLogout = findViewById(R.id.button_logout);
        helloMessage = findViewById(R.id.text_view_hello_message);
        listView = findViewById(R.id.list_view_start_questionnaire);

        helloMessage.setText("Hello, " + userLocalStore.getLoggedInUser().username);

/*        btnSection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
                startActivity(intent);
            }
        });*/

        final List<String> questionnaireNames = new ArrayList<>();
        questionnaireNames.add("Questionnaire 1");
        questionnaireNames.add("Questionnaire 2");
        questionnaireNames.add("Questionnaire 3");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                questionnaireNames
        );

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String questionnaire = questionnaireNames.get(i);
                Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
                intent.putExtra("questionnaire", questionnaire);
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
