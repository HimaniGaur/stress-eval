package com.stressevaluator.app;

import android.content.Intent;
import android.os.AsyncTask;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;



import java.util.ArrayList;

import static com.stressevaluator.app.Constants.baseUrl;



public class LoginRegisterActivity extends AppCompatActivity {

    EditText editEmail, editPassword, editName;
    Button btnSignIn, btnRegister;

    String URL= baseUrl + "/index.php";

    JSONParser jsonParser=new JSONParser();

    int i=0;

    UserLocalStore userLocalStore;
    ResponseLocalStore responseLocalStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String TAG = "LoginRegisterActivity";
        
        super.onCreate(savedInstanceState);


        FirebaseMessaging.getInstance().subscribeToTopic("test").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Subscribed to test topic",Toast.LENGTH_LONG).show();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(LoginRegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        setContentView(R.layout.activity_login_register);

        editEmail= findViewById(R.id.editEmail);
        editName= findViewById(R.id.editName);
        editPassword= findViewById(R.id.editPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        userLocalStore = new UserLocalStore(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==1)
                {
                    i=0;
                    btnRegister.setText("REGISTER");
                    editEmail.setVisibility(View.GONE);
                }
                else{
                    AttemptLogin attemptLogin= new AttemptLogin();
                    attemptLogin.execute(editName.getText().toString(),editPassword.getText().toString(),"");

                    String username = editName.getText().toString();
                    String password = editPassword.getText().toString();

                /*    editName.setHint("Username");
                    editEmail.setVisibility(View.VISIBLE);
                    btnRegister.setText("CREATE ACCOUNT");*/
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(i==0)
                {
                    i=1;
                    editName.setHint("Username (10 length SID)");
                    editEmail.setVisibility(View.VISIBLE);
                    btnRegister.setText("CREATE ACCOUNT");
                }
                else{
                    AttemptLogin attemptLogin= new AttemptLogin();
                    attemptLogin.execute(editName.getText().toString(),editPassword.getText().toString(),editEmail.getText().toString());
                }
            }
        });
    }

    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        String name, email, password;

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {

            this.email = args[2];
            this.password = args[1];
            this.name= args[0];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));
            if(email.length()>0) {
                params.add(new BasicNameValuePair("email",email));
            }

            return jsonParser.makeHttpRequest(URL, "POST", params);

        }

        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();

                    if (result.getString("success").equals("1")) {

                        // store user details now
                        User registeredUser = new User(name, email, password);
                        userLocalStore.storeUserData(registeredUser);
                        userLocalStore.setUserLoggedIn(true);

                        // set its responseLocalStore too
                        responseLocalStore = new ResponseLocalStore(getApplicationContext(), registeredUser);

                        Intent intent = null;
                        if (i == 1) { //user registered
                            intent = new Intent(getApplicationContext(), ClinicalProfile.class);
                        } else {
                            intent = new Intent(getApplicationContext(), AllQuestionnaires.class);
                        }
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: Server not responding", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
