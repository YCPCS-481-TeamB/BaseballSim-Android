package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kylemeyers22.heroku.utils.Endpoints;
import kylemeyers22.heroku.utils.HttpUtils;

public class FieldActivity extends AppCompatActivity {
    private ListView fieldListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field);
        fieldListView = (ListView) findViewById(R.id.fieldsList);

        final Button getFieldButton = (Button) findViewById(R.id.getFieldButton);

        getFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FieldActivity.LongOperation().execute(Endpoints.fieldsAPI);
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private String Content;
        private ProgressDialog Dialog = new ProgressDialog(FieldActivity.this);
        private ArrayAdapter<String> listAdapter;

        // Obtain API Authentication Token from LoginActivity's shared preferences
        SharedPreferences sPref = getSharedPreferences("LoginActivity", MODE_PRIVATE);
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
            Dialog.dismiss();

            ArrayList<String> fieldList = new ArrayList<>();

            try {
                JSONObject jObj = new JSONObject(Content);
                JSONArray fieldsArray = jObj.getJSONArray("fields");
                for (int i = 0; i < fieldsArray.length(); ++i) {
                    JSONObject item = fieldsArray.getJSONObject(i);
                    fieldList.add(item.getString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(fieldList.size());
            listAdapter = new ArrayAdapter<>(FieldActivity.this, R.layout.listrow, fieldList);
            fieldListView.setAdapter(listAdapter);
        }
    }
}