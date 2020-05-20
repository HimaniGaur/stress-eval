package com.stressevaluator.app;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stressevaluator.app.Constants.baseUrl;

public class Questionnaire extends AppCompatActivity {
    private TextView textViewQuestion;
    private Button buttonNextQuestion;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    JSONParser jsonParser = new JSONParser();
    JSONArray AllQuestions;

    String URL= baseUrl + "/survey/getAllQuestions.php";

    private ColorStateList textColorDefaultRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        textViewQuestion = findViewById(R.id.text_view_question);
        buttonNextQuestion = findViewById(R.id.button_next_question);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        rb5 = findViewById(R.id.radio_button5);

        new GetAllQuestions().execute();
    }

    private void loadNextQuestion(final Integer questionCounter) throws JSONException {
        if (questionCounter < AllQuestions.length()) {
            String jString = AllQuestions.getString(questionCounter);
            String question = (questionCounter + 1) + ". "+ new JSONObject(jString).getString("Question");

            textViewQuestion.setText(question);
            rbGroup.clearCheck();


            buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!rb1.isChecked() && !rb2.isChecked() && !rb3.isChecked()
                    && !rb4.isChecked() && !rb5.isChecked()) {
                        Toast.makeText(getApplicationContext(), "Please select an option", Toast.LENGTH_SHORT).
                                show();
                    } else {
                        // TODO: Store the response in local storage
                        try {
                            loadNextQuestion(questionCounter+1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            textViewQuestion.setText(R.string.thank_you_message);
            rbGroup.setVisibility(View.INVISIBLE);
            buttonNextQuestion.setText(R.string.button_finish_text);
        }

    }

    private class GetAllQuestions extends AsyncTask<Void, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            textViewQuestion.setText("Loading...");
        }

        @Override
        protected JSONObject doInBackground(Void... args) {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
//            super.onPostExecute();
            try {
                AllQuestions = result.getJSONArray("message");
                loadNextQuestion(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
