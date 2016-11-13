package kylemeyers22.heroku.apiObjects;


public class GamePosition {
    private int game_id;
    private int onFirst;
    private int onSecond;
    private int onThird;
    private String date_created;

    public GamePosition (int game_id, int onFirst, int onSecond, int onThird, String date_created)
    {
        this.game_id = game_id;
        this.onFirst = onFirst;
        this.onSecond = onSecond;
        this.onThird = onThird;
        this.date_created = date_created;
    }
}
