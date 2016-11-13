package kylemeyers22.heroku.apiObjects;


public class Game {
    private int team1_id;
    private int team2_id;
    private int field_id;
    private int league_id;
    private String date_created;

    public Game(int team1_id, int team2_id, int field_id, int league_id, String date_created)
    {
        this.team1_id = team1_id;
        this.team2_id = team2_id;
        this.field_id = field_id;
        this.league_id = league_id;
        this.date_created = date_created;
    }

    public int getTeamOne() { return this.team1_id;}

    public int getTeamTwo() { return this.team2_id; }

    public int getFieldId() { return this.field_id; }

    public int getLeagueId() { return this.league_id; }

    public String getDateCreated() { return this.date_created; }

}
