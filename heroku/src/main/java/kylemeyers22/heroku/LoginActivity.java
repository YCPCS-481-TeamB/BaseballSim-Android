package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        private String uname, pword, authToken;

        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);

        protected void onPreExecute() {
            uname = usernameInput.getText().toString();
            pword = passwordInput.getText().toString();

            // FOR DEBUGGING
//            uname = "bcoover";
//            pword = "devpass";

            Dialog.setMessage("Logging in...");
            Dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL getToken;
            HttpURLConnection conn;

            try {
                System.out.println("---- IN LoginActivity.doInBackground");
                getToken = new URL(urls[0]);
                String authParams = "username=" + uname + "&password=" + pword;
                byte[] authData = authParams.getBytes("UTF-8");

                // Setup POST configuration
                conn = (HttpURLConnection) getToken.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Submit auth params
                conn.setFixedLengthStreamingMode(authData.length);
                OutputStream postData = conn.getOutputStream();
                postData.write(authData);
                postData.flush();
                postData.close();

                // Read server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sBuilder.append(line);
                }
                authToken = sBuilder.toString();
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
                JSONObject loginJson = new JSONObject(authToken);
                editor.putString("apiToken", loginJson.getString("token"));
                editor.apply();
            } catch (JSONException jexc) {
                jexc.printStackTrace();
            }
            // Swap activities to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
