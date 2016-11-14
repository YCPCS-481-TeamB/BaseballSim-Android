package kylemeyers22.heroku;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.utils.Constants;
import kylemeyers22.heroku.utils.HttpUtils;
import kylemeyers22.heroku.utils.TeamVariables;


public class CreateNewGameActivity extends AppCompatActivity{
    private int teamOneId;
    private String teamOneName;

    private int teamTwoId;
    private String teamTwoName;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("create the new game");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);


        teamOneId = TeamVariables.getTeamOneId();
        teamOneName = TeamVariables.getTeamOneName();

        teamTwoId = TeamVariables.getTeamTwoId();
        teamTwoName = TeamVariables.getTeamTwoName();

        System.out.println(teamOneName +" whoo " + teamOneId);
        System.out.println(teamTwoName +" whoo " + teamTwoId);

        new LongOperation().execute(Constants.gamesAPI);
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(CreateNewGameActivity.this);
        private String gameId;
        private JSONObject gameCreateJson;
        private boolean gameCreateSuccess = false;


        SharedPreferences sharedPref = CreateNewGameActivity.this.getPreferences(Context.MODE_PRIVATE);

        protected void onPreExecute() {


            Dialog.setMessage("Creating game and positioning players...");
            Dialog.show();
        }

        protected Void doInBackground(String... urls) {

            try {
                //creating the game with all the appropriate variables
                String authParams = "team1_id=" + teamOneName + "team2_id=" + teamTwoId +
                        "field_id=" + 0 + "league_id=" + 0 + "date_created=" + "";
                Map<String, String> props = new HashMap<>();
                props.put("Content-Type", "application/x-www-form-urlencoded");

                gameId = HttpUtils.doPost(urls[0], props, authParams);
            } catch (IOException exc) {
                exc.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            Dialog.dismiss();

            SharedPreferences.Editor editor = sharedPref.edit();

            try {
                gameCreateJson = new JSONObject(gameId);

                //check the http reply for a successful game creation
                if(!gameCreateJson.getBoolean("success"))
                {
                    AlertDialog.Builder gameAlert = new AlertDialog.Builder(CreateNewGameActivity.this);

                    //creaste and display an error message
                    gameAlert.setMessage("Invalid arguments for game");
                    gameAlert.setTitle("Game Creation Failure");
                    gameAlert.setPositiveButton("OK", null);
                    gameAlert.setCancelable(true);
                    gameAlert.create().show();

                    gameAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                }  else
                {
                    //game created successfully
                    gameCreateSuccess = true;
                    //getting the id of the game
                    editor.putString("game_id",gameCreateJson.getString("id") );
                    editor.apply();
                }
            } catch(JSONException jexc){
                jexc.printStackTrace();
            }

            if(gameCreateSuccess)
            {
                //swap to gameplay or?
            }
        }

    }
}
