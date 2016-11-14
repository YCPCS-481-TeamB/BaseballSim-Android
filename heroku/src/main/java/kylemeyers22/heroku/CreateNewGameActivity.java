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


        protected void onPreExecute() {


            Dialog.setMessage("Creating game and positioning players...");
            Dialog.show();
        }

        protected Void doInBackground(String... urls) {


            return null;
        }

        protected void onPostExecute(Void unused) {

        }

    }
}
