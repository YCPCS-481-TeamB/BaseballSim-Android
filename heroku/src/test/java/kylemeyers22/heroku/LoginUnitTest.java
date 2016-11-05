package kylemeyers22.heroku;

import org.json.JSONObject;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.utils.HttpUtils;

public class LoginUnitTest {

    private String testGoodAuth;
    private String testBadAuth;
    private Map<String, String> testParams;

    private String baseAPI = "https://baseballsim.herokuapp.com/api/";
    private String loginAPI = baseAPI + "users/token/";

    @Before
    public void setUp() {
        String goodUser = "testUser";
        String goodPass = "testPass";
        String badUser = "notAdded";
        String badPass = "noPass";

        this.testParams = new HashMap<>();
        this.testParams.put("Content-Type", "application/x-www-form-urlencoded");
        this.testGoodAuth = "username=" + goodUser + "&password=" + goodPass;
        this.testBadAuth = "username=" + badUser + "&password=" + badPass;
    }

    @Test
    public void testLoginSucceed() throws IOException, JSONException {
        String replyText;
        JSONObject replyJson;

        replyText = HttpUtils.doPost(loginAPI, this.testParams, testGoodAuth);
        replyJson = new JSONObject(replyText);

        assertTrue("Login failed!", replyJson.getBoolean("success"));
    }

    @Test
    public void testLoginFail() throws IOException, JSONException {
        String replyText;
        JSONObject replyJson;

        replyText = HttpUtils.doPost(loginAPI, this.testParams, testBadAuth);
        replyJson = new JSONObject(replyText);

        assertFalse("Login unexpectedly succeeded!", replyJson.getBoolean("success"));
    }
}
