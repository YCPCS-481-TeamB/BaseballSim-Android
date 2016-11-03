package kylemeyers22.heroku;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by shdw2 on 10/2/2016.
 */
public interface PlayerService {
   @GET("players")
    Call<List<Player>> all();

    @GET("players/{id}")
    Call<Player> get(@Path("id") int id);

    //@POST("players/new")
    //Call<Book> create(@Body Book book);
}
