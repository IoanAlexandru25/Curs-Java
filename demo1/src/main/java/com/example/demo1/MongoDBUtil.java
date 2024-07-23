package com.example.demo1;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.Book;
import models.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class MongoDBUtil {
    private static MongoClient mongoClient = new MongoClient("localhost", 27017);
    private static MongoDatabase database = mongoClient.getDatabase("BookManagement");
    private static MongoCollection<Document> collection = database.getCollection("Users");
    private static MongoCollection<Document> booksCollection = database.getCollection("Books");

    public static User authenticate(String username, String password) {
        Document userDoc = collection.find(new Document("username", username)).first();
        if (userDoc != null) {
            String hashedPassword = userDoc.getString("password");
            if (BCrypt.checkpw(password, hashedPassword)) {
                boolean admin = userDoc.getBoolean("admin", false);
                return new User(username, password, admin);
            }
        }
        return null;
    }
    public static void addBook(Book book) {
        Document doc = new Document("title", book.getTitle())
                .append("author", book.getAuthor())
                .append("genre", book.getGenre())
                .append("publicationYear", book.getPublicationYear());
        booksCollection.insertOne(doc);
    }

    public static void deleteBook(Book book) {
        Bson query = eq("title",book.getTitle());
        booksCollection.deleteOne(query);
    }

    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        FindIterable<Document> docs = booksCollection.find();
        for (Document doc : docs) {
            Book book = new Book();
            book.setId(doc.getObjectId("_id"));
            book.setTitle(doc.getString("title"));
            book.setAuthor(doc.getString("author"));
            book.setGenre(doc.getString("genre"));
            book.setPublicationYear(doc.getInteger("publicationYear"));
            books.add(book);
        }
        return books;
    }

    public static void updateBook(Book book) {
        Document doc = new Document("title", book.getTitle())
                .append("author", book.getAuthor())
                .append("genre", book.getGenre())
                .append("publicationYear", book.getPublicationYear());
        booksCollection.updateOne(eq("_id", book.getId()), new Document("$set", doc));
    }

}
