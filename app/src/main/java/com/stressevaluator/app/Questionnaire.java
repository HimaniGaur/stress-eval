package com.stressevaluator.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
//import android.support.v7.app.AlertDialog;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.stressevaluator.app.Constants.baseUrl;

public class Questionnaire extends AppCompatActivity {
    private TextView textViewQuestion, textViewQuestionNum, textViewDesc;
    private Button buttonNextQuestion, buttonPrevQuestion;
    private RadioGroup rbGroup;
    private RadioButton rb1, rb2, rb3, rb4, rb5, rb6;
    JSONArray AllQuestions;
    String questionnaireCode;
    Integer responses[];
    UserLocalStore userLocalStore;
    ResponseLocalStore responseLocalStore;

    private ColorStateList textColorDefaultRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewQuestionNum = findViewById(R.id.text_view_question_num);
        textViewDesc = findViewById(R.id.text_view_question_short_desc);
        buttonNextQuestion = findViewById(R.id.button_next_question);
        buttonPrevQuestion = findViewById(R.id.button_previous_question);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        rb5 = findViewById(R.id.radio_button5);
        rb6 = findViewById(R.id.radio_button6);
        userLocalStore = new UserLocalStore(this);
        responseLocalStore = new ResponseLocalStore(this, userLocalStore.getLoggedInUser());

        textViewDesc.setText(Constants.getShortDesc(getIntent().getStringExtra("QuestionnaireCode")));


        try {
            AllQuestions = new JSONArray(getIntent().getStringExtra("AllQuestions"));
            questionnaireCode = getIntent().getStringExtra("QuestionnaireCode");
            responses = new Integer[AllQuestions.length()];
            Log.d("Questionnaire", String.valueOf(responses.length));
            loadQuestion(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadQuestion(final Integer questionCounter) throws JSONException {
        Log.d("Questionnaire", "response: " + Arrays.toString(responses) + ", " + questionCounter);
        String jString = AllQuestions.getString(questionCounter);
        String question = new JSONObject(jString).getString("Question");
        String questionNum = "Question " + (questionCounter+1) + "/" + AllQuestions.length();

        textViewQuestion.setText(question);
        textViewQuestionNum.setText(questionNum);

        if (!getIntent().getStringExtra("QuestionnaireCode").equals("SUS"))
            rb6.setVisibility(View.INVISIBLE);

        if (responses[questionCounter] == null)
            rbGroup.clearCheck();
        else {
            Log.d("Questionnaire", responses[questionCounter].getClass().getName());
            switch (responses[questionCounter]) {
                case 1:
                    rb1.setChecked(true);
                    break;
                case 2:
                    rb2.setChecked(true);
                    break;
                case 3:
                    rb3.setChecked(true);
                    break;
                case 4:
                    rb4.setChecked(true);
                    break;
                case 5:
                    rb5.setChecked(true);
                    break;
                case 6:
                    rb6.setChecked(true);
                default:
                    break;
            }
        }

        if (questionCounter == AllQuestions.length() - 1) {
            buttonNextQuestion.setText(R.string.button_finish_text);
        } else {
            buttonNextQuestion.setText("Next");
        }

        buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select an option", Toast.LENGTH_SHORT).
                            show();
                } else {
                    if (rb1.isChecked()) responses[questionCounter] = 1;
                    else if (rb2.isChecked()) responses[questionCounter] = 2;
                    else if (rb3.isChecked()) responses[questionCounter] = 3;
                    else if (rb4.isChecked()) responses[questionCounter] = 4;
                    else if (rb5.isChecked()) responses[questionCounter] = 5;
                    else if (rb6.isChecked()) responses[questionCounter] = 6;

                    if (questionCounter < (AllQuestions.length() - 1)) {
                        try {
                            loadQuestion(questionCounter + 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Questionnaire.this);
                        alertDialog.setTitle(R.string.app_name);
                        alertDialog.setMessage("Are you sure you want to submit?");
                        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Done: Store the information of this questionnaire done in a file
                                responseLocalStore.setQuestionnaireResponse(questionnaireCode, responses);
                                responseLocalStore.setQuestionnairesCompletedCounter(responseLocalStore.getQuestionnairesCompletedCounter()+1);

                                // TODO: set timer to 3 days if it is the first questionnaire attempted
                                if (responseLocalStore.getQuestionnairesCompletedCounter() == 1) {
                                    ;
                                }

                                // TODO: Push the responses in database when all questionnaires are done
                                if ( true /*responseLocalStore.getQuestionnairesCompletedCounter() == Constants.questionnaireNames.size()*/) {
                                    new pushResponsesToDatabase().execute(
                                            userLocalStore.getLoggedInUser().getUsername(),
                                            responseLocalStore.getResponseId().toString(),
                                            getIntent().getStringExtra("QuestionnaireCode"));
                                }
                            }
                        });
                        alertDialog.setNegativeButton(android.R.string.no, null);
                        alertDialog.create().show();
                    }
                }
            }
        });

        if (questionCounter > 0) {
            buttonPrevQuestion.setVisibility(View.VISIBLE);
            buttonPrevQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        loadQuestion(questionCounter - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            buttonPrevQuestion.setVisibility(View.INVISIBLE);
        }
    }

    private class pushResponsesToDatabase extends AsyncTask<String, String, JSONObject> {
        String URL = baseUrl + "/storeUserResponses.php";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(String... args) {
            String username = args[0];
            String response_id = args[1];
            String code = args[2];
            String responses = responseLocalStore.getQuestionnaireResponse(code);
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONParser jsonParser = new JSONParser();
            JSONObject json = null;
            
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("response_id", response_id));
            params.add(new BasicNameValuePair("questionnaireCode", code));
            params.add(new BasicNameValuePair("responses", responses));

            json = jsonParser.makeHttpRequest(URL, "POST", params);

            try {
                if (json.get("success").equals(0))
                    return json;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            
            if (result != null) {
                try {
                    Toast.makeText(getApplicationContext(), result.get("message").toString(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Questionnaire.this, AllQuestionnaires.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error: Server not responding. Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
