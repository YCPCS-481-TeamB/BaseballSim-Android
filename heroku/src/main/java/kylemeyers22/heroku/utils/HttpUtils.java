package kylemeyers22.heroku.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bcoover on 10/10/16.
 *
 * Provides commonly used HTTP operations to perform API calls
 */

public class HttpUtils {
    private static HttpURLConnection httpConn;

    /**
     * Submit data over an HttpURLConnection OutputStream
     *
     * @param toSend The String to submit to the Connection's OutputStream
     * @throws IOException
     */
    private static void sendData(String toSend) throws IOException {
        OutputStream connOutput = httpConn.getOutputStream();
        BufferedWriter dataWriter = new BufferedWriter(new OutputStreamWriter(connOutput, "UTF-8"));

        dataWriter.write(toSend);
        dataWriter.flush();
        dataWriter.close();
        connOutput.close();
    }

    /**
     * Read the response (if any) from an HttpURLConnection
     *
     * @return A String containing contents of the Connection's InputStream
     * @throws IOException
     */
    private static String readResponse() throws IOException {
        InputStreamReader urlInput = new InputStreamReader(httpConn.getInputStream());
        BufferedReader inputReader = new BufferedReader(urlInput);
        StringBuilder readString = new StringBuilder();
        String line;

        while ((line = inputReader.readLine()) != null) {
            readString.append(line);
        }
        inputReader.close();

        return readString.toString();
    }

    /**
     * Perform an HTTP POST request
     *
     * @param urlString The URL where the request is being sent
     * @param properties A Map containing any HTTP Request properties to apply
     * @param postData A String containing data to POST
     * @return The reply from the POST Request
     * @throws IOException
     */
    public static String doPost(String urlString,
                                Map<String, String> properties,
                                String postData) throws IOException {
        URL url = new URL(urlString);
        httpConn = (HttpURLConnection) url.openConnection();

        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        for (Map.Entry<String, String> property : properties.entrySet()) {
            httpConn.setRequestProperty(property.getKey(), property.getValue());
        }

        sendData(postData);

        return readResponse();
    }

    /**
     * Perform an HTTP GET request
     *
     * @param urlString The URL where the request is being sent
     * @param properties A Map containing any HTTP Request properties to apply
     * @return The reply from the GET Request
     * @throws IOException
     */
    public static String doGet(String urlString,
                               Map<String, String> properties) throws IOException {
        URL url = new URL(urlString);
        httpConn = (HttpURLConnection) url.openConnection();

        httpConn.setRequestMethod("GET");
        for (Map.Entry<String, String> property : properties.entrySet()) {
            httpConn.setRequestProperty(property.getKey(), property.getValue());
        }

        return readResponse();
    }

    /**
     * Workaround method to obtain API info pertaining to the current user.
     *
     * User ID is required for certain user-specific info, which is only obtainable from
     * the "users" Endpoint, which is only intended to be used for debugging.
     *
     * @return The current user's ID, or -1 if it does not exist
     * @throws IOException
     */
    public static int getCurrentUserID(String username) throws IOException {

        String userContent = HttpUtils.doGet(Endpoints.usersAPI, new HashMap<String, String>());
        try {
            JSONObject userJson = new JSONObject(userContent);
            JSONArray allUsers = userJson.getJSONArray("users");

            for (int i = 0; i < allUsers.length(); ++i) {
                JSONObject current = allUsers.getJSONObject(i);
                if (current.getString("username").equals(username)) {
                    return current.getInt("id");
                }
            }
        } catch (JSONException jexc) {
            jexc.getCause();
        }

        // Current user does not exist (should never happen)
        return -1;
    }
}
