package com.stressevaluator.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Questionnaire extends AppCompatActivity {
    private TextView textViewQuestion;
    private Button buttonNextQuestion, buttonPrevQuestion;
    private RadioGroup rbGroup;
    private RadioButton rb1, rb2, rb3, rb4, rb5;
    JSONArray AllQuestions;
    String questionnaireName;
    Integer responses[];
    UserLocalStore userLocalStore;

    private ColorStateList textColorDefaultRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        textViewQuestion = findViewById(R.id.text_view_question);
        buttonNextQuestion = findViewById(R.id.button_next_question);
        buttonPrevQuestion = findViewById(R.id.button_previous_question);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        rb5 = findViewById(R.id.radio_button5);
        userLocalStore = new UserLocalStore(this);

        try {
            AllQuestions = new JSONArray(getIntent().getStringExtra("AllQuestions"));
            questionnaireName = getIntent().getStringExtra("QuestionnaireName");
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
        String question = (questionCounter + 1) + ". " + new JSONObject(jString).getString("Question");

        textViewQuestion.setText(question);

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
                if (!rb1.isChecked() && !rb2.isChecked() && !rb3.isChecked()
                        && !rb4.isChecked() && !rb5.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please select an option", Toast.LENGTH_SHORT).
                            show();
                } else {
                    if (rb1.isChecked()) responses[questionCounter] = 1;
                    else if (rb2.isChecked()) responses[questionCounter] = 2;
                    else if (rb3.isChecked()) responses[questionCounter] = 3;
                    else if (rb4.isChecked()) responses[questionCounter] = 4;
                    else if (rb5.isChecked()) responses[questionCounter] = 5;

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
                                // Todo: Store the information of this questionnaire done in a file
                                userLocalStore.setQuestionnaireResponse(questionnaireName, responses);
                                // TODO: Push the responses in database
                                // Todo: change the activity back to StartQuestionnaire
                                Intent intent = new Intent(Questionnaire.this, StartQuestionnaire.class);
                                startActivity(intent);
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
}
