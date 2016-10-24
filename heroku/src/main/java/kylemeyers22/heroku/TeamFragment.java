package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class TeamFragment extends Fragment {
    private ListView teamListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.team_fragment, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        teamListView = (ListView) getView().findViewById(R.id.teamsList);

        final Button getTeamButton = (Button) getView().findViewById(R.id.getTeamButton);

        getTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //webserver request url
                String serverUrl = "https://baseballsim.herokuapp.com/api/teams";

                //use AsyncTask execute method to prevent ANR problem
                new TeamFragment.LongOperation().execute(serverUrl);
            }
        });
    }

    public class LongOperation extends AsyncTask<String, Void, Void> {

        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(getActivity());
        private ArrayAdapter<String> listAdapter;


        // Obtain API Authentication Token from LoginActivity's shared preferences
        SharedPreferences sPref = getActivity().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        final String apiToken = sPref.getString("apiToken", null);

        protected void onPreExecute() {
            //start progress dialog message
            Dialog.setMessage("Please Wait...");
            Dialog.show();
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

            //Receive JSON response
            System.out.println("#---- IN onPostExecute ----#");
            System.out.println(Content);

            ArrayList<String> teamList = new ArrayList<>();

            try {
                JSONObject jObj = new JSONObject(Content);
                JSONArray teamsArray = jObj.getJSONArray("teams");
                for (int i = 0; i < teamsArray.length(); ++i) {
                    JSONObject item = teamsArray.getJSONObject(i);
                    teamList.add(item.getString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(teamList.size());
            listAdapter = new ArrayAdapter<>(getActivity(), R.layout.listrow, teamList);
            teamListView.setAdapter(listAdapter);
        }
    }
}