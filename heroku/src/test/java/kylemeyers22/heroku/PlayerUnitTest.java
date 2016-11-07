package kylemeyers22.heroku;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.utils.HttpUtils;

public class PlayerUnitTest {

    private Map<String, String> testParams;

    private String playerAPI = "https://baseballsim.herokuapp.com/api/players";

    @Before
    public void setUp() throws IOException, JSONException {
        String authToken;
        String tokenAPI = "https://baseballsim.herokuapp.com/api/users/token/";
        String testAuth = "username=testUser&password=testPass";

        this.testParams = new HashMap<>();
        this.testParams.put("Content-Type", "application/x-www-form-urlencoded");

        String authReply = HttpUtils.doPost(tokenAPI, testParams, testAuth);
        JSONObject authJson = new JSONObject(authReply);
        authToken = authJson.getString("token");
        this.testParams.put("x-access-token", authToken);
    }

    @Test
    public void testGetPlayers() throws IOException, JSONException {
        String playerReply;
        JSONObject playerJson;

        playerReply = HttpUtils.doGet(playerAPI, testParams);
        playerJson = new JSONObject(playerReply);

        assertNotNull("\"Players\" does not exist!", playerJson.getJSONArray("players"));
    }
}
