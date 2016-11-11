package kylemeyers22.heroku;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.apiObjects.Team;
import kylemeyers22.heroku.utils.HttpUtils;

public class GameFragment extends Fragment {
    private ListView gameListView;
    private ArrayList<Team> teamObjs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gameListView = (ListView) getView().findViewById(R.id.gamesList);

        final Button getGameButton = (Button) getView().findViewById(R.id.getGameButton);
        // webserver request url
        String serverUrl = "https://baseballsim.herokuapp.com/api/games";

        // Fetch current history of games
        new GameFragment.LongOperation().execute(serverUrl);

        // Initiate new game
        getGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStartGameForm();
            }
        });
    }

    public void storeTeams(ArrayList<Team> teamList) {
        teamObjs = teamList;
    }

    private void createStartGameForm() {
        Dialog startGame = new Dialog(this.getContext());
        startGame.setContentView(R.layout.start_game);
        startGame.setCancelable(true);

        Button gameStart = (Button) startGame.findViewById(R.id.startGameButton);
        Spinner teamOneSpin = (Spinner) startGame.findViewById(R.id.team_one);
        Spinner teamTwoSpin = (Spinner) startGame.findViewById(R.id.team_two);
        ArrayAdapter adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, teamObjs);

        teamOneSpin.setAdapter(adapter);
        teamTwoSpin.setAdapter(adapter);

        gameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Click stuff
            }
        });

    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(getActivity());
        private ArrayAdapter<String> listAdapter;

        String data = "";
//        TextView uiUpdate = (TextView) findViewById(R.id.playerFirstName);
//        TextView jsonParsed = (TextView) findViewById(R.id.playerLastName);
//        TextView serverText = (TextView) findViewById(R.id.serverText);

        // Obtain API Authentication Token from LoginActivity's shared preferences
        SharedPreferences sPref = getActivity().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
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
            listAdapter = new ArrayAdapter<>(getActivity(), R.layout.listrow, gameList);
            gameListView.setAdapter(listAdapter);

//            }
        }
    }
}
