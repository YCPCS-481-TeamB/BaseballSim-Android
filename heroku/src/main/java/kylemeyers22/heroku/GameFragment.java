package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
public class GameFragment extends AppCompatActivity{
    private ListView gameListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        gameListView = (ListView) findViewById(R.id.gamesList);

        final Button getGameButton = (Button) findViewById(R.id.getGameButton);

        getGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //webserver request url
                String serverUrl = "https://baseballsim.herokuapp.com/api/games";

                //use AsyncTask execute method to prevent ANR problem
                new GameFragment.LongOperation().execute(serverUrl);
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(GameFragment.this);
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
            //Intent intent = new Intent(PlayerFragment.this, FieldActivity.class);
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

            ArrayList<String> gameList = new ArrayList<>();

            try {
                JSONObject jObj = new JSONObject(Content);
                System.out.println("##########");
//                    System.out.println(jObj.getJSONArray("users").getJSONObject(0).getString("firstname"));

//                    OutputData = "Output captured: " + jObj.getJSONArray("users").getJSONObject(0).getString("firstname");

                OutputData = jObj.toString();
                //show output on screen
                //jsonParsed.setText(OutputData);
                //System.out.println(OutputData);
                JSONArray gamesArray = jObj.getJSONArray("games");
                for (int i = 0; i < gamesArray.length(); ++i) {
                    JSONObject item = gamesArray.getJSONObject(i);
//                    System.out.println("PLAYER_FIRST: " + item.getString("firstname"));
//                    System.out.println("PLAYER_LAST: " + item.getString("lastname"));
//                    System.out.println("----------");

                    //the games take two ints for teams and shows them. Need to find a way to get the ints of teams and show them as strings. I'm not the get at JSON
                    //gameList.add(item.getInt("team1_id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(gameList.size());
            listAdapter = new ArrayAdapter<>(GameFragment.this, R.layout.listrow, gameList);
            gameListView.setAdapter(listAdapter);

//            }
        }
    }
}
