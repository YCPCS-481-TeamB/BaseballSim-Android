package kylemeyers22.heroku;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by shdw2 on 10/23/2016.
 */

public class ScheduleUnitTest extends ScheduleActivity{

    @Test
    public void testTeamName()
    {
        try {
            String jsonMimeType = "application/json";
            HttpUriRequest request = new HttpGet("https://baseballsim.herokuapp.com/api/schedules");

            // When
            HttpResponse response = HttpClientBuilder.create().build().execute(request);

            // Then
            String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
            assertEquals(jsonMimeType, mimeType);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
