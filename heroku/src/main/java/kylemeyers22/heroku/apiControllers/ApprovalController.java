package kylemeyers22.heroku.apiControllers;

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

    private String userForID(int userId) throws IOException, JSONException {
        String userList = HttpUtils.doGet(Endpoints.usersAPI, null);
        JSONObject userJson = new JSONObject(userList);
        JSONArray userArray = userJson.getJSONArray("users");

        // Return user that matches the given ID
        for (int i = 0; i < userArray.length(); ++i) {
            JSONObject currentUser = userArray.getJSONObject(i);
            if (currentUser.getInt("id") == userId) {
                return currentUser.getString("username");
            }
        }

        // No user found (should never happen)
        return null;
    }

    private Approval approvalFromContent(String approveContent) throws IOException, JSONException {
        JSONObject approveJson = new JSONObject(approveContent);

        return new Approval(approveJson.getInt("id"), approveJson.getString("approved"),
                            userForID(approveJson.getInt("approver_user_id")),
                            approveJson.getString("date"));
    }

    public void approveRequest(Approval toApprove) throws IOException {
        Map<String, String> requestParams = initMap();
        String approveBody = "status=approved";

        if (!toApprove.getStatus().equals("approved")) {
            HttpUtils.doPost(Endpoints.approvalSetStatusAPI(toApprove.getId()),
                                                            requestParams,
                                                            approveBody);
        }
    }
}
