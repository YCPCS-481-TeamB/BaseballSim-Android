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
    public static final String fieldsAPI = rootAPI + "fields/";
    public static final String leaguesAPI = rootAPI + "leagues/";
    public static final String schedulesAPI = rootAPI + "schedules/";

    /**
     * Game API Endpoints
     */
    public static final String gamesAPI = rootAPI + "games/";
    // Get game approval status
    public static String gameIsApprovedAPI(int gameID) {
        return gamesAPI + Integer.toString(gameID) + "/approvals/state";
    }
    // Start game
    public static String gameStartAPI(int gameID) {
        return gamesAPI + Integer.toString(gameID) + "/start/";
    }
    // Get all events for given game
    public static String gameEventsAllAPI(int gameID) {
        return gamesAPI + Integer.toString(gameID) + "/events/";
    }
    // Trigger next event for game
    public static String gameNextAPI(int gameID) {
        return gamesAPI + Integer.toString(gameID) + "/events/next/";
    }
    // Get all games for a user
    public static String userGamesAPI(int userID) {
        return usersAPI + Integer.toString(userID) + "/games";
    }

    /**
     * Team API Endpoints
     */
    // Get all teams for a user
    public static String userTeamsAPI(int userID) {
        return usersAPI + Integer.toString(userID) + "/teams";
    }

    /**
     * Approval API Endpoints
     */
    private static final String approvalAPI = rootAPI + "approvals/";
    // Get all pending approvals for currently logged in user
    public static final String userPendingApprovalsAPI = approvalAPI + "user/pending";
    // Set status of an approval
    public static String approvalSetStatusAPI(int approvalID) {
        return approvalAPI + Integer.toString(approvalID) + "/status";
    }
}
