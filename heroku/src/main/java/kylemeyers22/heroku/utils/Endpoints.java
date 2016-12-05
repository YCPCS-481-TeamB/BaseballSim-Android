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
    public static final String fieldsAPI = rootAPI + "fields/";
    public static final String leaguesAPI = rootAPI + "leagues/";
    public static final String schedulesAPI = rootAPI + "schedules/";

    private static final String approvalAPI = rootAPI + "approvals/";
    public static final String userPendingApprovalsAPI = approvalAPI + "user/pending";

    public static String gameStartAPI(int gameID) {
        return gamesAPI + Integer.toString(gameID) + "/start/";
    }

    public static String gameNextAPI(int gameID) {
        return gamesAPI + Integer.toString(gameID) + "/events/next/";
    }

    public static String userTeamsAPI(int userID) {
        return usersAPI + Integer.toString(userID) + "/teams";
    }

    public static String userGamesAPI(int userID) {
        return usersAPI + Integer.toString(userID) + "/games";
    }

    public static String approvalSetStatusAPI(int approvalID) {
        return approvalAPI + Integer.toString(approvalID) + "/status";
    }
}
