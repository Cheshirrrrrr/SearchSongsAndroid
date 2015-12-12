package ru.ifmo.android_2015.search_song.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by vorona on 12.12.15.
 */
public class ArrayOfSongs2 {

    private static ArrayList<Tracks> tracks = new ArrayList<Tracks>();

    public static int getCount() {
        return tracks.size();
    }

    public static void addSong(Tracks track) {
        Log.w("BySongAsyncTask", "We try to add");
        tracks.add(track);
    }

    public static Tracks getSong(int position) {
        return tracks.get(position);
    }

    public static void clear() {
        tracks.clear();
    }

    private ArrayOfSongs2() {}
}
