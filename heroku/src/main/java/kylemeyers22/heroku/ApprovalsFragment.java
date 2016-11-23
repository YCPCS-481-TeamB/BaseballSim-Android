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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.adapters.ApprovalListItemAdapter;
import kylemeyers22.heroku.utils.HttpUtils;

public class ApprovalsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.approvals_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisible) {
        super.setUserVisibleHint(isVisible);

        if (this.isVisible()) {
            // Call check for new approvals
        }
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private String Content;
        private ProgressDialog Dialog = new ProgressDialog(getActivity());
        private ApprovalListItemAdapter approvalAdapter;

        private SharedPreferences sPref = getActivity().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        final private String apiToken = sPref.getString("apiToken", null);

        protected void onPreExecute() {
            Dialog.setMessage("Refreshing...");
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
            
        }
    }
}
