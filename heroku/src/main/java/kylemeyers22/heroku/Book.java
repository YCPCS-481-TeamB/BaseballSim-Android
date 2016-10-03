package kylemeyers22.heroku;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shdw2 on 10/1/2016.
 */
public class Book {

    @SerializedName("id")
    int id;

    @SerializedName("isbn")
    String isbn;

    public Book(int id, String isbn) {
        this.id = id;
        this.isbn = isbn;
    }

    public Book(String isbn) {
        this.isbn = isbn;
    }
}
