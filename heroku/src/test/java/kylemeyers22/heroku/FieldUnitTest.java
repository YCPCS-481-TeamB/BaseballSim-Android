package kylemeyers22.heroku;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by shdw2 on 10/23/2016.
 */

public class FieldUnitTest extends FieldActivity{
    @Test
    public void testFieldName()
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
