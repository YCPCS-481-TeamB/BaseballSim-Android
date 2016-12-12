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

import kylemeyers22.heroku.apiObjects.Team;
import kylemeyers22.heroku.utils.Endpoints;
import kylemeyers22.heroku.utils.HttpUtils;

public class TeamFragment extends Fragment {
    private ListView teamListView;

    private String apiToken;
    private int userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.team_fragment, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        teamListView = (ListView) getView().findViewById(R.id.teamsList);

        final Button getTeamButton = (Button) getView().findViewById(R.id.getTeamButton);

        // Obtain API Authentication Token from LoginActivity's shared preferences
        SharedPreferences sPref = getActivity().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        apiToken = sPref.getString("apiToken", null);
        userID = sPref.getInt("currentUser", -1);

        // Populate homeTeamList immediately on Activity creation
        new TeamFragment.LongOperation().execute(
                Endpoints.userTeamsAPI(userID),
                Endpoints.teamsAPI
        );

        getTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh list of teams
                new TeamFragment.LongOperation().execute(Endpoints.userTeamsAPI(userID));
            }
        });
    }

    public class LongOperation extends AsyncTask<String, Void, Void> {

        private String userTeams;
        private String allTeams;
        private ProgressDialog Dialog = new ProgressDialog(getActivity());
        private ArrayAdapter<String> listAdapter;

        protected void onPreExecute() {
            //start progress dialog message
            Dialog.setMessage("Please Wait...");
            Dialog.show();
        }

        protected Void doInBackground(String... urls) {
            try {
                Map<String, String> props = new HashMap<>();
                props.put("x-access-token", apiToken);

                userTeams = HttpUtils.doGet(urls[0], props);
                allTeams = HttpUtils.doGet(urls[1], props);
            } catch (IOException iexc) {
                iexc.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            //close progress dialog
            Dialog.dismiss();

            ArrayList<String> homeTeams = new ArrayList<>();
            ArrayList<Team> homeObjs = new ArrayList<>();
            ArrayList<Team> allObjs = new ArrayList<>();
            ArrayList<Team> opposeObjs = new ArrayList<>();

            try {
                // Parse current user's teams
                JSONObject jObj = new JSONObject(userTeams);
                JSONArray teamsArray = jObj.getJSONArray("teams");
                for (int i = 0; i < teamsArray.length(); ++i) {
                    JSONObject item = teamsArray.getJSONObject(i);
                    // Save team name strings for displaying in layout
                    homeTeams.add(item.getString("name"));
                    homeObjs.add(new Team(item.getInt("id"), item.getString("name")));
                }
                // Parse all teams
                jObj = new JSONObject(allTeams);
                teamsArray = jObj.getJSONArray("teams");
                for (int i = 0; i < teamsArray.length(); ++i) {
                    JSONObject item = teamsArray.getJSONObject(i);
                    allObjs.add(new Team(item.getInt("id"), item.getString("name")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Filter user teams from all teams
            for (Team current : allObjs) {
                if (!homeObjs.contains(current)) {
                    opposeObjs.add(current);
                }
            }

            // Set list of teams in MainTabbedActivity for use in GameFragment
            MainTabbedActivity.homeTeamList = homeObjs;
            MainTabbedActivity.opposeTeamList = opposeObjs;

            //System.out.println(homeTeamList.size());
            listAdapter = new ArrayAdapter<>(getActivity(), R.layout.listrow, homeTeams);
            teamListView.setAdapter(listAdapter);
        }
    }
}
