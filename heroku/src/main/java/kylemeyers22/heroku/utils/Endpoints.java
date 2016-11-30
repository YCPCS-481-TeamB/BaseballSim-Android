package kylemeyers22.heroku.utils;

public class Endpoints {
    // API locations
    //private static final String rootAPI = "https://baseballsim.herokuapp.com/api/";
    //private static final String rootAPI = "localhost:3000/console";
    private static final String rootAPI = "https://baseballsim-koopaluigi.c9users.io/api/";
    public static final String usersAPI = rootAPI + "users/";
    public static final String authAPI = usersAPI + "token/";
    public static final String playersAPI = rootAPI + "players/";
    public static final String teamsAPI = rootAPI + "teams/";
    public static final String gamesAPI = rootAPI + "games/";
    public static final String approvalAPI = rootAPI + "approvals/";

    public static String gameStartAPI(int gameID) {
        return gamesAPI + Integer.toString(gameID) + "/start/";
    }

    public static String gameNextAPI(int gameID) {
        return gamesAPI + Integer.toString(gameID) + "/events/next/";
    }

    public static String userTeamsAPI(int userID) {
        return usersAPI + Integer.toString(userID) + "/teams";
    }

    public static String approvalSetStatusAPI(int approvalID) {
        return approvalAPI + Integer.toString(approvalID) + "/status";
    }
}
