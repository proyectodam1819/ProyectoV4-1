package org.izv.aad.proyecto.Objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Date;

public class DateCustom implements Parcelable {

    private String day, month, year;

    public DateCustom(int day, int month, int year){
        this(String.valueOf(day), String.valueOf(month), String.valueOf(year));
    }

    public DateCustom(String date, String format){
        String[] dates = date.split("-");
        String day = "00";
        String month = "00";
        String year = "00";
        if(format.equals("d-m-y")){
            day = dates[0];
            month = dates[1];
            year = dates[2];
        }else{
            day = dates[2];
            month = dates[1];
            year = dates[0];
        }
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public DateCustom(){
        this("00","00","0000");
    }

    public DateCustom(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    protected DateCustom(Parcel in) {
        day = in.readString();
        month = in.readString();
        year = in.readString();
    }

    public static final Creator<DateCustom> CREATOR = new Creator<DateCustom>() {
        @Override
        public DateCustom createFromParcel(Parcel in) {
            return new DateCustom(in);
        }

        @Override
        public DateCustom[] newArray(int size) {
            return new DateCustom[size];
        }
    };

    public DateCustom setDate(String date, String format, String separator){
        String[] dates = date.split(separator);
        String day = "00";
        String month = "00";
        String year = "00";
        if(!date.isEmpty()) {
            if (format.equals("d-m-y")) {
                day = dates[0];
                month = dates[1];
                year = dates[2];
            } else {
                day = dates[2];
                month = dates[1];
                year = dates[0];
            }
        }
        this.day = day;
        this.month = month;
        this.year = year;
        return this;
    }

    public String getDate(){
        return getDate("-");
    }

    public String getDate(String separator){
        return day + separator + month + separator + year;
    }

    public String getDateEnglish(){
        return getDateEnglish("-");
    }

    public String getDateEnglish(String separator){
        return year + separator + month + separator + day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeString(month);
        dest.writeString(year);
    }

    @Override
    public String toString() {
        return getDate();
    }
}
