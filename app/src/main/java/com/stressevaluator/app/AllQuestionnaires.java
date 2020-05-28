package com.stressevaluator.app;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.stressevaluator.app.Constants.baseUrl;
import static com.stressevaluator.app.Constants.getQuestionnaireCode;

public class AllQuestionnaires extends AppCompatActivity {

    private Button btnSection1, btnLogout;
    private TextView helloMessage;
    private ListView listView;

    private UserLocalStore userLocalStore;
    private ResponseLocalStore responseLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_questionnaires);

        btnLogout = findViewById(R.id.button_logout);
        helloMessage = findViewById(R.id.text_view_hello_message);
        listView = findViewById(R.id.list_view_start_questionnaire);
        userLocalStore = new UserLocalStore(this);
        responseLocalStore = new ResponseLocalStore(this, userLocalStore.getLoggedInUser());


        helloMessage.setText("Hello, " + userLocalStore.getLoggedInUser().username);

        final List<String> questionnaireNames = Constants.questionnaireNames;

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
                String questionnaireCode = getQuestionnaireCode(questionnaire);

                if (responseLocalStore.isQuestionnaireAttempted(questionnaireCode)) {
                    Toast.makeText(getApplicationContext(), R.string.already_attempted, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_SHORT).show();
                    new GetQuestionnaireInfo().execute(questionnaire);
                }
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

    private class GetQuestionnaireInfo extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        String URL = baseUrl + "/getQuestionnaireDesc.php";
        String questionnaireName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            questionnaireName = args[0];
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("questionnaireName", questionnaireName));
            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            try {
                if (result != null) {
                    if (!result.getString("success").equals("1")) {
                        Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), StartQuestionnaire.class);
                        intent.putExtra("questionnaireName", questionnaireName);
                        intent.putExtra("questionnaireDesc", result.getString("message"));
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: Server not responding", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
