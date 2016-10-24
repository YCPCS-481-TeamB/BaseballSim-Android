
package kylemeyers22.heroku;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import android.content.SharedPreferences;

import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.utils.HttpUtils;

import kylemeyers22.heroku.TeamActivity;

/**
 * Created by shdw2 on 10/23/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 23)
//@Config(constants = BuildConfig.class)

public class TeamUnitTest{

    @Test
    public void testTeamName()
    {
        try {
            String jsonMimeType = "application/json";
            HttpUriRequest request = new HttpGet("https://baseballsim.herokuapp.com/api/users/token");

            // When
            HttpResponse response = HttpClientBuilder.create().build().execute(request);

            // Then
            assertThat(response.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
           // String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
            //assertEquals(jsonMimeType, mimeType);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
