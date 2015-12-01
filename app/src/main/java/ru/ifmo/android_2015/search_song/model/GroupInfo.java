package ru.ifmo.android_2015.search_song.model;

/**
 * Created by vorona on 29.11.15.
 */
public class GroupInfo {
    int id = 0;
    String title = "";
    int tracks = 0;
    String genres [] = null;
    String coverURI = "";


    public void setName(String title) {
        this.title = title;
    }

    public String getName() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
