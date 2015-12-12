package ru.ifmo.android_2015.search_song;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
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
import java.util.Arrays;
import java.util.Comparator;

import ru.ifmo.android_2015.search_song.list.FirstRecyclerAdapter;
import ru.ifmo.android_2015.search_song.list.GroupSelectedListener;
import ru.ifmo.android_2015.search_song.list.RecylcerDividersDecorator;
import ru.ifmo.android_2015.search_song.model.Group;

/**
 * Created by vorona on 02.12.15.
 */
public class ChooseGroupAsyncTask extends AsyncTask<String, Void, Void> {

    private static final String LOGTAG = "Downloading";
    private Activity activity;
    private DownloadState state;
    private TextView title;

    public static Group[] groups;

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

    public static Group getGroupInPosition(int position) {
        return groups[position];
    }

    public static int numberOfGroups() {
        return groups.length;
    }

    public ChooseGroupAsyncTask(Activity activity) {
        this.activity = activity;
        title = (TextView) activity.findViewById(R.id.txtWritten);
        title.setText(R.string.downloading);
        Log.w("ChooseGroupAsyncTask", "We started Async");
        groups = new Group[0];
    }

    public void attachActivity(Activity activity) {
        this.activity = activity;
        updateView(activity);
        Log.w("ChooseGroupAsyncTask", "We've attached");
    }

    private void updateView(Activity activity) {

        Log.w("ChooseGroupAsyncTask", "We try to update");

    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            Log.w("ChooseGroupAsyncTask", "We went into do..." + params[0]);
            state = DownloadState.DOWNLOADING;

            groups = getGroup(params[0]);

            if (groups != null) {
                if (groups[0].title == null) {
                    Log.w("ChooseGroupAsyncTask", "We got empty first group");
                    state = DownloadState.NOALBS;
                } else {
                    state = DownloadState.DONE;
                }
            } else {
                state = DownloadState.ERROR;
            }

            return null;
        } catch (Exception e) {
            Log.w("ChooseGroupAsyncTask", "We got exc...");
            return null;
        }
    }


    @Override
    protected void onPostExecute(Void vi) {

        title = (TextView) activity.findViewById(R.id.txtWritten);

        Log.w("ChooseGroupAsyncTask", "We in Post...");
        if (groups == null || state == DownloadState.NOALBS) {
            title.setText(R.string.no_info);
            return;
        }
        if (state == DownloadState.ERROR) {
            title.setText(R.string.error);
        } else {
            title.setText("Выберите группу");
        }
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) activity.findViewById(R.id.list0);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(Color.BLUE));
        FirstRecyclerAdapter adapter = new FirstRecyclerAdapter(activity);
        adapter.setSingerSelectedListener((GroupSelectedListener) activity);
        recyclerView.setAdapter(adapter);
    }


    public static Group[] getGroup(String searchName) throws IOException {

        Log.w("ChooseGroupAsyncTask", "We in getGroup");
        searchName = "http://megalyrics.ru/search?utf8=✓&search=" + URLEncoder.encode(searchName, "utf-8");
        Document html = Jsoup.connect(searchName).get();
        Log.w("ChooseGroupAsyncTask", "We in getGroup0");
        Elements artists = html.getElementById("artists_list").select("a");
        Log.w("ChooseGroupAsyncTask", "We in getGroup0,5");
        Group[] groups = new Group[artists.size()];
        Log.w("SingerAsyncTask", "We in getGroup1");
        int i = 0;
        for (Element artist : artists) {
            groups[i] = new Group();
            groups[i].id = artist.absUrl("href");
            groups[i].coverURI = artist.select("img[src]").attr("src");
            groups[i].title = artist.getElementsByClass("name").text();
            groups[i].relevant = artist.getElementsByClass("plays").text();
            i++;

        }
        Log.w("ChooseGroupAsyncTask", "We in getGroup2");
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


        Log.w("ChooseGroupAsyncTask", "We go out getGroup");
        return groups;
    }


}

