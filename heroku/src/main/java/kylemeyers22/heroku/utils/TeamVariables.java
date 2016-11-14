package kylemeyers22.heroku.utils;


public class TeamVariables {
    private int teamOneId;
    private String teamOneName;

    private int teamTwoId;
    private String teamTwoName;

    public void setTeamOneId(int teamOneId)
    {
        this.teamOneId = teamOneId;
    }

    public void setTeamTwoId(int teamTwoId)
    {
        this.teamTwoId= teamTwoId;
    }

    public void setTeamOneName(String teamOneName)
    {
        this.teamOneName = teamOneName;
    }

    public void setTeamTwoName(String teamTwoName)
    {
        this.teamTwoName = teamTwoName;
    }

    public int getTeamOneId ()
    {
        return this.teamOneId;
    }
    public int getTeamTwoId()
    {
        return this.teamTwoId;
    }
    public String getTeamOneName()
    {
        return this.teamOneName;
    }
    public String getTeamTwoName ()
    {
        return this.teamTwoName;
    }
}
