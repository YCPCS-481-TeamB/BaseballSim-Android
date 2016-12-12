package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.utils.Endpoints;
import kylemeyers22.heroku.utils.HttpUtils;

public class CreateUserActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        // Return to Login screen with Back Button
        Intent backIntent = new Intent(CreateUserActivity.this, LoginActivity.class);
        startActivity(backIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        final Button createAccountButton = (Button) findViewById(R.id.CreateAndLoginButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Direct to users endpoint for account creation
                new LongOperation().execute(Endpoints.usersAPI, Endpoints.authAPI);
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(CreateUserActivity.this);

        private EditText usernameInput = (EditText) findViewById(R.id.newUser);
        private EditText passwordInput = (EditText) findViewById(R.id.newPass);
        private EditText firstnameInput = (EditText) findViewById(R.id.newFirstname);
        private EditText lastnameInput = (EditText) findViewById(R.id.newLastname);
        private EditText emailInput = (EditText) findViewById(R.id.newEmail);

        private String uname;
        private String pword;
        private String fname;
        private String lname;
        private String email;
        private String userResponse;
        private String loginResponse;
        private String errorStatus;

        private JSONObject userJson;
        private JSONObject loginJson;
        private boolean creationSuccess;

        SharedPreferences sharedPref = CreateUserActivity.this.getPreferences(Context.MODE_PRIVATE);

        protected void onPreExecute() {
            uname = usernameInput.getText().toString();
            pword = passwordInput.getText().toString();
            fname = firstnameInput.getText().toString();
            lname = lastnameInput.getText().toString();
            email = emailInput.getText().toString();

            Dialog.setMessage("Creating account and logging in...");
            Dialog.show();
        }

        protected Void doInBackground(String... urls) {
            if (uname.equals("") || pword.equals("")) {
                cancel(true);
            } else {
                Map<String, String> props = new HashMap<>();
                props.put("Content-Type", "application/x-www-form-urlencoded");
                try {
                    String userParams = "username=" + uname + "&password=" + pword;
                    // Add optional parts if they were given
                    if (fname != null && !fname.equals("")) {
                        userParams += "&firstname=" + fname;
                    }
                    if (lname != null && !lname.equals("")) {
                        userParams += "&lastname=" + lname;
                    }
                    if (email != null && !email.equals("")) {
                        userParams += "&email=" + email;
                    }

                    userResponse = HttpUtils.doPost(urls[0], props, userParams);
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
                // Check if user creation succeeded
                try {
                    userJson = new JSONObject(userResponse);
                    creationSuccess = userJson.getBoolean("success");
                    // Login new user if succeeded, else quit
                    if (!creationSuccess) {
                        errorStatus = userJson.getString("error");
                        return null;
                    } else {
                        String loginParams = "username=" + uname + "&password=" + pword;
                        loginResponse = HttpUtils.doPost(urls[1], props, loginParams);
                    }
                } catch (JSONException | IOException iexc) {
                    iexc.printStackTrace();
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();

            if (!creationSuccess) {
                AlertDialog.Builder creationError = new AlertDialog.Builder(CreateUserActivity.this);

                creationError.setTitle("User Creation Error");
                creationError.setMessage(errorStatus);
                creationError.setPositiveButton("OK", null);
                creationError.setCancelable(true);
                creationError.create().show();

                creationError.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            } else {
                SharedPreferences.Editor editor = sharedPref.edit();
                try {
                    loginJson = new JSONObject(loginResponse);
                    editor.putString("apiToken", loginJson.getString("token"));
                    editor.apply();
                } catch (JSONException jexc) {
                    jexc.printStackTrace();
                }

                Intent intent = new Intent(CreateUserActivity.this, MainTabbedActivity.class);
                startActivity(intent);
                finish();
            }
        }

        protected void onCancelled() {
            AlertDialog.Builder createAlert = new AlertDialog.Builder(CreateUserActivity.this);

            createAlert.setMessage("You must provide a Username and Password!");
            createAlert.setTitle("Missing Credentials");
            createAlert.setPositiveButton("OK", null);
            createAlert.setCancelable(true);
            createAlert.create().show();

            createAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
        }
    }
}
