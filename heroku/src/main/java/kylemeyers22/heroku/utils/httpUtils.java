package kylemeyers22.heroku.utils;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by bcoover on 10/10/16.
 */

public class httpUtils {
    private URL url;
    private String mode;
    private Map<String, String> properties;
    private HttpURLConnection conn;

    public httpUtils(String url, String mode) throws MalformedURLException {
        this.url = new URL(url);
        this.mode = mode.toUpperCase();
    }

    private void configConnection(void) {
        return null;
    }
}
