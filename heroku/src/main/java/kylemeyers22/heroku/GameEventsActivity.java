package kylemeyers22.heroku;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kylemeyers22.heroku.utils.Constants;
import kylemeyers22.heroku.utils.TeamVariables;

public class GameEventsActivity extends AppCompatActivity{

    private int teamOneId;
    private String teamOneName;

    private int teamTwoId;
    private String teamTwoName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("Creating events for the game");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);


        teamOneId = TeamVariables.getTeamOneId();
        teamOneName = TeamVariables.getTeamOneName();

        teamTwoId = TeamVariables.getTeamTwoId();
        teamTwoName = TeamVariables.getTeamTwoName();

        System.out.println(teamOneName +" whoo event " + teamOneId);
        System.out.println(teamTwoName +" whoo event " + teamTwoId);

//probably needs to be an int or json obj
        String gameId = getIntent().getExtras().getString("gameId");

        System.out.println("Game ID: " +gameId);

        new GameEventsActivity.LongOperation().execute(Constants.gamesAPI + "/" + gameId + "/start/");
        //where :id is the gameId
        //the api route for the events would be Constants.gamesAPI + "/events/:event_id/positions
        //to get the latest event is Constants.gamesAPI + /:id/positions/latest
        //to get all the events is Constants.gamesAPI + /:id/events
        //to calculate the next event Constants.gamesAPI + /:id/events/next
        //there are more! but these are the ones to focus on
    }
    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(GameEventsActivity.this);

        protected void onPreExecute() {


            Dialog.setMessage("Events are being created as we speak...");
            Dialog.show();
        }

        protected Void doInBackground(String... urls)
        {
            return null;
        }

        protected void onPostExecute(Void unused)
        {

        }
    }
}
