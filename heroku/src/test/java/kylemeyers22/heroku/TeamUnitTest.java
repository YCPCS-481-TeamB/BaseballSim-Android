package kylemeyers22.heroku;

import android.content.SharedPreferences;

import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.utils.HttpUtils;

import static org.junit.Assert.assertEquals;

/**
 * Created by shdw2 on 10/23/2016.
 */

public class TeamUnitTest extends TeamActivity{

    @Test
    public void testTeamName()
    {
        try {
            String dbName = "Test Team 1";
            JSONObject jObj = new JSONObject();
            String teamName = jObj.getString("name");

            assertEquals(dbName, teamName);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }
}
