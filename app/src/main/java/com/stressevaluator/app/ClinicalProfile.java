package com.stressevaluator.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stressevaluator.app.Constants.baseUrl;

public class ClinicalProfile extends AppCompatActivity {
    EditText age, numFamMembers;
    Spinner gender, branch, year, day_scholar, dasa, drop_year,
            annual_fam_income, mother_edu_level, father_edu_level,
            fam_similar_bg, involve_tech_societies, involve_cult_clubs,
            placed, cgpa, scstobc, pwd, single_child;
    Button btnSubmitCPS;

    UserLocalStore userLocalStore;
    ResponseLocalStore responseLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinical_profile);

        age = findViewById(R.id.edit_text_age);
        numFamMembers = findViewById(R.id.edit_num_members_family);

        gender = findViewById(R.id.spinner_gender);
        branch = findViewById(R.id.spinner_branch);
        year = findViewById(R.id.spinner_year);
        day_scholar = findViewById(R.id.day_scholar);
        dasa = findViewById(R.id.dasa);
        drop_year = findViewById(R.id.drop_year);
        annual_fam_income = findViewById(R.id.spinner_family_income);
        mother_edu_level = findViewById(R.id.spinner_edu_mother);
        father_edu_level = findViewById(R.id.spinner_edu_father);
        fam_similar_bg = findViewById(R.id.same_edu_bg);
        involve_tech_societies = findViewById(R.id.spinner_tech_societies);
        involve_cult_clubs = findViewById(R.id.spinner_cultural_clubs);
        placed = findViewById(R.id.spinner_placed);
        cgpa = findViewById(R.id.spinner_cgpa);
        scstobc = findViewById(R.id.spinner_scstobc);
        pwd = findViewById(R.id.spinner_pwd);
        single_child = findViewById(R.id.spinner_single_child);

        btnSubmitCPS = findViewById(R.id.btn_submit_cps);

        userLocalStore = new UserLocalStore(this);
        responseLocalStore = new ResponseLocalStore(this, userLocalStore.getLoggedInUser());

        final int totalAnswersToBeCompleted = 19;
        final int[] currentAnswersCompleted = {0};
        final int responses[] = new int[19];
        for (int lc=0;lc<19;lc++) responses[lc] = -1;

        final Map<String, Integer> position = new HashMap<>();

        position.put("age", 0);
        position.put("gender", 1);
        position.put("branch", 2);
        position.put("year", 3);
        position.put("day_scholar", 4);
        position.put("dasa", 5);
        position.put("drop_year", 6);
        position.put("annual_fam_income", 7);
        position.put("numFamMembers", 8);
        position.put("mother_edu_level", 9);
        position.put("father_edu_level", 10);
        position.put("fam_similar_bg", 11);
        position.put("involve_tech_societies", 12);
        position.put("involve_cult_clubs", 13);
        position.put("placed", 14);
        position.put("cgpa", 15);
        position.put("scstobc", 16);
        position.put("pwd", 17);
        position.put("single_child", 18);


        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("gender")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });

        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("branch")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("year")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });

        day_scholar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("day_scholar")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });

        dasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("dasa")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });

        drop_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("drop_year")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });

        annual_fam_income.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("annual_fam_income")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });
        mother_edu_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("mother_edu_level")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });
        father_edu_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("father_edu_level")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });
        fam_similar_bg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("fam_similar_bg")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });
        involve_tech_societies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("involve_tech_societies")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });
        involve_cult_clubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("involve_cult_clubs")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });
        placed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("placed")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });
        cgpa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("cgpa")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });
        scstobc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("scstobc")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });
        pwd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("pwd")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });
        single_child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                responses[position.get("single_child")] = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentAnswersCompleted[0]--;
            }
        });

        btnSubmitCPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(age.getText())) {
                    Integer value = Integer.parseInt(numFamMembers.getText().toString());
                    responses[position.get("age")] = value;
                } else {
                    currentAnswersCompleted[0]--;
                }

                if (!TextUtils.isEmpty(numFamMembers.getText())) {
                    Integer value = Integer.parseInt(numFamMembers.getText().toString());
                    responses[position.get("numFamMembers")] = value;
                } else {
                    currentAnswersCompleted[0]--;
                }

//                Toast.makeText(getApplicationContext(), String.valueOf(currentAnswersCompleted[0]), Toast.LENGTH_SHORT).show();

                if (currentAnswersCompleted[0] < 0 || isNegativeValueInResponse(responses)) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the form!!", Toast.LENGTH_SHORT).show();
                } else {
                    new submitCPS().execute(userLocalStore.getLoggedInUser().getUsername(), Arrays.toString(responses));
                }
            }
        });
    }

    private boolean isNegativeValueInResponse(int [] responses) {
        for (int val : responses) {
            if (val < 0) return true;
        }
        return false;
    }

    private class submitCPS extends AsyncTask<String, String, JSONObject> {
        String URL = baseUrl + "/storeUserResponses.php";
        JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String username = args[0];
            String response_id = "-1";
            String responses = args[1];
            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("questionnaireCode", "CPS"));
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("response_id", response_id));
            params.add(new BasicNameValuePair("responses", responses));
            JSONObject jObj = jsonParser.makeHttpRequest(URL, "POST", params);
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            if (result != null) {
                try {
                    Toast.makeText(getApplicationContext(), result.get("message").toString(), Toast.LENGTH_SHORT).show();

                    if (result.get("success").equals(1)) {
                        responseLocalStore.setCPSResponseCompleted(userLocalStore.getLoggedInUser().getUsername());
                        Intent intent = new Intent(getApplicationContext(), AllQuestionnaires.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error: Server not responding", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
