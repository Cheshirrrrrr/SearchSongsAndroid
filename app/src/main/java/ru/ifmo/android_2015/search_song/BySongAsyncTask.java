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

import ru.ifmo.android_2015.search_song.list.RecylcerDividersDecorator;
import ru.ifmo.android_2015.search_song.list.SecondRecyclerAdapter;
import ru.ifmo.android_2015.search_song.list.SongSelectedListener;
import ru.ifmo.android_2015.search_song.model.Track;

/**
 * Created by vorona on 12.12.15.
 */
public class BySongAsyncTask extends AsyncTask<String, Void, Void> {

    private static final String LOGTAG = "Downloading";
    private Activity activity;
    private DownloadState state;
    private TextView title;
    public static Track[] tracks;

    enum DownloadState {
        DOWNLOADING(R.string.downloading),
        DONE(R.string.done),
        ERROR(R.string.error),
        NOSONGS(R.string.no_info);

        final int titleResId;

        DownloadState(int titleResId) {
            this.titleResId = titleResId;
        }
    }

    public static Track getTrackInPosition(int position) {
        return tracks[position];
    }

    public static int numberOfTracks() {
        return tracks.length;
    }

    public BySongAsyncTask(Activity activity) {
        this.activity = activity;
        title = (TextView) activity.findViewById(R.id.txtSongName);
        title.setText(R.string.downloading);
        Log.w("BySongAsyncTask", "We started Async");
        tracks = new Track[0];
    }

    public void attachActivity(Activity activity) {
        this.activity = activity;
        updateView(activity);
        Log.w("BySongAsyncTask", "We've attached");
    }

    private void updateView(Activity activity) {

        Log.w("BySongAsyncTask", "We try to update");

    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            Log.w("BySongAsyncTask", "We went into do..." + params[0]);
            state = DownloadState.DOWNLOADING;

            tracks = getBySongName(params[0]);

            Log.w("BySongAsyncTask", "We got groups");
            if (tracks != null) {
                if (tracks[0].title == null) {
                    Log.w("BySongAsyncTask", "We got empty first song");
                    state = DownloadState.NOSONGS;
                } else {
                    state = DownloadState.DONE;
                }
            } else {
                state = DownloadState.ERROR;
            }

            return null;
        } catch (Exception e) {
            Log.w("BySongAsyncTask", "We got exc...");
            return null;
        }
    }


    @Override
    protected void onPostExecute(Void vi) {

        title = (TextView) activity.findViewById(R.id.txtSongName);

        Log.w("BySongAsyncTask", "We in Post...");
        if (tracks == null || state == DownloadState.NOSONGS){
            title.setText(R.string.no_info);
            return;
        }
        if (state == DownloadState.ERROR) {
            title.setText(R.string.error);
        } else {
            title.setText("Выберите группу");
        }
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) activity.findViewById(R.id.list2);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(Color.BLUE));
        SecondRecyclerAdapter adapter = new SecondRecyclerAdapter(activity);
        adapter.setSongSelectedListener((SongSelectedListener) activity);
        recyclerView.setAdapter(adapter);
    }


    public static Track[] getBySongName(String searchName) throws IOException {
        Document html = Jsoup.connect("http://megalyrics.ru/search?utf8=✓&search=" + URLEncoder.encode(searchName, "utf-8")).get();
        Elements songTable = html.getElementsByClass("songs-table").select("tr");
        Track[] tracks = new Track[songTable.size()];
        int i = -1;
        for (Element song : songTable) {
            if (i == -1) {
                i++;
                continue;
            }
            tracks[i] = new Track();
            System.out.println();
            tracks[i].source = "http://megalyrics.ru/" + song.getElementsByClass("st-title").select("a").attr("href");
            tracks[i].title = song.getElementsByClass("st-title").text();
            tracks[i].artist = song.getElementsByClass("st-artist").text();
            tracks[i].length = song.getElementsByClass("st-duration").text();
            i++;
        }
        return tracks;
    }


}
