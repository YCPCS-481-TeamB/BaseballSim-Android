package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.utils.Endpoints;
import kylemeyers22.heroku.utils.HttpUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Button loginButton = (Button) findViewById(R.id.AuthButton);
        final Button newUserButton = (Button) findViewById(R.id.NewUserButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LongOperation().execute(Endpoints.authAPI);
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);
        private TextView usernameInput = (TextView) findViewById(R.id.UsernameInput);
        private TextView passwordInput = (TextView) findViewById(R.id.PasswordInput);

        private String uname;
        private String pword;
        private String authToken;

        private JSONObject loginJson;
        private boolean loginSuccess = false;
        private boolean serviceFailure = false;

        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);

        protected void onPreExecute() {
//            uname = usernameInput.getText().toString();
//            pword = passwordInput.getText().toString();

            // FOR DEBUGGING
            uname = "bcoover";
            pword = "devpass";

            Dialog.setMessage("Logging in...");
            Dialog.show();
        }

        protected Void doInBackground(String... urls) {
            try {
                String authParams = "username=" + uname + "&password=" + pword;
                Map<String, String> props = new HashMap<>();
                props.put("Content-Type", "application/x-www-form-urlencoded");

                authToken = HttpUtils.doPost(urls[0], props, authParams);
            } catch (FileNotFoundException exc) {
                // Occurs if the service is unavailable for some reason
                serviceFailure = true;
            } catch (IOException iexc) {
                iexc.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();

            SharedPreferences.Editor editor = sharedPref.edit();

            // Trigger an alert if the service is unavailable
            if (serviceFailure) {
                AlertDialog.Builder failBuilder = new AlertDialog.Builder(LoginActivity.this);

                failBuilder.setMessage("Service is currently unavailable");
                failBuilder.setTitle("Service Failure");
                failBuilder.setPositiveButton("OK", null);
                failBuilder.setCancelable(true);
                failBuilder.create().show();

                failBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            } else {
                // Parse JSON for API Token and save to Shared Preferences for access elsewhere
                try {
                    loginJson = new JSONObject(authToken);

                    // Check the HTTP Reply for a successful authentication
                    if (!loginJson.getBoolean("success")) {
                        AlertDialog.Builder loginAlert = new AlertDialog.Builder(LoginActivity.this);

                        // Create and display an error dialog
                        loginAlert.setMessage("Invalid Password and/or Username");
                        loginAlert.setTitle("Login Failure");
                        loginAlert.setPositiveButton("OK", null);
                        loginAlert.setCancelable(true);
                        loginAlert.create().show();

                        loginAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                    } else {
                        // Auth succeeded, extract and save apiToken
                        loginSuccess = true;
                        editor.putString("apiToken", loginJson.getString("token"));
                        editor.putString("currentUser", uname);
                        editor.apply();
                    }
                } catch (JSONException jexc) {
                    jexc.printStackTrace();
                }
            }

            // Swap activities to PlayerFragment if authentication passed
            if (loginSuccess) {
                Intent intent = new Intent(LoginActivity.this, MainTabbedActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
