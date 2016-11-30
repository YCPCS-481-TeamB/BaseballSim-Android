package kylemeyers22.heroku.apiControllers;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.apiObjects.Game;
import kylemeyers22.heroku.utils.HttpUtils;
import kylemeyers22.heroku.utils.Endpoints;

public class GameController {

    private String token;

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

        return gameFromContent(gameResponse);
    }

    public void startGame(Game toStart) throws IOException {
        Map<String, String> requestParams = initMap();

        HttpUtils.doPost(Endpoints.gameStartAPI(toStart.getGameId()), requestParams, null);
    }

    public void getNextEvent(Game toAdvance, int playerOneID, int playerTwoID) throws IOException {
        Map<String, String> requestParams = initMap();
        String nextBody = "game_id=" + toAdvance.getGameId() +
                          "&player1_id=" + playerOneID +
                          "&player2_id=" + playerTwoID;

        HttpUtils.doPost(Endpoints.gameNextAPI(toAdvance.getGameId()), requestParams, nextBody);
    }
}
