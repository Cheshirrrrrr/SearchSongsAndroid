package ru.ifmo.android_2015.search_song.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vorona on 25.11.15.
 */
public class Group implements Parcelable {
    public  String id = "";
    public String title = "";
    public String coverURI = "";
    public String relevant = "";
    public String bioURI = "";


    public Group() {
        id = "";
        title = "";
        coverURI = "";
        relevant = "";
        bioURI = "";
    }

    //--------
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(id);
        dest.writeString(relevant);
        dest.writeString(coverURI);
        dest.writeString(bioURI);
    }

    public Group(Parcel src) {
        title = src.readString();
        id = src.readString();
        relevant = src.readString();
        coverURI = src.readString();
        bioURI = src.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
