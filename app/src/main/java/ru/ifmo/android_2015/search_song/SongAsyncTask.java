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

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

import ru.ifmo.android_2015.search_song.list.RecylcerDividersDecorator;
import ru.ifmo.android_2015.search_song.list.AlbumRecyclerAdapter;
import ru.ifmo.android_2015.search_song.list.AlbumSelectedListener;
import ru.ifmo.android_2015.search_song.list.SingerSelectedListener;
import ru.ifmo.android_2015.search_song.model.ArrayOfSongs;

/**
 * Created by vorona on 29.11.15.
 */
public class SongAsyncTask extends AsyncTask<Object, Void, Void> {
    private static final String LOGTAG = "Downloading";
    private Activity activity;
    private DownloadState state;
    private TextView title;
    private String album_title = "";

    enum DownloadState {
        DOWNLOADING(R.string.downloading),
        DONE(R.string.done),
        ERROR(R.string.error),
        NOSONGS(R.string.no_info);

        // ID строкового ресурса для заголовка окна прогресса
        final int titleResId;

        DownloadState(int titleResId) {
            this.titleResId = titleResId;
        }
    }

    public SongAsyncTask(Activity activity) {
        this.activity = activity;
        title = (TextView) activity.findViewById(R.id.album_name);
        title.setText(R.string.downloading);
        Log.w("SongAsyncTask", "We started Async");
        ArrayOfSongs.clear();
    }

    public void attachActivity(Activity activity) {
        this.activity = activity;
        updateView(activity);
        Log.w("SongAsyncTask", "We've attached");
    }

    private void updateView(Activity activity) {
        Log.w("SongAsyncTask", "We try to update");
    }

    @Override
    protected Void doInBackground(Object... params) {
        album_title = (String) params[1];
        try {
            Log.w("SongAsyncTask", "We went into do..." + params[0]);
            state = DownloadState.DOWNLOADING;
            String[] songs = getSongs((Integer)params[0]);

            Log.w("SongAsyncTask", "We got groups");
            if (songs != null) {
                if (songs[0] == null) {
                    Log.w("SongAsyncTask", "We got empty first song");
                    state = DownloadState.NOSONGS;
                } else {
                    state = DownloadState.DONE;
                }
            } else {
                state = DownloadState.ERROR;
            }
            Log.w("SongAsyncTask", "We make array");
            for (int i = 0; i < songs.length; i++) {
                Log.w("SongAsyncTask", "We make array 1");
                ArrayOfSongs.addSong(songs[i]);
                Log.w("SongAsyncTask", "We make array 2");
            }
            Log.w("SongAsyncTask", "We make array 4");

        } catch (Exception e) {
            state = DownloadState.ERROR;
            Log.e(LOGTAG, e.getMessage());
            Log.w("SongAsyncTask", "We got exc...");

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void voi) {

        title = (TextView) activity.findViewById(R.id.album_name);
        Log.w("SongAsyncTask", "We in Post...");
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
        AlbumRecyclerAdapter adapter = new AlbumRecyclerAdapter(activity);
        adapter.setCitySelectedListener((SingerSelectedListener)activity);
        recyclerView.setAdapter(adapter);

    }


    private static String[] getSongs(int id) throws IOException {
        Log.w("SongAsyncTask", "We in getJson");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JsonReader jsonReader = getJson("https://music.yandex.ru/album/" + Integer.toString(id));
        JsonArray jsonVolumes = jsonReader.readObject().getJsonObject("pageData").getJsonArray("volumes");
        int size = 0;
        for (int i = 0; i < jsonVolumes.size(); i++) {
            size += jsonVolumes.getJsonArray(i).size();
        }
        String songs[] = new String[size];
        int k = 0;
        for (int i = 0; i < jsonVolumes.size(); i++)  {
            JsonArray currentVolume = jsonVolumes.getJsonArray(i);
            for (int j = 0; j < currentVolume.size(); j++, k++) {
                songs[k] = currentVolume.getJsonObject(j).getString("title");

            }
            currentVolume = null;
        }
        return songs;
    }

    //      ищет по запросу группы, вытаскивает из html  страницы яндекс музыки информацию о найденых группах в формате json
    public static JsonReader getJson(String searchName) throws IOException {
        Log.w("SongAsyncTask", "We in getJson");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document html = Jsoup.connect(searchName).get();
        Log.w("SongAsyncTask", "We in getJson1");
        String json = html.body().child(0).data();
        Log.w("SongAsyncTask", "We in getJson2");
        String groupInformation = json.substring(json.indexOf('{'), json.lastIndexOf("}") + 1);
        Log.w("SongAsyncTask", "We in getJson3");
        // создает JsonReader  и парсит его
        StringReader reader = new StringReader(groupInformation);
        Log.w("SongAsyncTask", "We in getJson4");
        JsonReader jsonReader = Json.createReader(reader);
        Log.w("SongAsyncTask", "We out getJson");
        return jsonReader;
    }

}
