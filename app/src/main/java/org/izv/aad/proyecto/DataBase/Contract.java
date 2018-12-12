package org.izv.aad.proyecto.DataBase;

import android.content.ContentValues;
import android.provider.BaseColumns;

import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;

public class Contract {

    public static final String SQL_CREATE_TABLE_BOOK =
            "CREATE TABLE " + BookTable.TABLE_NAME + " (" +
                    BookTable._ID + " integer primary key autoincrement," +
                    BookTable.COLUMN_ID_AUTHOR + " integer," +
                    BookTable.COLUMN_TITLE +" text," +
                    BookTable.COLUMN_URL_PHOTO + " text," +
                    BookTable.COLUMN_RESUME + " text," +
                    BookTable.COLUMN_ASSESSMENT + " integer," +
                    BookTable.COLUMN_FAVORITE + " integer," +
                    BookTable.COLUMN_START_DATE + " text," +
                    BookTable.COLUMN_END_DATE + " text," +
                    BookTable.COLUMN_KEY + " text unique," +
                    "FOREIGN KEY("+ BookTable.COLUMN_ID_AUTHOR +") REFERENCES "+ AuthorTable.COLUMN_NAME +"("+ AuthorTable._ID+") )";


    public static final String SQL_DELETE_TABLE_BOOK =
            "DROP TABLE if exists " + BookTable.TABLE_NAME;


    public static final String SQL_CREATE_TABLE_AUTHOR =
            "CREATE TABLE " + AuthorTable.TABLE_NAME + " (" +
                    AuthorTable._ID + " integer primary key autoincrement," +
                    AuthorTable.COLUMN_KEY + " text unique," +
                    AuthorTable.COLUMN_NAME + " text)";


    public static final String SQL_DELETE_TABLE_AUTHOR =
            "DROP TABLE if exists " + AuthorTable.TABLE_NAME;

    private Contract(){}

    public static class BookTable implements BaseColumns {
        public static final String TABLE_NAME = "book";
        public static final String COLUMN_ID_AUTHOR = "idAuthor";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_URL_PHOTO = "urlPhoto";
        public static final String COLUMN_RESUME = "resume";
        public static final String COLUMN_ASSESSMENT = "assessment";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_START_DATE = "startDate";
        public static final String COLUMN_END_DATE = "endDate";
        public static final String COLUMN_KEY = "key";
    }

    public static class AuthorTable implements BaseColumns {
        public static final String TABLE_NAME = "author";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_KEY = "key";
    }

    public static ContentValues contentValuesBook(Book book){
        ContentValues cv = new ContentValues();
        cv.put(BookTable.COLUMN_ID_AUTHOR, book.getIdAuthor());
        cv.put(BookTable.COLUMN_TITLE, book.getTitle());
        cv.put(BookTable.COLUMN_URL_PHOTO, book.getUrlPhoto());
        cv.put(BookTable.COLUMN_RESUME, book.getResume());
        cv.put(BookTable.COLUMN_ASSESSMENT, book.getAssessment());
        cv.put(BookTable.COLUMN_FAVORITE, book.isFavorite());
        cv.put(BookTable.COLUMN_START_DATE, book.getStartDate().toString());
        cv.put(BookTable.COLUMN_END_DATE, book.getEndDate().toString());
        return cv;
    }

    public static  ContentValues contentValuesAuthor(Author author){
        ContentValues cv = new ContentValues();
        cv.put(AuthorTable.COLUMN_NAME, author.getName());
        return cv;
    }

}
