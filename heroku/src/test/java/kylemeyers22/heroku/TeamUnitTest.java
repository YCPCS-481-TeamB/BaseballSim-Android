
package kylemeyers22.heroku;

import android.app.Activity;
import android.os.Bundle;

import org.apache.http.HttpStatus;
import org.junit.Before;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;



import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Response;

/**
 * Created by shdw2 on 10/23/2016.
 */

import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

import java.io.IOException;
import java.net.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import android.app.Activity;
import android.os.Bundle;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 23)
//@Config(constants = BuildConfig.class)

public class TeamUnitTest{

        @Mock
        HttpURLConnection mockHttpConnection;

        HttpUrlActivity activity;

        public class HttpUrlActivity extends Activity {

            private HttpURLConnection httpConnection;

            // this would be in the for real application
            public HttpUrlActivity() throws MalformedURLException, IOException {
                this((HttpURLConnection) new URL("http://baseballsim.heroku.com/api/teams").openConnection());
            }

            // this is how we inject the mock in our test
            public HttpUrlActivity(final HttpURLConnection httpConnection) {
                this.httpConnection = httpConnection;
            }

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                try {
                    httpConnection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                }
                super.onCreate(savedInstanceState);
            }

        }

        @Before
        public void setUp() {
        MockitoAnnotations.initMocks(this);
        activity = new HttpUrlActivity(mockHttpConnection);
    }

        @Test
        public void itCanTestHttpURLConnectionStuff() throws ProtocolException {
        shadowOf(activity).recreate();
        verify(mockHttpConnection).setRequestMethod("GET");
    }
}
