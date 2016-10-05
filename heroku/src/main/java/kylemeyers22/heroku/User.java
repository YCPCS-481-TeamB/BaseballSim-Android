package kylemeyers22.heroku;

/**
 * Created by Ben Coover on 10/4/2016.
 */

public class User {
    String authToken;
    String username;

    public User(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }
}
