package org.izv.aad.proyecto.FireBase;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.izv.aad.proyecto.Interfaces.InterfaceFireBase;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseCustom {

    private static FirebaseUser user = null;
    private static StorageReference storage = FirebaseStorage.getInstance().getReference();

    private static String getRouteBook(String key){
        if(key == null){
            key = getReference().child("book").push().getKey();
        }
        return getUser().getUid() + "/book/" + key + "/";
    }

    private static String getRouteAuthor(String key){
        if(key == null){
            key = getReference().child("author").push().getKey();
        }
        return getUser().getUid() + "/author/" + key + "/";
    }

    public static FirebaseDatabase getDataBase(){
        return FirebaseDatabase.getInstance();
    }

    public static DatabaseReference getReference(){
        return getDataBase().getReference();
    }

    public static FirebaseAuth getFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    public static FirebaseUser getUser(){
        if(getFirebaseAuth().getCurrentUser() != null) {
            setUser();
        }
        return user;
    }

    private static void setUser(){
        user = getFirebaseAuth().getCurrentUser();
    }

    public static void login(Activity activity, String email, String password, final InterfaceFireBase interfaceFireBase){
        Task<AuthResult> task = getFirebaseAuth().signInWithEmailAndPassword(email,password);
        task.addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setUser();
                Exception error = task.getException();
                String mensajeError = "";
                if(error != null){
                    mensajeError = error.toString();
                }
                interfaceFireBase.getUserLogin(user, mensajeError);
            }
        });
    }

    /*
    * Hace falta instanciar la interfaz InterfaceFireBase en donde se llame al método log up
    * Al instanciarla se hará cargo en el método isCorrectlyLogUp de mostrar el error o
    * el que se haya hecho bien
    * */
    public static void logup(Activity activity, String email, String password, final InterfaceFireBase interfaceFireBase){
        Task<AuthResult> task = getFirebaseAuth().createUserWithEmailAndPassword(email,password);
        task.addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Exception error = task.getException();
                String mensajeError = "";
                if(error != null){
                    mensajeError = error.toString();
                }
                interfaceFireBase.isCorrectlyLogUp(task.isSuccessful(), mensajeError);
            }
        });
    }

    public static Book saveBook(Book book){
        Map<String, Object> saveItem = new HashMap<>();
        String key = getReference().child("book").push().getKey();
        book.setKey(key);
        saveItem.put(getRouteBook(key), book.toMap());
        getReference().updateChildren(saveItem);
        return book;
    }

    public static Author saveAuthor(Author author){
        Map<String, Object> saveItem = new HashMap<>();
        String key = getReference().child("author").push().getKey();
        author.setKey(key);
        saveItem.put(getRouteAuthor(key), author.toMap());
        getReference().updateChildren(saveItem);
        return author;
    }

    public static void getBook(Book book, final InterfaceFireBase interfaceFireBase){
        Query query = getReference().child(getRouteBook(book.getKey()));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map <String, Object> items = (Map<String, Object>) dataSnapshot.getValue();
                for(Map.Entry<String,Object> entry: items.entrySet()){
                    Map mapItem = (Map) entry.getValue();
                    interfaceFireBase.getBook(Book.fromMap(mapItem));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getAuthor(Author author, final InterfaceFireBase interfaceFireBase){
        Query query = getReference().child(getRouteAuthor(author.getKey()));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map <String, Object> items = (Map<String, Object>) dataSnapshot.getValue();
                for(Map.Entry<String,Object> entry: items.entrySet()){
                    Map mapItem = (Map) entry.getValue();
                    interfaceFireBase.getAuthor(Author.fromMap(mapItem));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static Book updateBook(Book book){
        Map<String, Object> saveItem = new HashMap<>();
        saveItem.put(getRouteBook(book.getKey()), book.toMap());
        getReference().updateChildren(saveItem);
        return book;
    }

    public static Author updateAuthor(Author author){
        Map<String, Object> saveItem = new HashMap<>();
        saveItem.put(getRouteAuthor(author.getKey()), author.toMap());
        getReference().updateChildren(saveItem);
        return author;
    }

    public static void removeBook(Book book){
        Map<String, Object> saveItem = new HashMap<>();
        saveItem.put(getRouteBook(book.getKey()), null);
        getReference().updateChildren(saveItem);
    }

    public static void removeAuthor(Author author){
        Map<String, Object> saveItem = new HashMap<>();
        saveItem.put(getRouteAuthor(author.getKey()), null);
        getReference().updateChildren(saveItem);
    }

    public static void getAllBooks(final InterfaceFireBase interfaceFireBase){

        Log.v("XYZ", getUser().getUid() + "");

        Query query = getReference().child(getUser().getUid() + "/book/");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Book> books = new ArrayList<>();
                if(dataSnapshot != null) {
                    Map<String, Object> items = (Map<String, Object>) dataSnapshot.getValue();
                    if(items != null) {
                        for (Map.Entry<String, Object> entry : items.entrySet()) {
                            Map mapItem = (Map) entry.getValue();
                            books.add(Book.fromMap(mapItem));
                        }
                    }
                }
                interfaceFireBase.getAllBooks(books);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getAllAuthors(final InterfaceFireBase interfaceFireBase){
        Query query = getReference().child(getUser().getUid() + "/author/");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Author> authors = new ArrayList<>();
                if(dataSnapshot != null) {
                    Map<String, Object> items = (Map<String, Object>) dataSnapshot.getValue();
                    if(items != null) {
                        for (Map.Entry<String, Object> entry : items.entrySet()) {
                            Map mapItem = (Map) entry.getValue();
                            authors.add(Author.fromMap(mapItem));
                        }
                    }
                }
                interfaceFireBase.getAllAuthors(authors);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void sendPhoto(Uri uri, final InterfaceFireBase interfaceFireBase){
        final String url = getUser().getUid() + "/" + generateRandomText();
        StorageReference image = storage.child(url);
        UploadTask uploadTask = image.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                interfaceFireBase.sendRoutePhoto(url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                interfaceFireBase.sendRoutePhoto(null);
            }
        });
    }

    public static void getPhoto(String url, final InterfaceFireBase interfaceFireBase){

        if( url == null || url.isEmpty() || url.equals("null") ){
            url = "defaultBook.png";
        }

        storage.child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                interfaceFireBase.getRoutePhoto(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                interfaceFireBase.getRoutePhoto(null);
            }
        });
    }

    private static String generateRandomText() {
        SecureRandom random = new SecureRandom();
        String text = new BigInteger(130, random).toString(32);
        return text;
    }

}
