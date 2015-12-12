package ru.ifmo.android_2015.search_song;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import ru.ifmo.android_2015.search_song.list.RecylcerDividersDecorator;
import ru.ifmo.android_2015.search_song.list.SongsOfSingerRecyclerAdapter;
import ru.ifmo.android_2015.search_song.list.SongSelectedListener;
import ru.ifmo.android_2015.search_song.model.Group;
import ru.ifmo.android_2015.search_song.model.Track;

/**
 * Created by vorona on 29.11.15.
 */
public class SongsOfSingerAsyncTask extends AsyncTask<Group, Void, Void> {
    private static final String LOGTAG = "Downloading";
    private Activity activity;
    private DownloadState state;
    private TextView title;
    private String album_title = "";
    private Group group = new Group();
    private  Bitmap my_b;
    public static Track[] songs;

    enum DownloadState {
        DOWNLOADING(R.string.downloading),
        DONE(R.string.done),
        ERROR(R.string.error),
        NOSONGS(R.string.no_info),
        NOIMG(R.string.fuck);

        // ID строкового ресурса для заголовка окна прогресса
        final int titleResId;

        DownloadState(int titleResId) {
            this.titleResId = titleResId;
        }
    }

    public static String getSongInPosition(int position) {
        return songs[position].title;
    }

    public static int numberOfSongs() {
        return songs.length;
    }


    public SongsOfSingerAsyncTask(Activity activity) {
        this.activity = activity;
        title = (TextView) activity.findViewById(R.id.album_name);
        title.setText(R.string.downloading);
        Log.w("SongsOfSingerAsyncTask", "We started Async");
        songs = new Track[0];
    }

    public void attachActivity(Activity activity) {
        this.activity = activity;
        updateView(activity);
        Log.w("SongsOfSingerAsyncTask", "We've attached");
    }

    private void updateView(Activity activity) {
        Log.w("SongsOfSingerAsyncTask", "We try to update");
    }

    @Override
    protected Void doInBackground(Group... params) {
        album_title = params[0].title;
        group = params[0];
        try {
            Log.w("SongsOfSingerAsyncTask", "We went into do..." + params[0]);
            state = DownloadState.DOWNLOADING;
            songs = getSongs(params[0]);

            Log.w("SongsOfSingerAsyncTask", "We got groups");
            if (songs != null) {
                if (songs[0] == null) {
                    Log.w("SongsOfSingerAsyncTask", "We got empty first song");
                    state = DownloadState.NOSONGS;
                } else {
                    state = DownloadState.DONE;
                }
            } else {
                state = DownloadState.ERROR;
            }

            try {
                my_b = getBitmap(group);
            } catch (Exception e) {
               state = DownloadState.NOIMG;
            }

        } catch (Exception e) {
            state = DownloadState.ERROR;
            Log.w("SongsOfSingerAsyncTask", "We got exc...");

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void voi) {

        title = (TextView) activity.findViewById(R.id.album_name);
        Log.w("SongsOfSingerAsyncTask", "We in Post...");
        if ( state == DownloadState.NOSONGS) {
            title.setText(R.string.no_info);
            return;
        }
        if(state == DownloadState.ERROR){
            title.setText(R.string.error);
        } else {
            title.setText(album_title);
        }
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) activity.findViewById(R.id.list3);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(Color.BLUE));
        SongsOfSingerRecyclerAdapter adapter = new SongsOfSingerRecyclerAdapter(activity);
        adapter.setSelectedListener((SongSelectedListener) activity);
        recyclerView.setAdapter(adapter);

        ImageView view = (ImageView) activity.findViewById(R.id.album_image);
        ProgressBar pr = (ProgressBar) activity.findViewById(R.id.progress3);
        pr.setVisibility(View.INVISIBLE);
        if (group == null || state == DownloadState.NOSONGS) {
            view.setImageResource(R.drawable.cat1);
            return;
        }
        if(state == DownloadState.ERROR ){
            view.setImageResource(R.drawable.cat1);
            title.setText(R.string.error);
            return;
        }
        if(state == DownloadState.NOIMG ){
            view.setImageResource(R.drawable.cat1);
            title.setText(R.string.fuck);
            return;
        }
        view.setImageBitmap(my_b);


    }
    private Bitmap getBitmap(Group group) throws IOException {
        HttpURLConnection connection = null;
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            String str = "http://megalyrics.ru" + group.coverURI;
            URLEncoder.encode(str, "utf-8");
            connection = (HttpURLConnection) (new URL(str)).openConnection();
            is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            ImageView view = (ImageView) activity.findViewById(R.id.album_image);
            view.setImageResource(R.drawable.cat1);
        } finally {
            if (is != null) {
                is.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return  bitmap;
    }


    static Track[] getSongs(Group group) throws IOException {

        Document html = Jsoup.connect(group.id).get();
        Elements songs = html.getElementsByClass("st-title");
        System.out.println(songs);
        Track[] tracks = new Track[songs.size() - 1];
        int i = -1;
        for (Element song :songs) {
            if(i == -1) {
                i++;
                continue;
            }
            tracks[i] = new Track();
            tracks[i].title = song.text();
            tracks[i].artist = group.title;
            tracks[i].source = "http://megalyrics.ru/" + song.select("a").attr("href");
            i++;
        }
        group.bioURI = html.getElementsByClass("text").select("a").first().absUrl("href");
        return tracks;

    }

}
