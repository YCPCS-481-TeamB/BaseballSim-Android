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
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.adapters.ApprovalListItemAdapter;
import kylemeyers22.heroku.apiObjects.Approval;
import kylemeyers22.heroku.utils.Endpoints;
import kylemeyers22.heroku.utils.HttpUtils;

public class ApprovalsFragment extends Fragment {
    private ListView approvalListView;

    private String apiToken;
    private int userID;

    @Override
    public void setUserVisibleHint(boolean isVisible) {
        super.setUserVisibleHint(isVisible);

        if (this.isVisible()) {
            // Call check for new approvals
//            new ApprovalOperation().execute(Endpoints.userApprovalsAPI(userID));
            new ApprovalOperation().execute(Endpoints.approvalAPI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.approvals_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        approvalListView = (ListView) getView().findViewById(R.id.approvalList);
        SharedPreferences sPref = getActivity().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        apiToken = sPref.getString("apiToken", null);
        userID = sPref.getInt("currentUser", -1);

//        new ApprovalOperation().execute(Endpoints.userApprovalsAPI(userID));
        new ApprovalOperation().execute(Endpoints.approvalAPI);
    }

    private class ApprovalOperation extends AsyncTask<String, Void, Void> {

        private String Content;
        private ProgressDialog Dialog = new ProgressDialog(getActivity());
        private ApprovalListItemAdapter approvalAdapter;

        protected void onPreExecute() {
//            Dialog.setMessage("Refreshing...");
//            Dialog.show();
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
//            Dialog.dismiss();

            ArrayList<Approval> approvalItems = new ArrayList<>();
            System.out.println("In Approval Frag: " + Content);

            try {
                JSONObject jObj = new JSONObject(Content);

                JSONArray approvalArray = jObj.getJSONArray("approvals");
                for (int i = 0; i < approvalArray.length(); ++i) {
                    JSONObject item = approvalArray.getJSONObject(i);
                    approvalItems.add(new Approval(
                            item.getInt("id"),
                            item.getString("approved"),
                            item.getString("date_created"),
                            item.getInt("approver_user_id")
                    ));
                }
            } catch (JSONException jexc) {
                jexc.printStackTrace();
            }

            approvalAdapter = new ApprovalListItemAdapter(
                    getActivity(),
                    R.layout.approvalrow,
                    approvalItems);
            approvalListView.setAdapter(approvalAdapter);
        }
    }
}
