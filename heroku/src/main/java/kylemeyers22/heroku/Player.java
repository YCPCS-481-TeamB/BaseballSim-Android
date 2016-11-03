package kylemeyers22.heroku;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shdw2 on 10/2/2016.
 */
public class Player {

    @SerializedName("id")
    int id;

    @SerializedName("firstname")
    String firstname;

    @SerializedName("lastname")
    String lastname;

    @SerializedName("player_position")
    String position;

    @SerializedName("team_id")
    int team_id;


    public Player(int id, String firstname, String lastname, String position, int team_id)
    {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.position = position;
        this.team_id = team_id;
    }
}
