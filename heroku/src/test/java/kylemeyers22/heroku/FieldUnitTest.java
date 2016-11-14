package kylemeyers22.heroku;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.utils.HttpUtils;

import static org.junit.Assert.assertNotNull;

public class FieldUnitTest {
    private Map<String, String> testParams;

    private String fieldAPI = "https://baseballsim.herokuapp.com/api/fields";

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
    public void testGetFields() throws IOException, JSONException {
        String fieldReply;
        JSONObject fieldJson;

        fieldReply = HttpUtils.doGet(fieldAPI, testParams);
        fieldJson = new JSONObject(fieldReply);

        assertNotNull("\"Fields\" does not exist!", fieldJson.getJSONArray("fields"));
    }
}
