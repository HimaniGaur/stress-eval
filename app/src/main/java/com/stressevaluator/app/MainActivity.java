package com.stressevaluator.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stressevaluator.app.Constants.baseUrl;

public class MainActivity extends AppCompatActivity {


    EditText editEmail, editPassword, editName;
    Button btnSignIn, btnRegister, btnSignIn2;

    String URL= baseUrl + "/survey/index.php";

    JSONParser jsonParser=new JSONParser();

    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (Build.VERSION.SDK_INT> 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy().Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            System.out.println("******* MY thread is now configured to allow connections *******");
        }*/
        setContentView(R.layout.activity_main);

        editEmail= findViewById(R.id.editEmail);
        editName= findViewById(R.id.editName);
        editPassword= findViewById(R.id.editPassword);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        btnSignIn2 = findViewById(R.id.btnSignIn_2);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptLogin attemptLogin= new AttemptLogin();
                attemptLogin.execute(editName.getText().toString(),editPassword.getText().toString(),"");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(i==0)
                {
                    i=1;
                    editName.setHint("Username");
                    editEmail.setVisibility(View.VISIBLE);
                    btnSignIn.setVisibility(View.GONE);
                    btnRegister.setText("CREATE ACCOUNT");
                    btnSignIn2.setVisibility(View.VISIBLE);

                }
                else{

                    btnRegister.setText("REGISTER");
                    editEmail.setVisibility(View.GONE);
                    btnSignIn.setVisibility(View.VISIBLE);
                    i=0;

                    AttemptLogin attemptLogin= new AttemptLogin();
                    attemptLogin.execute(editName.getText().toString(),editPassword.getText().toString(),editEmail.getText().toString());

                }
            }
        });

        btnSignIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRegister.setText("REGISTER");
                btnSignIn2.setVisibility(View.GONE);
                editEmail.setVisibility(View.GONE);
                btnSignIn.setVisibility(View.VISIBLE);
                i=0;
            }
        });
    }

    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {

            String email = args[2];
            String password = args[1];
            String name= args[0];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));
            if(email.length()>0) {
                params.add(new BasicNameValuePair("email",email));
            }

            return jsonParser.makeHttpRequest(URL, "POST", params);

        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), AllQuestionnaires.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
