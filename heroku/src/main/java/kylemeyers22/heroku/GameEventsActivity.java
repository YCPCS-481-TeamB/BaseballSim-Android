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
        System.out.println("creaing events for the game");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);


        teamOneId = TeamVariables.getTeamOneId();
        teamOneName = TeamVariables.getTeamOneName();

        teamTwoId = TeamVariables.getTeamTwoId();
        teamTwoName = TeamVariables.getTeamTwoName();

        System.out.println(teamOneName +" whoo " + teamOneId);
        System.out.println(teamTwoName +" whoo " + teamTwoId);

//probably needs to be an int or json obj
        String gameId = getIntent().getExtras().getString("gameId");

        new GameEventsActivity.LongOperation().execute(Constants.gamesAPI + "/:id/start/");
    }
    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(GameEventsActivity.this);

        protected void onPreExecute() {


            Dialog.setMessage("Creating game and positioning players...");
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
