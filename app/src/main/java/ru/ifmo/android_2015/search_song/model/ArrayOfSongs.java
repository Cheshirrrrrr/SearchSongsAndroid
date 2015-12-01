package ru.ifmo.android_2015.search_song.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by vorona on 29.11.15.
 */
public class ArrayOfSongs {
    private static ArrayList<String> songs_arr = new ArrayList<String>();

    public static int getCount() {
        return songs_arr.size();
    }

    public static void addSong(String song) {
        Log.w("SongAsyncTask", "We try add");
        songs_arr.add(song);
    }

    public static String getSong(int position) {
        return songs_arr.get(position);
    }

    public static void clear() {
        songs_arr.clear();
    }

    private ArrayOfSongs() {}
}
