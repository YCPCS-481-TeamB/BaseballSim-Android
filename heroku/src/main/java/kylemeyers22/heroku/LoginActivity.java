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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.utils.HttpUtils;

/**
 * Created by Ben Coover on 10/4/2016.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Button loginButton = (Button) findViewById(R.id.AuthButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Direct LongOperation to API token endpoint
                String authUrl = "https://baseballsim.herokuapp.com/api/users/token";
                new LongOperation().execute(authUrl);
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
            } catch (IOException exc) {
                exc.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();

            SharedPreferences.Editor editor = sharedPref.edit();

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
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                } else {
                    // Auth succeeded, extract and save apiToken
                    loginSuccess = true;
                    editor.putString("apiToken", loginJson.getString("token"));
                    editor.apply();
                }
            } catch (JSONException jexc) {
                jexc.printStackTrace();
            }

            // Swap activities to MainActivity if authentication passed
            if (loginSuccess) {
//                Intent intent = new Intent(LoginActivity.this, LeagueActivity.class);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
