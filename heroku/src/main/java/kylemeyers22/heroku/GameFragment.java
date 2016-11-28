package kylemeyers22.heroku;

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

import kylemeyers22.heroku.adapters.GameListItemAdapter;
import kylemeyers22.heroku.apiControllers.GameController;
import kylemeyers22.heroku.apiObjects.Game;
import kylemeyers22.heroku.apiObjects.Team;
import kylemeyers22.heroku.utils.Endpoints;
import kylemeyers22.heroku.utils.HttpUtils;

public class GameFragment extends Fragment {
    private ListView gameListView;
    private GameController gameController;

    // Obtain API Authentication Token from LoginActivity's shared preferences
    SharedPreferences sPref;
    String apiToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gameListView = (ListView) getView().findViewById(R.id.gamesList);
        sPref = getActivity().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        apiToken = sPref.getString("apiToken", null);

        final Button getGameButton = (Button) getView().findViewById(R.id.getGameButton);

        // Fetch current history of games
        new GameFragment.LongOperation().execute(Endpoints.gamesAPI);

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

                gameController = new GameController(apiToken);
                try {
                    gameController.createGame(teamOne.getTeamID(), teamTwo.getTeamID());
                } catch (IOException | JSONException gexc) {
                    System.out.println("Game creation failed!");
                    gexc.getCause();
                    gexc.printStackTrace();
                }
            }
        });

    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private String Content;
        private ProgressDialog Dialog = new ProgressDialog(getActivity());
        private GameListItemAdapter gameAdapter;

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

            ArrayList<Game> gameItems = new ArrayList<>();

            try {
                JSONObject jObj = new JSONObject(Content);

                JSONArray gamesArray = jObj.getJSONArray("games");
                for (int i = 0; i < gamesArray.length(); ++i) {
                    JSONObject item = gamesArray.getJSONObject(i);
                    gameItems.add(new Game(item.getInt("id"),
                            item.getInt("team1_id"),
                            item.getInt("team2_id"),
                            item.getInt("field_id"),
                            item.getInt("league_id"),
                            item.getString("date_created")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            gameAdapter = new GameListItemAdapter(getActivity(), R.layout.gamerow, gameItems);
            gameListView.setAdapter(gameAdapter);
        }
    }
}
