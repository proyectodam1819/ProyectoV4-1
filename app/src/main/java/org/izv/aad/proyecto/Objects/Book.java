package org.izv.aad.proyecto.Objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Book implements Parcelable, Comparable<Book>{

    private long id, idAuthor;
    private String title, urlPhoto, resume, key;
    private float assessment;
    private boolean favorite;
    private DateCustom startDate, endDate;

    public Book() {
        this(0 ,0, "", "" ,"" ,"" ,0 ,false , new DateCustom(), new DateCustom());
    }

    public Book(long id, long idAuthor, String key, String title, String urlPhoto, String resume, float assessment, boolean favorite, DateCustom startDate, DateCustom endDate) {
        this.title = title;
        this.urlPhoto = urlPhoto;
        this.key = key;
        this.resume = resume;
        this.id = id;
        this.idAuthor = idAuthor;
        this.assessment = assessment;
        this.favorite = favorite;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    protected Book(Parcel in) {
        id = in.readLong();
        idAuthor = in.readLong();
        title = in.readString();
        urlPhoto = in.readString();
        resume = in.readString();
        key = in.readString();
        assessment = in.readFloat();
        favorite = in.readByte() != 0;
        startDate = in.readParcelable(DateCustom.class.getClassLoader());
        endDate = in.readParcelable(DateCustom.class.getClassLoader());
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public Book setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
        return this;
    }

    public String getResume() {
        return resume;
    }

    public Book setResume(String resume) {
        this.resume = resume;
        return this;
    }

    public long getId() {
        return id;
    }

    public Book setId(long id) {
        this.id = id;
        return this;
    }

    public long getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(long idAuthor) {
        this.idAuthor = idAuthor;
    }

    public float getAssessment() {
        return assessment;
    }

    public Book setAssessment(float assessment) {
        this.assessment = assessment;
        return this;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public Book setFavorite(boolean favorite) {
        this.favorite = favorite;
        return this;
    }

    public DateCustom getStartDate() {
        return startDate;
    }

    public Book setStartDate(DateCustom startDate) {
        this.startDate = startDate;
        return this;
    }

    public DateCustom getEndDate() {
        return endDate;
    }

    public Book setEndDate(DateCustom endDate) {
        this.endDate = endDate;
        return this;
    }


    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("idAuthor", idAuthor);
        result.put("title", title);
        result.put("urlPhoto", urlPhoto);
        result.put("resume", resume);
        result.put("key", key);
        result.put("assessment", assessment);
        result.put("favorite", favorite);
        result.put("startDate", startDate.toString());
        result.put("endDate", startDate.toString());
        return result;
    }

    public static Book fromMap(Map<String, Object> map){
        Book book = new Book();
        book.id = (Long) map.get("id");
        book.idAuthor = (Long) map.get("idAuthor");
        book.title = (String) map.get("title");
        book.urlPhoto = (String) map.get("urlPhoto");
        book.resume = (String) map.get("resume");
        book.key = (String) map.get("key");
        book.assessment = Float.parseFloat(map.get("assessment").toString());
        book.favorite = (Boolean) map.get("favorite");
        book.startDate = new DateCustom( (String) map.get("startDate"), "d-m-y");
        book.endDate = new DateCustom( (String) map.get("endDate"), "d-m-y");
        return book;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", idAuthor=" + idAuthor +
                ", title='" + title + '\'' +
                ", urlPhoto='" + urlPhoto + '\'' +
                ", resume='" + resume + '\'' +
                ", key='" + key + '\'' +
                ", assessment=" + assessment +
                ", favorite=" + favorite +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(idAuthor);
        dest.writeString(title);
        dest.writeString(urlPhoto);
        dest.writeString(resume);
        dest.writeString(key);
        dest.writeFloat(assessment);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeParcelable(startDate, flags);
        dest.writeParcelable(endDate, flags);
    }

    @Override
    public int compareTo(@NonNull Book book) {
        return this.getTitle().compareTo(book.getTitle());
    }
}
