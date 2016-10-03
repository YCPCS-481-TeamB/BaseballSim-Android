package kylemeyers22.heroku;

import android.app.ProgressDialog;
import android.icu.text.IDNA;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        /*used for herokutestandroid
        final Button button = (Button) findViewById(R.id.button);
        final TextView textView = (TextView) findViewById(R.id.textView);*/

        //used with postgresexample
        final TextView playerFirstLabel = (TextView) findViewById(R.id.playerFirstNameLabel);
        final Button getPlayerButton = (Button) findViewById(R.id.getPlayerButton);

        getPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //webserver request url
                String serverUrl = "http://baseballsim.herokuapp.com/api/players";

                //use AsyncTask execute method to prevent ANR problem
                new LongOperation().execute(serverUrl);
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, Void>
    {
        //private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        String data = "";
        TextView uiUpdate = (TextView) findViewById(R.id.playerFirstName);
        TextView jsonParsed = (TextView) findViewById(R.id.playerLastName);
        int sizeData = 0;
        EditText serverText = (EditText) findViewById(R.id.serverText);

        protected void onPreExecute()
        {
            //start progress dialog message
            Dialog.setMessage("Please Wait....");
            Dialog.show();

            try
            {
                //set request parameter
                data +="&" + URLEncoder.encode("data", "UTF-8")+ "="+serverText.getText();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }

        protected Void doInBackground(String... urls)
        {
            //make post call to server
            BufferedReader reader = null;

            //send data
            try
            {
                //defined url where to send data
                URL url = new URL(urls[0]);

                //send post data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                //get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                //read servedr response
                while((line=reader.readLine()) != null)
                {
                    //append server response n string
                    sb.append(line + "");
                }

                //append server response to content string
                Content = sb.toString();
            }
            catch(Exception ex)
            {
               // Error = ex.getMessage();
            }
            finally
            {
                try
                {
                    reader.close();
                }
                catch (Exception ex){}
            }
            return null;
    }

        protected void onPostExecute(Void unused) {
            //close progress dialog
            Dialog.dismiss();

            if (Error != null) {
                uiUpdate.setText("Output : " + Error);
            } else {
                uiUpdate.setText(Content);

                //start parse response json

                String OutputData = "";
                JSONObject jsonResponse;

                try
                {
                    jsonResponse = new JSONObject(Content);

                    JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

                    int lengthJsonArr = jsonMainNode.length();
                    for(int i=0; i<lengthJsonArr; i++)
                    {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        //fetch node values
                        String firstname = jsonChildNode.optString("firstname").toString();
                        String number = jsonChildNode.optString("lastname").toString();
                        String date_added = jsonChildNode.optString("date_created").toString();

                        OutputData +=" Name         : " +firstname+"" +
                                "Number         :" +number+""
                                + "Time                     : "+date_added+""
                                +"========================================";
                    }

                    //show output on screen
                    jsonParsed.setText(OutputData);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
        //gets the backend from heroku
        //use herokutestandroid for the url to test backend initially
        //use postgresexample for an example of postgres
        //use baseballsim for our baseball sim project
       /* Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://baseballsim.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/


    //createse click listener for the button
       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book(isbnInput.getText().toString());
                Call<Book> createCall = service.create(book);
                createCall.enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> _, Response<Book> resp) {
                        Book newBook = resp.body();
                        textView.setText("Created Book with ISBN: " + newBook.isbn);
                    }

                    @Override
                    public void onFailure(Call<Book> _, Throwable t) {
                        t.printStackTrace();
                        textView.setText(t.getMessage());
                    }
                });
            }
        });*/


}
