package kylemeyers22.heroku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BackendHeroku extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend_heroku);

        final Button button = (Button) findViewById(R.id.button);
        final TextView textView = (TextView) findViewById(R.id.textView);


        //gets the backend from heroku
        //use herokutestandroid for the url to test backend initially
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://herokutestandroid.herokuapp.com/")
                .build();

        final HerokuService service = retrofit.create(HerokuService.class);

    //createse click listener for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> call = service.hello();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> _,
                                           Response<ResponseBody> response) {
                        try {
                            textView.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                            textView.setText(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> _, Throwable t) {
                        t.printStackTrace();
                        textView.setText(t.getMessage());
                    }
                });
            }
        });
    }
}
