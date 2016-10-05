package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                String authUrl = "https://baseballsim.herokuapp.com/api/users/token";
                new LongOperation().execute(authUrl);
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);
        private TextView usernameInput = (TextView) findViewById(R.id.UsernameInput);
        private TextView passwordInput = (TextView) findViewById(R.id.PasswordInput);
        private String uname, pword;

        protected void onPreExecute() {
            uname = usernameInput.getText().toString();
            pword = usernameInput.getText().toString();

            Dialog.setMessage("Logging in...");
            Dialog.show();
        }

        protected Void doInBackground(String... urls) {
            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
