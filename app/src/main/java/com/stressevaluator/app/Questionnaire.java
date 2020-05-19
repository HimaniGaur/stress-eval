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

public class Questionnaire extends AppCompatActivity {
    private TextView textViewQuestion;
    private Button buttonNextQuestion;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private Integer questionCounter = 0;
    JSONParser jsonParser = new JSONParser();
    JSONArray AllQuestions;

    String URL= "http://192.168.1.103/survey/getAlLQuestions.php";

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

        GetAllQuestions getAllQuestions = new GetAllQuestions();
        getAllQuestions.execute();
    }

    private void loadNextQuestion(final Integer questionCounter) throws JSONException {
        if (questionCounter < AllQuestions.length()) {
            textViewQuestion.setText(AllQuestions.optJSONObject(questionCounter).getString("Question"));
            buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!rb1.isSelected() && !rb2.isSelected() && !rb3.isSelected()
                    && !rb4.isSelected() && !rb5.isSelected()) {
                        Toast.makeText(getApplicationContext(), "Please select an option", Toast.LENGTH_SHORT).
                                show();
                    } else {
                        // store the responses in an array or a file
                        try {
                            loadNextQuestion(questionCounter+1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }

    private class GetAllQuestions extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textViewQuestion.setText("Loading...");
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            return jsonParser.makeHttpRequest(URL, "GET", params);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
//            super.onPostExecute();
            try {
                AllQuestions = result.getJSONArray("message");
                loadNextQuestion(questionCounter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
