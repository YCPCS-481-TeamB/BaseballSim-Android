package kylemeyers22.heroku;

import android.app.Activity;
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
import kylemeyers22.heroku.utils.HttpUtils;

public class TeamFragment extends Fragment {
    private ListView teamListView;

    TeamsUpdated teamCallback;
    public interface TeamsUpdated{
        void sendTeams(ArrayList<Team> teamList);
    }

    @Override
    public void onAttach(Context thisContext) {
        super.onAttach(thisContext);

        try {
            teamCallback = (TeamsUpdated) thisContext;
        } catch (ClassCastException exc) {
            throw new ClassCastException(thisContext.toString() + " must implement TeamsUpdated");
        }
    }

    @Override
    public void onDetach() {
        teamCallback = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.team_fragment, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        teamListView = (ListView) getView().findViewById(R.id.teamsList);

        final Button getTeamButton = (Button) getView().findViewById(R.id.getTeamButton);

        //webserver request url
        final String serverUrl = "https://baseballsim.herokuapp.com/api/teams";
        new TeamFragment.LongOperation().execute(serverUrl);

        getTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            ArrayList<Team> teamObjs = new ArrayList<>();

            try {
                JSONObject jObj = new JSONObject(Content);
                JSONArray teamsArray = jObj.getJSONArray("teams");
                for (int i = 0; i < teamsArray.length(); ++i) {
                    JSONObject item = teamsArray.getJSONObject(i);
                    teamList.add(item.getString("name"));
                    teamObjs.add(new Team(item.getInt("id"), item.getString("name")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Store list of teams in GameFragment's arg list for use
            System.out.println("STORING BUNDLE ARGUMENTS");
//            Fragment gameFrag = new GameFragment();
//            Bundle teamBundle = new Bundle();
//            teamBundle.putSerializable("teamsObjs", teamObjs);
//            gameFrag.setArguments(teamBundle);
            teamCallback.sendTeams(teamObjs);

            //System.out.println(teamList.size());
            listAdapter = new ArrayAdapter<>(getActivity(), R.layout.listrow, teamList);
            teamListView.setAdapter(listAdapter);
        }
    }
}
