package ru.ifmo.android_2015.search_song;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

import ru.ifmo.android_2015.search_song.list.FirstRecyclerAdapter;
import ru.ifmo.android_2015.search_song.list.FirstSelectedListener;
import ru.ifmo.android_2015.search_song.list.RecylcerDividersDecorator;
import ru.ifmo.android_2015.search_song.model.ArrayOfSingers;
import ru.ifmo.android_2015.search_song.model.Group;
import ru.ifmo.android_2015.search_song.model.GroupInfo;

/**
 * Created by vorona on 02.12.15.
 */
public class SingerAsyncTask extends AsyncTask<String, GroupInfo, GroupInfo> {

        private static final String LOGTAG = "Downloading";
        private Activity activity;
        private DownloadState state;
        private TextView title;

        GroupInfo groupInfo = null;

        enum DownloadState {
            DOWNLOADING(R.string.downloading),
            DONE(R.string.done),
            ERROR(R.string.error),
            NOALBS(R.string.no_info);

            final int titleResId;

            DownloadState(int titleResId) {
                this.titleResId = titleResId;
            }
        }

        public SingerAsyncTask(Activity activity) {
        this.activity = activity;
        title = (TextView) activity.findViewById(R.id.txtWritten);
        title.setText(R.string.downloading);
        Log.w("SingerAsyncTask", "We started Async");
        ArrayOfSingers.clear();
    }

    public void attachActivity(Activity activity) {
        this.activity = activity;
        updateView(activity);
        Log.w("SingerAsyncTask", "We've attached");
    }

    private void updateView(Activity activity) {

        Log.w("SingerAsyncTask", "We try to update");

    }

    @Override
    protected GroupInfo doInBackground(String... params) {
        try {
            Log.w("SingerAsyncTask", "We went into do..." + params[0]);
            state = DownloadState.DOWNLOADING;

            Group[] group = getGroup(params[0]);
            groupInfo = new GroupInfo();

            Log.w("SingerAsyncTask", "We got groups");
            if (group != null) {
                groupInfo.setName(group[0].title);
                groupInfo.setId(group[0].id);
                if (group[0].title == null) {
                    Log.w("SingerAsyncTask", "We got empty first group");
                    state = DownloadState.NOALBS;
                } else {
                    state = DownloadState.DONE;
                }

                Log.w("SingerAsyncTask", "We make array");
                for (int i = 0; i < group.length; i++) {
                    Log.w("SingerAsyncTask", "We make array 1");
                    ArrayOfSingers.addGroup(group[i]);
                    Log.w("SingerAsyncTask", "We make array 2");
                    groupInfo.setName(group[i].title);
                    Log.w("SingerAsyncTask", "We make array 3");
                }
            } else {
                state = DownloadState.ERROR;
            }

            return groupInfo;
        } catch (Exception e) {
            Log.w("SingerAsyncTask", "We got exc...");
            return null;
        }
    }



    @Override
    protected void onPostExecute(GroupInfo gr) {

        title = (TextView) activity.findViewById(R.id.txtWritten);

        Log.w("SingerAsyncTask", "We in Post...");
        if (gr == null || state == DownloadState.NOALBS) {
            title.setText(R.string.no_info);
            return;
        }
        if(state == DownloadState.ERROR){
            title.setText(R.string.error);
        }
        else {
            title.setText("Выберите группу");
        }
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) activity.findViewById(R.id.list0);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(Color.BLUE));
        FirstRecyclerAdapter adapter = new FirstRecyclerAdapter(activity);
        adapter.setSingerSelectedListener((FirstSelectedListener) activity);
        recyclerView.setAdapter(adapter);
    }


    public static Group[] getGroup(String searchName) throws IOException {

        Log.w("SingerAsyncTask", "We in getGroup");
        searchName = "http://megalyrics.ru/search?utf8=✓&search=" + URLEncoder.encode(searchName, "utf-8");
        Document html = Jsoup.connect(searchName).get();
        Log.w("SingerAsyncTask", "We in getGroup0");
        Elements artists = html.getElementById("artists_list").select("a");
        Log.w("SingerAsyncTask", "We in getGroup0,5");
        Group [] groups = new Group[artists.size()];
        Log.w("SingerAsyncTask", "We in getGroup1");
        int i = 0;
        for(Element artist : artists) {
            groups[i] = new Group();
            groups[i].id = artist.absUrl("href");
            groups[i].coverURI = artist.select("img[src]").attr("src");
            groups[i].title = artist.getElementsByClass("name").text();
            groups[i].relevant = artist.getElementsByClass("plays").text();
            i++;

        }
        Log.w("SingerAsyncTask", "We in getGroup2");
        Arrays.sort(groups, new Comparator<Group>() {
                    @Override
                    public int compare(Group g1, Group g2) {
                        int len1 = g1.relevant.length();
                        int len2 = g2.relevant.length();
                        if (len1 == len2) {
                            return g2.relevant.compareTo(g1.relevant);
                        }
                        return len2 - len1;
                    }
                });


                Log.w("SingerAsyncTask", "We go out getGroup");
        return groups;
    }


}

