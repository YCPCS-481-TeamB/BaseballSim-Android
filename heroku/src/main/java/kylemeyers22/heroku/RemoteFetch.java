package kylemeyers22.heroku;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Created by shdw2 on 10/2/2016.
 */
public class RemoteFetch {

    private static final String BASEBALL_SIM_API = "http://baseballsim.herokuapp.com/api";

    public static JSONObject getJSON(Context context, String player)
    {
        try {
            URL url = new URL(String.format(BASEBALL_SIM_API, player));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",context.getString(R.string.baseballsim_api_id));

        }catch (Exception e)
        {
            return null;
        }
    }
}
