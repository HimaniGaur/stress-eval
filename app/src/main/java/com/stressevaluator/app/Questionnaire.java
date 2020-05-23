package com.stressevaluator.app;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
    private Button buttonNextQuestion, buttonPrevQuestion;
    private RadioGroup rbGroup;
    private RadioButton rb1, rb2, rb3, rb4, rb5;
    JSONArray AllQuestions;
    ArrayList<Integer> responses = new ArrayList<>();

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

        try {
            AllQuestions = new JSONArray(getIntent().getStringExtra("AllQuestions"));
            responses.ensureCapacity(AllQuestions.length());
            loadQuestion(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadQuestion(final Integer questionCounter) throws JSONException {
        String jString = AllQuestions.getString(questionCounter);
        String question = (questionCounter + 1) + ". " + new JSONObject(jString).getString("Question");

        textViewQuestion.setText(question);

        switch (responses.get(questionCounter)) {
            case 0:
                rb1.setSelected(true);
                break;
            case 1:
                rb2.setSelected(true);
                break;
            case 2:
                rb3.setSelected(true);
                break;
            case 3:
                rb4.setSelected(true);
                break;
            case 4:
                rb5.setSelected(true);
                break;
            default:
                rbGroup.clearCheck();
                break;
        }

        if (questionCounter == AllQuestions.length() - 1) {
            buttonNextQuestion.setText(R.string.button_finish_text);
        }

        buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rb1.isChecked() && !rb2.isChecked() && !rb3.isChecked()
                        && !rb4.isChecked() && !rb5.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please select an option", Toast.LENGTH_SHORT).
                            show();
                } else {
                    if (rb1.isChecked()) responses.set(questionCounter, 0);
                    else if (rb2.isChecked()) responses.set(questionCounter, 1);
                    else if (rb3.isChecked()) responses.set(questionCounter, 2);
                    else if (rb4.isChecked()) responses.set(questionCounter, 3);
                    else if (rb5.isChecked()) responses.set(questionCounter, 4);

                    if (questionCounter < AllQuestions.length() - 1) {
                        try {
                            loadQuestion(questionCounter + 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        new AlertDialog.Builder(getApplicationContext())
                                .setMessage("Are you sure you want to submit?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Todo: Store the information of this questionnaire done in a file
                                        // TODO: Push the responses in database
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
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
        }
    }
}
