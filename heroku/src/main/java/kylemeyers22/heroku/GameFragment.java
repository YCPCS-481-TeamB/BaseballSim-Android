package kylemeyers22.heroku;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

    // Obtain API Authentication Token from LoginActivity's shared preferences
    private String apiToken;
    private int userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gameListView = (ListView) getView().findViewById(R.id.gamesList);
        SharedPreferences sPref = getActivity().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        apiToken = sPref.getString("apiToken", null);
        userID = sPref.getInt("currentUser", -1);

        final Button getGameButton = (Button) getView().findViewById(R.id.getGameButton);
        final Button gameRefresh = (Button) getView().findViewById(R.id.gameRefreshButton);

        // Fetch current history of games
        new GameFragment.LongOperation().execute(Endpoints.userGamesAPI(userID));

        // Initiate new game
        getGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStartGameForm();
            }
        });

        // Refresh game list
        gameRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GameFragment.LongOperation().execute(Endpoints.userGamesAPI(userID));
            }
        });
    }

    /**
     * Popup form for starting a new game. Team 1 is chosen from the current user's list
     * of teams, Team 2 is chosen from all other available teams.
     */
    private void createStartGameForm() {
        final Dialog startGame = new Dialog(this.getContext());
        startGame.setContentView(R.layout.start_game);
        startGame.setCancelable(true);
        startGame.setCanceledOnTouchOutside(false);

        ArrayList<Team> homeTeams = MainTabbedActivity.homeTeamList;
        ArrayList<Team> opposeTeams = MainTabbedActivity.opposeTeamList;

        Button gameStart = (Button) startGame.findViewById(R.id.startGameButton);
        final Spinner teamOneSpin = (Spinner) startGame.findViewById(R.id.team_one);
        final Spinner teamTwoSpin = (Spinner) startGame.findViewById(R.id.team_two);
        ArrayAdapter<Team> adapterHome = new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_spinner_item,
                homeTeams
        );
        ArrayAdapter<Team> adapterOppose = new ArrayAdapter<Team>(
                this.getContext(),
                android.R.layout.simple_spinner_item,
                opposeTeams
        );

        teamOneSpin.setAdapter(adapterHome);
        teamTwoSpin.setAdapter(adapterOppose);

        startGame.show();

        gameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Team teamOne = (Team) teamOneSpin.getSelectedItem();
                Team teamTwo = (Team) teamTwoSpin.getSelectedItem();

                new StartGameTask().execute(teamOne.getTeamID(), teamTwo.getTeamID());
                startGame.dismiss();
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

    private class StartGameTask extends AsyncTask<Integer, Void, Void> {
        private GameController gameController;
        private Game newGame;

        protected Void doInBackground(Integer... teamData) {
            gameController = new GameController(apiToken);

            try {
                newGame = gameController.createGame(teamData[0], teamData[1]);
            } catch (IOException | JSONException gexc) {
                System.out.println("Could not create game!");
                gexc.getCause();
                gexc.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            if (newGame == null) {
                AlertDialog.Builder failureAlert = new AlertDialog.Builder(getContext());
                failureAlert.setTitle("Game Failed");
                failureAlert.setMessage("Could not create game using those teams! Please try again");
                failureAlert.show();
                failureAlert.setCancelable(true);
            } else {
                System.out.println("New game created: " + newGame.getDateCreated() + " | " + newGame.getGameId());
                // Refresh game list
                new LongOperation().execute(Endpoints.userGamesAPI(userID));
            }
        }
    }
}
