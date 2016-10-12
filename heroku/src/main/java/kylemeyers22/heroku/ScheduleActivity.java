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
public class ScheduleActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        //used with postgresexample
        final TextView scheduleLabel = (TextView) findViewById(R.id.scheduleLabel);
        final Button getGamesButton = (Button) findViewById(R.id.gamesButton);

        getGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //webserver request url
                String serverUrl = "https://baseballsim.herokuapp.com/api/schedules";

                //use AsyncTask execute method to prevent ANR problem
                new LongOperation().execute(serverUrl);
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(ScheduleActivity.this);


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

            Intent intent = new Intent(ScheduleActivity.this, GameActivity.class);
            startActivity(intent);
            finish();

        }
    }
}