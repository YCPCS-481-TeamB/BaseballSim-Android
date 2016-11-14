package kylemeyers22.heroku;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.apiObjects.Team;
import kylemeyers22.heroku.utils.Constants;
import kylemeyers22.heroku.utils.HttpUtils;

public class GameFragment extends Fragment {
    private ListView gameListView;

    //interface to interact with the activity for the team id variables
    public interface OnGameCreatedListener{
        public void onTeamSelected(int teamOneId, String teamOneName, int teamTwoId, String teamTwoName);
    }

    //listener that goes with the interface
    OnGameCreatedListener mCallback;

    //attaching the listener to the activity (which is MainTabbedActivity (for some reason))
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try{
            mCallback = (OnGameCreatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException((activity.toString() + "must implement OnGameCreatedListener"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gameListView = (ListView) getView().findViewById(R.id.gamesList);

        final Button getGameButton = (Button) getView().findViewById(R.id.getGameButton);

        // Fetch current history of games
        new GameFragment.LongOperation().execute(Constants.gamesAPI);

        // Initiate new game
        getGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStartGameForm();
            }
        });
    }

    private void createStartGameForm() {
        Dialog startGame = new Dialog(this.getContext());
        startGame.setContentView(R.layout.start_game);
        startGame.setCancelable(true);
        startGame.setCanceledOnTouchOutside(false);

        ArrayList<Team> teamObjs = MainTabbedActivity.teamList;

        Button gameStart = (Button) startGame.findViewById(R.id.startGameButton);
        final Spinner teamOneSpin = (Spinner) startGame.findViewById(R.id.team_one);
        final Spinner teamTwoSpin = (Spinner) startGame.findViewById(R.id.team_two);
        ArrayAdapter<Team> adapter = new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_spinner_item,
                teamObjs
        );

        teamOneSpin.setAdapter(adapter);
        teamTwoSpin.setAdapter(adapter);

        startGame.show();

        gameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Team teamOne = (Team) teamOneSpin.getSelectedItem();
                Team teamTwo = (Team) teamTwoSpin.getSelectedItem();
                System.out.println("Team 1: " + teamOne.toString() + " | " + teamOne.getTeamID());
                System.out.println("Team 2: " + teamTwo.toString() + " | " + teamTwo.getTeamID());

                //send the event to the host activity
                mCallback.onTeamSelected(teamOne.getTeamID(), teamOne.toString(), teamTwo.getTeamID(), teamTwo.toString());


                //creating a new game
                Intent intent = new Intent(v.getContext(), CreateNewGameActivity.class);
                startActivity(intent);
                //finish();
            }
        });

    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(getActivity());
        private ArrayAdapter<String> listAdapter;

        // Obtain API Authentication Token from LoginActivity's shared preferences
        SharedPreferences sPref = getActivity().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        final String apiToken = sPref.getString("apiToken", null);

        protected void onPreExecute() {
            //start progress dialog message
            Dialog.setMessage("Please Wait...");
            Dialog.show();
        }

        protected Void doInBackground(String... urls) {
            try {
                Map<String, String> props = new HashMap<>();
                props.put("x-access-token", apiToken);

                Content = HttpUtils.doGet(urls[0], props);
            } catch (IOException iexc) {
                iexc.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            //close progress dialog
            Dialog.dismiss();

            System.out.println("#---- IN onPostExecute ----#");
            System.out.println(Content);

            ArrayList<String> gameList = new ArrayList<>();

            try {
                JSONObject jObj = new JSONObject(Content);
                System.out.println("##########");

                JSONArray gamesArray = jObj.getJSONArray("games");
                for (int i = 0; i < gamesArray.length(); ++i) {
                    JSONObject item = gamesArray.getJSONObject(i);

                    //the games take two ints for teams and shows them. Need to find a way to get the ints of teams and show them as strings. I'm not the get at JSON
                    //gameList.add(item.getInt("team1_id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(gameList.size());
            listAdapter = new ArrayAdapter<>(getActivity(), R.layout.listrow, gameList);
            gameListView.setAdapter(listAdapter);
        }
    }
}
