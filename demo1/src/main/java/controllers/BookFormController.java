package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Book;
import utils.MongoDBUtil;


public class BookFormController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField yearField;
    @FXML
    private Button actionButton;

    private Book currentBook;
    private boolean isEditing;

    private LibraryController libraryController;

    public void initializeForm(Book book, boolean isEditing) {
        this.currentBook = book;
        this.isEditing = isEditing;

        if (book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            genreField.setText(book.getGenre());
            yearField.setText(String.valueOf(book.getPublicationYear()));
        }

        actionButton.setText(isEditing ? "Modify" : "Save");
    }

    public void setLibraryController(LibraryController libraryController) {
        this.libraryController = libraryController;
    }

    @FXML
    private void handleBookAction() {
        try {
            String title = titleField.getText();
            String author = authorField.getText();
            String genre = genreField.getText();
            int year = Integer.parseInt(yearField.getText());

            if (currentBook == null) {
                currentBook = new Book(title, author, genre, year);
                MongoDBUtil.addBook(currentBook);
            } else {
                currentBook.setTitle(title);
                currentBook.setAuthor(author);
                currentBook.setGenre(genre);
                currentBook.setPublicationYear(year);
                MongoDBUtil.updateBook(currentBook);
            }

            if (currentBook != null && libraryController != null) {
                libraryController.refreshBookList(currentBook);
            }

            closeWindow();
        } catch (NumberFormatException e) {
            LibraryController.showAlert("Invalid input", "Publication year must be a valid number.", Alert.AlertType.ERROR);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) actionButton.getScene().getWindow();
        stage.close();
    }
}
