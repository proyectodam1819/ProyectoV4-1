package org.izv.aad.proyecto.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.Objects.DateCustom;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    private AuthorHelper authorHelper;
    private BookHelper bookHelper;
    private SQLiteDatabase bdBook, bdAuthor;

    public Manager(Context context){
        this.authorHelper = new AuthorHelper(context);
        this.bookHelper = new BookHelper(context);
        bdAuthor = authorHelper.getWritableDatabase();
        bdBook = bookHelper.getWritableDatabase();
    }

    public void close(){
        bdAuthor.close();
        bdBook.close();
    }

    /*************************************************
     ********************   INSERT  ******************
     *************************************************/

    public long insertAutor(Author author){
        return bdAuthor.insert(Contract.AuthorTable.TABLE_NAME, null, Contract.contentValuesAuthor(author));
    }

    public long insertLibro(Book book){
        return bdBook.insert(Contract.BookTable.TABLE_NAME, null, Contract.contentValuesBook(book));
    }

    /*************************************************
     ********************   DELETE  ******************
     *************************************************/

    public int deleteLibro(Book book){
        return deleteLibro(book.getId());
    }

    public int deleteLibro(long id){
        String condicion = Contract.BookTable._ID + " = ?";
        String[] argumentos = { id + "" };
        return bdBook.delete(Contract.BookTable.TABLE_NAME, condicion, argumentos);
    }

    public int deleteAutor(Author author){
        return deleteAutor(author.getId());
    }

    public int deleteAutor(long id){
        String condicion = Contract.AuthorTable._ID + " = ?";
        String[] argumentos = { id + "" };
        return bdAuthor.delete(Contract.AuthorTable.TABLE_NAME, condicion, argumentos);
    }

    /*************************************************
     ********************   UPDATE  ******************
     *************************************************/

    public int updateBook(Book book){
        String condicion = Contract.BookTable._ID + " = ?";
        String[] argumentos = { book.getId() + "" };
        return bdBook.update(Contract.BookTable.TABLE_NAME, Contract.contentValuesBook(book), condicion, argumentos);
    }

    public int updateAuthor(Author author){
        String condicion = Contract.AuthorTable._ID + " = ?";
        String[] argumentos = { author.getId() + "" };
        return bdAuthor.update(Contract.AuthorTable.TABLE_NAME, Contract.contentValuesAuthor(author), condicion, argumentos);
    }

    /*************************************************
     ********************   CURSOR  ******************
     *************************************************/

    private Cursor getCursorAuthor(){
        return getCursorAuthor(null, null);
    }

    private Cursor getCursorAuthor(String condicion, String[] argumentos){
        return bdAuthor.query(
                Contract.AuthorTable.TABLE_NAME,
                null,
                condicion,
                argumentos,
                null,
                null,
                Contract.AuthorTable.COLUMN_NAME + " desc",
                null
        );
    }

    private Cursor getCursorBook(){
        return getCursorBook(null, null);
    }

    private Cursor getCursorBook(String condicion, String[] argumentos){
        return bdBook.query(
                Contract.BookTable.TABLE_NAME,
                null,
                condicion,
                argumentos,
                null,
                null,
                Contract.BookTable.COLUMN_ID_AUTHOR + " desc",
                null
        );
    }

    /*************************************************
     *******************   GET_ROW  ******************
     *************************************************/

    private Book getRowBook(Cursor cursor){
        Book book = new Book();

        int posId = cursor.getColumnIndex(Contract.BookTable._ID);
        int posIdAuthor = cursor.getColumnIndex(Contract.BookTable.COLUMN_ID_AUTHOR);
        int posTitle = cursor.getColumnIndex(Contract.BookTable.COLUMN_TITLE);
        int posUrlPhoto = cursor.getColumnIndex(Contract.BookTable.COLUMN_URL_PHOTO);
        int posResume = cursor.getColumnIndex(Contract.BookTable.COLUMN_RESUME);
        int posAssessment = cursor.getColumnIndex(Contract.BookTable.COLUMN_ASSESSMENT);
        int posFavorite = cursor.getColumnIndex(Contract.BookTable.COLUMN_FAVORITE);
        int posStartDate = cursor.getColumnIndex(Contract.BookTable.COLUMN_START_DATE);
        int posEndDate = cursor.getColumnIndex(Contract.BookTable.COLUMN_END_DATE);
        int posKey = cursor.getColumnIndex(Contract.BookTable.COLUMN_KEY);

        book.setTitle(cursor.getString(posTitle));
        book.setUrlPhoto(cursor.getString(posUrlPhoto));
        book.setResume(cursor.getString(posResume));
        book.setId(cursor.getLong(posId));
        book.setIdAuthor(cursor.getLong(posIdAuthor));
        book.setAssessment(cursor.getFloat(posAssessment));
        book.setKey(cursor.getString(posKey));
        if(cursor.getInt(posFavorite) > 0){
            book.setFavorite(true);
        }else{
            book.setFavorite(false);
        }

        book.setStartDate(new DateCustom(cursor.getString(posStartDate), "d-m-y"));
        book.setEndDate(new DateCustom(cursor.getString(posEndDate), "d-m-y"));

        return book;
    }

    private Author getRowAutor(Cursor cursor){
        Author author = new Author();
        int posId = cursor.getColumnIndex(Contract.AuthorTable._ID);
        int posName = cursor.getColumnIndex(Contract.AuthorTable.COLUMN_NAME);
        int posKey = cursor.getColumnIndex(Contract.AuthorTable.COLUMN_KEY);

        author.setId(cursor.getLong(posId));
        author.setName(cursor.getString(posName));
        author.setKey(cursor.getString(posKey));

        return author;
    }

    /*************************************************
     ********************   SELECT  ******************
     *************************************************/

    public Author getAuthor(long id){
        Author author = null;
        String condicion = Contract.AuthorTable._ID + " = ?";
        String[] argumentos = { id + "" };
        Cursor cursor = getCursorAuthor(condicion, argumentos);

        while(cursor.moveToNext()){
            author = getRowAutor(cursor);
        }
        return author;
    }

    public Author getAuthor(String name){
        Author author = null;
        String condicion = Contract.AuthorTable.COLUMN_NAME + " = ?";
        String[] argumentos = { name + "" };
        Cursor cursor = getCursorAuthor(condicion, argumentos);

        while(cursor.moveToNext()){
            author = getRowAutor(cursor);
        }
        return author;
    }

    public List<Book> getAllBooks(){
        List<Book> books = new ArrayList<>();
        Cursor cursor = getCursorBook();
        Book book;

        while(cursor.moveToNext()){
            book = getRowBook(cursor);
            books.add(book);
        }
        return books;
    }

    public List<Book> getBooksByAuthor(long authorId){
        List<Book> books = new ArrayList<>();
        String condicion = Contract.BookTable.COLUMN_ID_AUTHOR + " = ?";
        String[] argumentos = { authorId + "" };
        Cursor cursor = getCursorBook(condicion, argumentos);
        Book book;

        while(cursor.moveToNext()){
            book = getRowBook(cursor);

            Log.v("XYZ", book.toString());

            books.add(book);
        }
        return books;
    }

    public List<Author> getAllAuthor(String condicion){
        List<Author> authors = new ArrayList<>();
        Cursor cursor = getCursorAuthor(condicion, null);
        if(condicion == null){
            cursor = getCursorAuthor();
        }
        Author author;

        while(cursor.moveToNext()){
            author = getRowAutor(cursor);
            authors.add(author);
        }
        return authors;
    }

    public List<Book> getFilterBooks(String title){
        List<Book> books = new ArrayList<>();
        String condicion = Contract.BookTable.COLUMN_TITLE + " like ?";
        String[] argumentos = { "%" + title + "%" };
        Cursor cursor = getCursorBook(condicion, argumentos);

        while(cursor.moveToNext()){
            books.add(getRowBook(cursor));
        }
        return books;
    }

    /*************************************************
     *********************   DROP  *******************
     *************************************************/

    public void dropTables(){
        authorHelper.dropTable(bdAuthor);
        bookHelper.dropTable(bdBook);
    }

    public void createTables(){
        bookHelper.onCreate(bdBook);
        authorHelper.onCreate(bdAuthor);
    }
}
