package ru.ifmo.android_2015.search_song.model;

import android.util.Log;

import java.util.ArrayList;

public final class ArrayOfAlbums {


    private static ArrayList<Album> albums_arr = new ArrayList<Album>();

    public static int getCount() {
        return albums_arr.size();
    }

    public static void addAlbum(Album album) {
        Log.w("AlbumsAsyncTask", "We try add");
        albums_arr.add(album);
    }

    public static Album getAlbum(int position) {
        return albums_arr.get(position);
    }

    public static void clear() {
        albums_arr.clear();
    }

    private ArrayOfAlbums() {}
}
