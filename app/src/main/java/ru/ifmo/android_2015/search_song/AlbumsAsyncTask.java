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
import javax.json.JsonObject;
import javax.json.JsonReader;

import ru.ifmo.android_2015.search_song.list.AlbumSelectedListener;
import ru.ifmo.android_2015.search_song.list.SingerRecyclerAdapter;
import ru.ifmo.android_2015.search_song.list.SingerSelectedListener;
import ru.ifmo.android_2015.search_song.list.RecylcerDividersDecorator;
import ru.ifmo.android_2015.search_song.model.Album;
import ru.ifmo.android_2015.search_song.model.ArrayOfAlbums;
import ru.ifmo.android_2015.search_song.model.Group;
import ru.ifmo.android_2015.search_song.model.AlbumInfo;
import ru.ifmo.android_2015.search_song.model.GroupInfo;


public class AlbumsAsyncTask extends AsyncTask<String, AlbumInfo, AlbumInfo>{

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

    public AlbumsAsyncTask(Activity activity) {
        this.activity = activity;
        title = (TextView) activity.findViewById(R.id.txtSingerName);
        title.setText(R.string.downloading);
        Log.w("AlbumsAsyncTask", "We started Async");
        ArrayOfAlbums.clear();
    }

    public void attachActivity(Activity activity) {
        this.activity = activity;
        updateView(activity);
        Log.w("AlbumsAsyncTask", "We've attached");
    }

    private void updateView(Activity activity) {
        Log.w("AlbumsAsyncTask", "We try to update");

    }

    @Override
    protected AlbumInfo doInBackground(String... params) {
        try {
            Log.w("AlbumsAsyncTask", "We went into do..." + params[0]);
            state = DownloadState.DOWNLOADING;

            Group[] group = getGroup(params[0]);
            Group res = group[0];

            AlbumInfo albumInfo = new AlbumInfo();
            groupInfo = new GroupInfo();

            Log.w("AlbumsAsyncTask", "We got groups");
            if (group != null) {
                groupInfo.setName(res.title);
                groupInfo.setId(res.id);
                if (res.title == null) {
                    Log.w("AlbumsAsyncTask", "We got empty first group");
                    state = DownloadState.NOALBS;
                } else {
                    state = DownloadState.DONE;
                }
            } else {
                state = DownloadState.ERROR;
            }


            Album[] albums = getAlbums(group[0].id);
            Log.w("AlbumsAsyncTask", "We make array");
            for (int i = 0; i < albums.length; i++) {
                Log.w("AlbumsAsyncTask", "We make array 1");
                ArrayOfAlbums.addAlbum(albums[i]);
                Log.w("AlbumsAsyncTask", "We make array 2");
                albumInfo.setName(albums[i].title);
                Log.w("AlbumsAsyncTask", "We make array 3");
            }
            Log.w("AlbumsAsyncTask", "We make array 4");
            return albumInfo;
        } catch (Exception e) {
            Log.e(LOGTAG, e.getMessage());
            Log.w("AlbumsAsyncTask", "We got exc...");
            return null;
        }
    }



    @Override
    protected void onPostExecute(AlbumInfo alb) {

        title = (TextView) activity.findViewById(R.id.txtSingerName);

        Log.w("AlbumsAsyncTask", "We in Post...");
        if (alb == null || state == DownloadState.NOALBS) {
            title.setText(R.string.no_info);
            return;
        }
        if(state == DownloadState.ERROR){
            title.setText(R.string.error);
        }
        else {
            title.setText(groupInfo.getName());
        }
        RecyclerView recyclerView;
//        Log.w("Album", "our albums: " + ArrayOfAlbums.getAlbum(0) + ", " + ArrayOfAlbums.getAlbum(1));
        recyclerView = (RecyclerView) activity.findViewById(R.id.list1);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(Color.BLUE));
        SingerRecyclerAdapter adapter = new SingerRecyclerAdapter(activity);
        adapter.setSingerSelectedListener((AlbumSelectedListener) activity);
        recyclerView.setAdapter(adapter);
    }


    private static Album[] getAlbums(int id) throws IOException {

        JsonReader jsonReader = getJson("https://music.yandex.ru/artist/" + Integer.toString(id) + "/albums");
        JsonArray jsonAlbums = jsonReader.readObject().getJsonObject("pageData").getJsonArray("albums");
        Album[] albums = new Album[jsonAlbums.size()];

        for (int i = 0; i < jsonAlbums.size(); i++) {
            albums[i] = new Album();
            JsonObject currentAlbum = jsonAlbums.getJsonObject(i);
            albums[i].id = currentAlbum.getInt("id");
            albums[i].title = currentAlbum.getString("title");
            albums[i].year = currentAlbum.getInt("year");
            albums[i].genre = currentAlbum.getString("genre");
            albums[i].coverURI = currentAlbum.getString("coverUri");
            albums[i].coverURI = albums[i].coverURI.replace("%%", "200x200"); //размер подгоним
            currentAlbum = null;
        }
        return albums;
    }


//      ищет по запросу группы, вытаскивает из html  страницы яндекс музыки информацию о найденых группах в формате json
    public static JsonReader getJson(String searchName) throws IOException {
        Log.w("AlbumsAsyncTask", "We in getJson");
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document html = Jsoup.connect(searchName).get();
        Log.w("AlbumsAsyncTask", "We in getJson1");
        String json = html.body().child(0).data();
        Log.w("AlbumsAsyncTask", "We in getJson2");
        String groupInformation = json.substring(json.indexOf('{'), json.lastIndexOf("}") + 1);
        Log.w("AlbumsAsyncTask", "We in getJson3");
        // создает JsonReader  и парсит его
        StringReader reader = new StringReader(groupInformation);
        Log.w("AlbumsAsyncTask", "We in getJson4");
        JsonReader jsonReader = Json.createReader(reader);
        Log.w("AlbumsAsyncTask", "We out getJson");
        return jsonReader;
    }

    // json  парсер, парсит  найденые группы
//    // JsonObject , всеравно все уже выгрузили.
    public static Group[] getGroup(String searchName) throws IOException {

        Log.w("AlbumsAsyncTask", "We in getGroup");
        JsonReader jsonReader = getJson("https://music.yandex.ru/search?text=" + searchName);
        Log.w("AlbumsAsyncTask", "We in getGroup 0");
        JsonArray JsonGroups = jsonReader.readObject().getJsonObject("pageData").getJsonObject("result").getJsonObject("artists").getJsonArray("items");
        Group groups [] = new Group[JsonGroups.size()];
        Log.w("AlbumsAsyncTask", "We in getGroup 1");
        for (int i = 0; i < JsonGroups.size(); i++) {
            Log.w("AlbumsAsyncTask", "We in getGroup 2");
            groups[i] = new Group();
            JsonObject currentGroup = JsonGroups.getJsonObject(i);
            groups[i].id = currentGroup.getInt("id");
            groups[i].title = currentGroup.getString("name");
            groups[i].tracks = currentGroup.getJsonObject("counts").getInt("tracks");
            Object tempGenres[] = currentGroup.getJsonArray("genres").toArray();
            groups[i].genres = new String[tempGenres.length];
            for (int j = 0; j < tempGenres.length; j++) {
                groups[i].genres[j] = tempGenres[j].toString();
                tempGenres[j] = null;
            }
//            groups[i].coverURI = currentGroup.getJsonObject("cover").getString("uri");
//            groups[i].coverURI = groups[i].coverURI.replace("%%", "200x200");

            currentGroup = null;
            tempGenres = null;
        }

        Log.w("AlbumsAsyncTask", "We go out getGroup");
        return groups;
    }


}
