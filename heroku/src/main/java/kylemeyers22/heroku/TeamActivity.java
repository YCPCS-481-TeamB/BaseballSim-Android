package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by shdw2 on 10/9/2016.
 */
public class TeamActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team);

        //used with postgresexample
        //final TextView teamLabel = (TextView) findViewById(R.id.teamName);
        final Button getScheduleButton = (Button) findViewById(R.id.ScheduleButton);

        getScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //webserver request url
                String serverUrl = "https://baseballsim.herokuapp.com/api/teams";

                //use AsyncTask execute method to prevent ANR problem
                new LongOperation().execute(serverUrl);
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(TeamActivity.this);


        protected void onPreExecute() {
            //start progress dialog message
            Dialog.setMessage("Please Wait...");
            Dialog.show();

        }

        protected Void doInBackground(String... urls) {

            return null;
        }

        protected void onPostExecute(Void unused) {
            //close progress dialog
            Dialog.dismiss();

            Intent intent = new Intent(TeamActivity.this, ScheduleActivity.class);
            startActivity(intent);
            finish();

        }
    }
}
