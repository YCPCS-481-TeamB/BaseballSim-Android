package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.icu.text.IDNA;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        /*used for herokutestandroid
        final Button button = (Button) findViewById(R.id.button);
        final TextView textView = (TextView) findViewById(R.id.textView);*/

        //used with postgresexample
        final TextView playerFirstLabel = (TextView) findViewById(R.id.playerFirstNameLabel);
        final Button getPlayerButton = (Button) findViewById(R.id.getPlayerButton);

        getPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //webserver request url
                String serverUrl = "https://baseballsim.herokuapp.com/api/players";

                //use AsyncTask execute method to prevent ANR problem
                new LongOperation().execute(serverUrl);
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {
        //private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        String data = "";
        TextView uiUpdate = (TextView) findViewById(R.id.playerFirstName);
        TextView jsonParsed = (TextView) findViewById(R.id.playerLastName);
        int sizeData = 0;
        TextView serverText = (TextView) findViewById(R.id.serverText);

        protected void onPreExecute() {
            //start progress dialog message
            Dialog.setMessage("Please Wait....");
            Dialog.show();

            try {
                //set request parameter
                data += "&" + URLEncoder.encode("data", "UTF-8") + "=" + serverText.getText();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        protected Void doInBackground(String... urls) {
            //make post call to server
            BufferedReader reader = null;

            //send data
            try {
                //defined url where to send data
                //URL url = new URL(urls[0]);
                URL url = new URL("https://baseballsim.herokuapp.com/api" + "/users");

                //send post data request

                URLConnection conn = url.openConnection();
                System.out.println("---- IN MAINACTIVITY ----");
                System.out.println("CONN OUTPUT: " + conn.getContent().toString());
//                conn.setDoOutput(true);
//                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//                wr.write(data);
//                wr.flush();
//
//                //get the server response
//
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";

                //read server response
                while ((line = reader.readLine()) != null) {
                    //append server response n string
                    sb.append(line);
                }

                //append server response to content string
                Content = sb.toString();
            } catch (Exception ex) {
                // Error = ex.getMessage();
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                }
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            //close progress dialog
            Dialog.dismiss();

            if (Error != null) {
                uiUpdate.setText("Output : " + Error);
            } else {
                uiUpdate.setText(Content);

                //start parse response json

                String OutputData = "";
                JSONObject jsonResponse;

                System.out.println("#---- IN onPostExecute ----#");
                System.out.println(Content);
                try {
                    JSONObject jObj = new JSONObject(Content);
                    System.out.println("##########");
                    System.out.println(jObj.getJSONArray("users").getJSONObject(0).getString("firstname"));

                    OutputData = "Output captured: " + jObj.getJSONArray("users").getJSONObject(0).getString("firstname");

                    //show output on screen
                    jsonParsed.setText(OutputData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}