package kylemeyers22.heroku;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class CreateNewGameActivity extends AppCompatActivity{
    private int teamOneId;
    private String teamOneName;

    private int teamTwoId;
    private String teamTwoName;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("create the new game");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);


        System.out.println(teamOneId +" whoo " + teamOneName);
    }

}
