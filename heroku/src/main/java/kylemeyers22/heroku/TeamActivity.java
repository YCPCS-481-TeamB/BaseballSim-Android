package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.utils.HttpUtils;

/**
 * Created by shdw2 on 10/9/2016.
 */
public class TeamActivity extends AppCompatActivity {
    private ListView teamListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team);
        teamListView = (ListView) findViewById(R.id.teamsList);

        final Button getTeamButton = (Button) findViewById(R.id.getTeamButton);

        getTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //webserver request url
                String serverUrl = "https://baseballsim.herokuapp.com/api/teams";

                //use AsyncTask execute method to prevent ANR problem
                new TeamActivity.LongOperation().execute(serverUrl);
            }
        });
    }

    public class LongOperation extends AsyncTask<String, Void, Void> {

        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(TeamActivity.this);
        private ArrayAdapter<String> listAdapter;

        String data = "";
//        TextView uiUpdate = (TextView) findViewById(R.id.playerFirstName);
//        TextView jsonParsed = (TextView) findViewById(R.id.playerLastName);
//        TextView serverText = (TextView) findViewById(R.id.serverText);

        // Obtain API Authentication Token from LoginActivity's shared preferences
        SharedPreferences sPref = getSharedPreferences("LoginActivity", MODE_PRIVATE);
        final String apiToken = sPref.getString("apiToken", null);

        protected void onPreExecute() {
            //start progress dialog message
            Dialog.setMessage("Please Wait...");
            Dialog.show();

//            try {
//                //set request parameter
//                data += "&" + URLEncoder.encode("data", "UTF-8") + "=" + serverText.getText();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
        }

        protected Void doInBackground(String... urls) {
            try {
                Map<String, String> props = new HashMap<>();
                props.put("x-access-token", apiToken);

                Content = HttpUtils.doGet(urls[0], props);
            } catch (IOException iexc) {
                iexc.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            //close progress dialog
            Dialog.dismiss();
            //Intent intent = new Intent(MainActivity.this, FieldActivity.class);
            // startActivity(intent);
            //finish();
//
//            if (Error != null) {
//                uiUpdate.setText("Output : " + Error);
//            } else {
//                uiUpdate.setText(Content);

            //Receive JSON response
            String OutputData;

            System.out.println("#---- IN onPostExecute ----#");
            System.out.println(Content);

            ArrayList<String> teamList = new ArrayList<>();

            try {
                JSONObject jObj = new JSONObject(Content);
                System.out.println("##########");
//                    System.out.println(jObj.getJSONArray("users").getJSONObject(0).getString("firstname"));

//                    OutputData = "Output captured: " + jObj.getJSONArray("users").getJSONObject(0).getString("firstname");

                OutputData = jObj.toString();
                //show output on screen
                //jsonParsed.setText(OutputData);
                //System.out.println(OutputData);
                JSONArray teamsArray = jObj.getJSONArray("teams");
                for (int i = 0; i < teamsArray.length(); ++i) {
                    JSONObject item = teamsArray.getJSONObject(i);
//                    System.out.println("PLAYER_FIRST: " + item.getString("firstname"));
//                    System.out.println("PLAYER_LAST: " + item.getString("lastname"));
//                    System.out.println("----------");
                    teamList.add(item.getString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(teamList.size());
            listAdapter = new ArrayAdapter<>(TeamActivity.this, R.layout.listrow, teamList);
            teamListView.setAdapter(listAdapter);

//            }
        }
    }
}
