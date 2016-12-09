package kylemeyers22.heroku.apiControllers;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import kylemeyers22.heroku.apiObjects.Game;
import kylemeyers22.heroku.utils.HttpUtils;
import kylemeyers22.heroku.utils.Endpoints;

public class GameController {

    private String token;
    private boolean status;
    private boolean isApproved;

    public GameController(String token) {
        this.token = token;
    }

    private Map<String, String> initMap() {
        Map<String, String> initParams = new HashMap<>();
        initParams.put("x-access-token", token);

        return initParams;
    }

    private Game gameFromContent(String gameContent) throws JSONException {
        JSONObject gameJson = new JSONObject(gameContent).getJSONObject("game");

        return new Game(gameJson.getInt("id"),
                gameJson.getInt("team1_id"), gameJson.getInt("team2_id"),
                gameJson.getInt("field_id"), gameJson.getInt("league_id"),
                gameJson.getString("date_created"));
    }

    public Game createGame(int teamOneID, int teamTwoID) throws IOException, JSONException {
        Map<String, String> requestParams = initMap();
        // field_id and league_id currently hardcoded.
        // TODO: Provide handling for parameterized field and league ID
        String requestBody = "team1_id=" + Integer.toString(teamOneID) +
                             "&team2_id=" + Integer.toString(teamTwoID) +
                             "&field_id=0" + "&league_id=0";

        String gameResponse = HttpUtils.doPost(Endpoints.gamesAPI, requestParams, requestBody);
        if (!new JSONObject(gameResponse).getBoolean("success")) {
            return null;
        }

        return gameFromContent(gameResponse);
    }

    public boolean isGameApproved(Game toCheck) throws IOException, JSONException {
        try {
            // Wait for background task to complete
            new IsGameApproved().execute(toCheck).get();
        } catch (ExecutionException | InterruptedException exec) {
            exec.printStackTrace();
        }
        return isApproved;
    }

    private class IsGameApproved extends AsyncTask<Game, Void, Void> {
        private String reply;

        protected Void doInBackground(Game... games) {
            Map<String, String> requestParams = initMap();

            try {
                reply = HttpUtils.doGet(
                        Endpoints.gameIsApprovedAPI(games[0].getGameId()), requestParams
                );
                JSONArray gameApproval = new JSONObject(reply).getJSONArray("approvals");
                // Should only ever be a single approval in this array. Failing that,
                // slot zero should be the most recent.
                JSONObject gameStatus = gameApproval.getJSONObject(0);

                // True if "approved" value is "approved", False otherwise
                isApproved = gameStatus.getString("approved").equals("approved");
            } catch (IOException | JSONException iexc) {
                iexc.printStackTrace();
            }
            return null;
        }
    }

    public boolean isGameStarted(Game toCheck) throws IOException, JSONException {
        try {
            // Wait for background task to finish
            new IsGameStarted().execute(toCheck).get();
        } catch (ExecutionException | InterruptedException exec) {
            exec.printStackTrace();
        }

        return status;
    }

    private class IsGameStarted extends AsyncTask<Game, Void, Void> {
        private String reply;

        protected Void doInBackground(Game... games) {
            Map<String, String> requestParams = initMap();

            try {
                reply = HttpUtils.doGet(
                        Endpoints.gameEventsAllAPI(games[0].getGameId()), requestParams
                );
                JSONArray gameEvents = new JSONObject(reply).getJSONArray("events");

                status = (gameEvents.length() >= 1 &&
                          gameEvents.getJSONObject(0).getString("type").equals("start"));
            } catch (IOException | JSONException iexc) {
                iexc.printStackTrace();
            }
            return null;
        }
    }

    public void startGame(Game toStart) throws IOException {
        new StartGame().execute(toStart);
    }

    private class StartGame extends AsyncTask<Game, Void, Void> {
        protected Void doInBackground(Game... games) {
            Map<String, String> requestParams = initMap();

            try {
                HttpUtils.doPost(Endpoints.gameStartAPI(games[0].getGameId()), requestParams, "");
            } catch (IOException iexc) {
                System.out.print("Could not start game!");
                iexc.printStackTrace();
            }
            return null;
        }
    }

    public void getNextEvent(Game toAdvance, int playerOneID, int playerTwoID) throws IOException {
        Map<String, String> requestParams = initMap();
        String nextBody = "game_id=" + toAdvance.getGameId() +
                          "&player1_id=" + playerOneID +
                          "&player2_id=" + playerTwoID;

        HttpUtils.doPost(Endpoints.gameNextAPI(toAdvance.getGameId()), requestParams, nextBody);
    }
}
