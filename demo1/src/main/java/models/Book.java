package models;

import org.bson.types.ObjectId;

public class Book {
    private ObjectId id;
    private String title;
    private String author;
    private String genre;
    private int publicationYear;

    public Book() {}

    public Book(String title, String author, String genre, int publicationYear) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Title: ").append(title);
        sb.append(", Author: ").append(author);
        sb.append(", Genre: ").append(genre);
        sb.append(", Publication Year: ").append(publicationYear);
        return sb.toString();
    }
}
