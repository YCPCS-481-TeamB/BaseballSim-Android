
package kylemeyers22.heroku;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;


import java.io.IOException;

/**
 * Created by shdw2 on 10/23/2016.
 */

public class TeamUnitTest extends TeamFragment {

    @Test
    public void testTeamName()
    {
        try {
            String jsonMimeType = "application/json";
            HttpUriRequest request = new HttpGet("https://baseballsim.herokuapp.com/api/teams");

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
