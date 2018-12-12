package org.izv.aad.proyecto.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Author implements Parcelable {

    private long id;
    private String name, key;

    public Author(String name){
        this(0, name, "");
    }

    public Author() {
        this(0,"", "");
    }

    public Author(long id, String name, String key) {
        this.id = id;
        this.name = name;
        this.key = key;
    }

    protected Author(Parcel in) {
        id = in.readLong();
        name = in.readString();
        key = in.readString();
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    public long getId() {
        return id;
    }

    public Author setId(long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Author setKey(String key) {
        this.key = key;
        return this;
    }

    public String getName() {
        return name;
    }

    public Author setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(key);
    }

    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("key", key);
        return result;
    }

    public static Author fromMap(Map<String, Object> map){
        Author author = new Author();
        author.id = (Long) map.get("id");
        author.name = (String) map.get("name");
        author.key = (String) map.get("key");
        return author;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
