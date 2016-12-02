package kylemeyers22.heroku.apiObjects;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import kylemeyers22.heroku.utils.Endpoints;
import kylemeyers22.heroku.utils.HttpUtils;

public class Approval {
    private int approvalId;
    private String status;
    private String date;
    private String approvalOwner;

    public Approval(int Id, String status, String date, int ownerID) {
        this.approvalId = Id;
        this.status = status;
        this.date = date;
        // Set owner by using the given ID
        //userForID(ownerID);
        //new UserForID().execute(ownerID);
    }

    public int getId() {
        return this.approvalId;
    }

    public String getStatus() {
        return this.status;
    }

    public String getDate() {
        return this.date;
    }

    public String getOwner() {
        return this.approvalOwner;
    }

    private void setOwner(String owner) {
        this.approvalOwner = owner;
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    private void userForID(final int userID) {

        new Thread(new Runnable() {
            String responseContent;

            @Override
            public void run() {
                try {
                    System.out.println("In userForID: Id is - " + userID);
                    responseContent = HttpUtils.doGet(Endpoints.usersAPI, new HashMap<String, String>());
                } catch (IOException iexc) {
                    iexc.printStackTrace();
                }

                try {
                    JSONObject jsonContent = new JSONObject(responseContent);
                    JSONArray userArray = jsonContent.getJSONArray("users");
                    for (int i = 0; i < userArray.length(); ++i) {
                        JSONObject current = userArray.getJSONObject(i);
                        if (current.getInt("id") == userID) {
                            setOwner(current.getString("username"));
                        }
                    }
                } catch (JSONException jexc) {
                    jexc.printStackTrace();
                }
            }
        }).start();
    }

    private class UserForID extends AsyncTask<Integer, Void, Void> {
        private String responseContent;

        protected Void doInBackground(Integer... data) {
            try {
                responseContent = HttpUtils.doGet(Endpoints.usersAPI, new HashMap<String, String>());
            } catch (IOException iexc) {
                iexc.printStackTrace();
            }

            try {
                JSONObject jsonContent = new JSONObject(responseContent);
                JSONArray userArray = jsonContent.getJSONArray("users");
                for (int i = 0; i < userArray.length(); ++i) {
                    JSONObject current = userArray.getJSONObject(i);
                    if (current.getInt("id") == data[0]) {
                        setOwner(current.getString("username"));
                    }
                }
            } catch (JSONException jexc) {
                jexc.printStackTrace();
            }

            return null;
        }
    }
}
