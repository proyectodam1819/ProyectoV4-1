package org.izv.aad.proyecto.Interfaces;

import com.google.firebase.auth.FirebaseUser;

import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;

import java.util.List;

public interface InterfaceFireBase {

    void isCorrectlyLogUp(boolean isSuccessful, String error);

    Book getBook(Book book);

    Author getAuthor(Author author);

    void getUserLogin(FirebaseUser user, String error);

    List<Book> getAllBooks(List<Book> books);

    List<Author> getAllAuthors(List<Author> authors);

    String sendRoutePhoto(String string);

    String getRoutePhoto(String string);
}
