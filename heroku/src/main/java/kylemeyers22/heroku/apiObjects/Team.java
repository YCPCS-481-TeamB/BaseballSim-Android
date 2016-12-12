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

    public String getName() {
        return this.teamName;
    }

    @Override
    public String toString() {
        return this.teamName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        else if ((other == null) || (other.getClass() != this.getClass())) {
            return false;
        }

        Team compareTeam = (Team) other;
        return (this.getName().equals(compareTeam.getName()) &&
                this.getTeamID() == compareTeam.getTeamID());
    }
}
