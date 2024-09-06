package utils;


import models.Book;

import java.util.List;

public class BookService {
    private MongoDBUtil mongoDBService;

    public BookService() {
        mongoDBService = new MongoDBUtil();
    }

    public void addBook(Book book) {
        mongoDBService.addBook(book);
    }

    public List<Book> getAllBooks() {
        return mongoDBService.getAllBooks();
    }

    public void updateBook(Book book) {
        mongoDBService.updateBook(book);
    }

    public void deleteBook(Book book) {
        mongoDBService.deleteBook(book);
    }
}
