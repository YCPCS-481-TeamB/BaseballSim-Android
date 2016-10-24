package kylemeyers22.heroku;

import android.app.Activity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by shdw2 on 10/23/2016.
 */

public class GameUnitTest extends GameFragment {
    @Mock
    HttpURLConnection mockHttpConnection;

    GameUnitTest.HttpUrlActivity activity;

    public class HttpUrlActivity extends Activity {

        private HttpURLConnection httpConnection;

        // this would be in the for real application
        public HttpUrlActivity() throws MalformedURLException, IOException {
            this((HttpURLConnection) new URL("http://baseballsim.heroku.com/api/games").openConnection());
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
        activity = new GameUnitTest.HttpUrlActivity(mockHttpConnection);
    }

    @Test
    public void itCanTestHttpURLConnectionStuff() throws ProtocolException {
        shadowOf(activity).recreate();
        verify(mockHttpConnection).setRequestMethod("GET");
    }
}
