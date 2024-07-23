package com.example.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Book;
import models.User;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class LibraryController {

    private User currentUser;
    private ObservableList<Book> bookItems = FXCollections.observableArrayList();

    @FXML
    private ListView<Book> booksListView;

    @FXML
    private Button deleteButton;

    private Book currentBook;

    public void setUser(User user) {
        this.currentUser = user;
        configureButtons();
    }

    private void configureButtons() {
        deleteButton.setDisable(!currentUser.isAdmin());
    }

    @FXML
    private void initialize() {
        booksListView.setItems(bookItems);
    }

    @FXML
    private void handleAddBookAction() {
        showBookDialog(null, "Add Book", false, this);
    }

    @FXML
    private void handleEditBookAction() {
        currentBook = booksListView.getSelectionModel().getSelectedItem();
        if (currentBook == null) {
            showAlert("No book selected", "Please select a book to modify.", Alert.AlertType.WARNING);
            return;
        }
        showBookDialog(currentBook, "Edit Book", true, this);
    }

    private void showBookDialog(Book book, String title, boolean isEditing, LibraryController libraryController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/book-form.fxml"));
            Parent root = loader.load();

            BookFormController controller = loader.getController();
            controller.initializeForm(book, isEditing);
            controller.setLibraryController(libraryController);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 350, 350));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (!isEditing && book != null) {
                bookItems.add(book);
            } else {
                booksListView.refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteBookAction() {
        Book book = booksListView.getSelectionModel().getSelectedItem();
        if (book == null) {
            showAlert("No book selected", "Please select a book to delete.", AlertType.WARNING);
            return;
        }
        if (currentUser.isAdmin()) {
            Stage deleteBookStage = new Stage();
            VBox deleteBookLayout = new VBox(10);
            Label warningLabel = new Label("Are you sure you want to delete this book?");
            Button yesButton = new Button("Yes");
            Button noButton = new Button("No");

            yesButton.setOnAction(e -> {
                booksListView.getItems().remove(book);
                MongoDBUtil.deleteBook(book);
                deleteBookStage.close();
            });

            noButton.setOnAction(e -> deleteBookStage.close());

            deleteBookLayout.getChildren().addAll(warningLabel, yesButton, noButton);
            Scene deleteBookScene = new Scene(deleteBookLayout, 350, 350);
            deleteBookStage.setTitle("Delete Book");
            deleteBookStage.setScene(deleteBookScene);
            deleteBookStage.initModality(Modality.APPLICATION_MODAL);
            deleteBookStage.showAndWait();
        } else {
            showAlert("Access Denied", "You do not have permission to delete books.", AlertType.ERROR);
        }
    }

    @FXML
    private void handleGetAllBooksAction() {
        bookItems.clear();
        bookItems.addAll(MongoDBUtil.getAllBooks());
    }

    static void showAlert(String title, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void refreshBookList(Book updatedBook) {
        bookItems.removeIf(book -> book.getId().equals(updatedBook.getId()));
        bookItems.add(updatedBook);
    }
}
