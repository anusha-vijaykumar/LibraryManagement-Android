package com.example.anusha.library;

/**
 * Created by anusha on 7/18/2016.
 */

// contains all details needed to be displayed in list view to the user
public class Modelitem {

    String Booktitle;
    String Bookimage;
    String Author;
    String genre;

    public Modelitem(String Booktitle, String Bookimage, String Author, String genre) {
        this.Bookimage = Bookimage;
        this.genre = genre;
        this.Booktitle = Booktitle;
        this.Author = Author;
    }

    public Modelitem() {
    }

    public String getBooktitle() {
        return Booktitle;
    }

    public void setBooktitle(String booktitle) {
        Booktitle = booktitle;
    }

    public String getBookimage() {
        return Bookimage;
    }

    public void setBookimage(String bookimage) {
        Bookimage = bookimage;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
