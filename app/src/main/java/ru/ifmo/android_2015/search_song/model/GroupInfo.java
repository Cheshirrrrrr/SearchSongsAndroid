package ru.ifmo.android_2015.search_song.model;

/**
 * Created by vorona on 29.11.15.
 */
public class GroupInfo {
    String id = "";
    String title = "";

    public void setName(String title) {
        this.title = title;
    }

    public String getName() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
