package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kylemeyers22.heroku.utils.Endpoints;

public class ScheduleActivity extends AppCompatActivity{
    // TODO: This activity needed?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        final TextView scheduleLabel = (TextView) findViewById(R.id.scheduleLabel);
        final Button getGamesButton = (Button) findViewById(R.id.gamesButton);

        getGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LongOperation().execute(Endpoints.schedulesAPI);
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private String Content;
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

            Intent intent = new Intent(ScheduleActivity.this, GameFragment.class);
            startActivity(intent);
            finish();

        }
    }
}
