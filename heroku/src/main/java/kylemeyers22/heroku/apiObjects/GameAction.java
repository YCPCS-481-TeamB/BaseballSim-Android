package kylemeyers22.heroku.apiObjects;

public class GameAction {
    private int game_id;
    private int team_at_bat;
    private int team1_score;
    private int team2_score;
    private int balls;
    private int strikes;
    private int outs;
    private int inning;
    private String type;
    private String message;
    private String date_created;

    public GameAction( int game_id, int team_at_bat, int team1_score, int team2_score,
    int balls, int strikes, int outs, int inning, String type, String message, String date_created)
    {
        this.game_id = game_id;
        this.team_at_bat = team_at_bat;
        this.team1_score = team1_score;
        this.team2_score = team2_score;
        this.balls = balls;
        this.strikes = strikes;
        this.outs = outs;
        this.inning = inning;
        this.type = type;
        this.message = message;
        this.date_created = date_created;
    }
}
