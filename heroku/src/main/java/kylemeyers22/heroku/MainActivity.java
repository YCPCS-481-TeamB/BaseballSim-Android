package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

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

        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

        String data = "";
        TextView uiUpdate = (TextView) findViewById(R.id.playerFirstName);
        TextView jsonParsed = (TextView) findViewById(R.id.playerLastName);
        TextView serverText = (TextView) findViewById(R.id.serverText);

        protected void onPreExecute() {
            //start progress dialog message
            Dialog.setMessage("Please Wait...");
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
                URL url = new URL("https://baseballsim.herokuapp.com/api" + "/players");

                //send post data request
                URLConnection conn = url.openConnection();
                System.out.println("---- IN MAINACTIVITY ----");
                System.out.println("CONN OUTPUT: " + conn.getContent().toString());

                //read server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Content = sb.toString();

            } catch (Exception ex) {
                Error = ex.getMessage();
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                    System.out.println("Could not close reader!");
                }
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            //close progress dialog
            Dialog.dismiss();
            //Intent intent = new Intent(MainActivity.this, FieldActivity.class);
           // startActivity(intent);
            //finish();

            if (Error != null) {
                uiUpdate.setText("Output : " + Error);
            } else {
                uiUpdate.setText(Content);

                //Receive JSON response
                String OutputData;

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