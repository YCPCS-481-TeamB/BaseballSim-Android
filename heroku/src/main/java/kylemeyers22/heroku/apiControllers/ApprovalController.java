package kylemeyers22.heroku.apiControllers;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.apiObjects.Approval;
import kylemeyers22.heroku.utils.Endpoints;
import kylemeyers22.heroku.utils.HttpUtils;

public class ApprovalController {

    private String token;

    public ApprovalController(String token) {
        this.token = token;
    }

    private Map<String, String> initMap() {
        Map<String, String> initParams = new HashMap<>();
        initParams.put("x-access-token", token);

        return initParams;
    }

    private Approval approvalFromContent(String approveContent) throws IOException, JSONException {
        JSONObject approveJson = new JSONObject(approveContent);

        return new Approval(approveJson.getInt("id"), approveJson.getString("approved"),
                            approveJson.getString("date"),
                            approveJson.getInt("approver_user_id"));
    }

    public void approveRequest(Approval toApprove) throws IOException {
        new ApproveRequest().execute(toApprove);
    }

    private class ApproveRequest extends AsyncTask<Approval, Void, Void> {
        private Map<String, String> requestParams = initMap();
        private String approveBody = "status=approved";

        protected Void doInBackground(Approval... approvals) {
            Approval toApprove = approvals[0];

            try {
                if (!toApprove.getStatus().equals("approved")) {
                    HttpUtils.doPost(Endpoints.approvalSetStatusAPI(
                            toApprove.getId()),
                            requestParams,
                            approveBody);
                }
            } catch (IOException iexc) {
                iexc.printStackTrace();
            }

            return null;
        }
    }
}
