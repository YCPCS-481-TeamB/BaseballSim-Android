package kylemeyers22.heroku.utils;

public class Constants {
    // Set to number of tabs on the MainTabbedActivity
    public static final int offScreenLimit = 3;

    // API locations
    public static final String rootAPI = "https://baseballsim.herokuapp.com/api/";
    //public static final String rootAPI = "localhost:3000/console";
    public static final String usersAPI = rootAPI + "users/";
    public static final String authAPI = usersAPI + "token/";
    public static final String playersAPI = rootAPI + "players/";
    public static final String teamsAPI = rootAPI + "teams/";
    public static final String gamesAPI = rootAPI + "games/";
}
