package ru.ifmo.android_2015.search_song.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by vorona on 02.12.15.
 */
public class ArrayOfSingers {

    private static ArrayList<Group> group_arr = new ArrayList<Group>();

    public static int getCount() {
        return group_arr.size();
    }

    public static void addGroup(Group group) {
        Log.w("SingerAsyncTask", "We try to add");
        group_arr.add(group);
    }

    public static Group getGroup(int position) {
        return group_arr.get(position);
    }

    public static void clear() {
        group_arr.clear();
    }

    private ArrayOfSingers() {}
}
