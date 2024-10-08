package utils;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import exceptions.UserAlreadyExistsException;
import models.Book;
import models.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.mindrot.jbcrypt.BCrypt;
import proxy.AbstractAuthenticationService;

import java.util.List;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoDBUtil implements AbstractAuthenticationService {
    private static MongoClient mongoClient = new MongoClient("localhost", 27017);
    private static MongoDatabase database = mongoClient.getDatabase("BookManagement");
    private static MongoCollection<Document> userCollection = database.getCollection("Users");
    private static MongoCollection<Document> booksCollection = database.getCollection("Books");
    private static MongoCollection<Document> devicesCollection = database.getCollection("Devices");

    public static MongoCollection<Document> getDevicesCollection() {
        return devicesCollection;
    }

    public static MongoCollection<Document> getUserCollection() {
        return userCollection;
    }

    @Override
    public User authentication(String username, String password, String deviceId) {
        Document userDoc = userCollection.find(new Document("username", username)).first();
        if (userDoc != null) {
            String hashedPassword = userDoc.getString("password");
            if (BCrypt.checkpw(password, hashedPassword)) {
                boolean admin = userDoc.getBoolean("admin", false);
                return new User(username, password, admin);
            }
        }
        return null;
    }

    public User registration(String username, String password) throws UserAlreadyExistsException {
        if (!username.isEmpty() && !password.isEmpty()) {
            Document existingUser = userCollection.find(new Document("username", username)).first();

            if(existingUser != null) {
                throw new UserAlreadyExistsException();
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            Document userDoc = new Document("username", username)
                    .append("password", hashedPassword)
                    .append("admin", false);
            userCollection.insertOne(userDoc);
            return new User(username, hashedPassword, false);
        }
        return null;
    }

    private Document getDeviceDocument(String username, String deviceId) {
        MongoCollection<Document> userCollection = getUserCollection();
        return userCollection.find(and(eq("username", username), eq("deviceId", deviceId))).first();
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
