package com.stressevaluator.app;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stressevaluator.app.Constants.baseUrl;
import static com.stressevaluator.app.Constants.getQuestionnaireCode;
import static com.stressevaluator.app.Constants.questionnaireNames;

public class StartQuestionnaire extends AppCompatActivity {
    private JSONArray AllQuestions;
    private TextView qName, qDesc;
    private Button buttonStartQues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_questionnaire);
        qName = findViewById(R.id.text_view_questionnaire_name);
        qDesc = findViewById(R.id.text_view_questionnaire_desc);
        buttonStartQues = findViewById(R.id.button_start_questionnaire);

        final String qNameText = getIntent().getStringExtra("questionnaireName");
        String qDescText = getIntent().getStringExtra("questionnaireDesc")
                .replace("\\n", "\n")
                .replace("\"", "")
                .replace("\\r", "");

        qName.setText(qNameText);
        qDesc.setText(qDescText);

        buttonStartQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Please wait", Toast.LENGTH_SHORT).show();
                new GetAllQuestions().execute(Constants.getQuestionnaireCode(qNameText));
            }
        });
    }

    private class GetAllQuestions extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        String URL = baseUrl + "/getAllQuestions.php";
        String questionnaireCode;

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
            questionnaireCode = args[0];
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("questionnaireName", questionnaireCode));
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
                        AllQuestions = result.getJSONArray("message");
                        Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
                        intent.putExtra("QuestionnaireCode", questionnaireCode);
                        intent.putExtra("AllQuestions", AllQuestions.toString());
                        startActivity(intent);
                        finish();
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