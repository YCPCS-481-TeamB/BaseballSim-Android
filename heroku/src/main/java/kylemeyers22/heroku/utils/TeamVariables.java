package kylemeyers22.heroku.utils;


import android.app.Application;

public class TeamVariables extends Application{
    private static int teamOneId;
    private static String teamOneName;

    private static int teamTwoId;
    private static String teamTwoName;

    public static void setTeamOneId(int teamOneIds)
    {
        teamOneId = teamOneIds;
    }

    public static void setTeamTwoId(int teamTwoIds)
    {
        teamTwoId= teamTwoIds;
    }

    public static void setTeamOneName(String teamOneNames)
    {
        teamOneName = teamOneNames;
    }

    public static void setTeamTwoName(String teamTwoNames)
    {
        teamTwoName = teamTwoNames;
    }

    public static int getTeamOneId ()
    {
        return teamOneId;
    }
    public static int getTeamTwoId()
    {
        return teamTwoId;
    }
    public static String getTeamOneName()
    {
        return teamOneName;
    }
    public static String getTeamTwoName ()
    {
        return teamTwoName;
    }
}
