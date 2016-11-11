package kylemeyers22.heroku.apiObjects;

import java.io.Serializable;

public class Team implements Serializable {
    private int teamID;
    private String teamName;
    // private int leagueID

    public Team(int id, String name) {
        this.teamID = id;
        this.teamName = name;
    }

    public int getTeamID() {
        return this.teamID;
    }

    public String toString() {
        return this.teamName;
    }
}
