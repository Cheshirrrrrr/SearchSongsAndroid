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
import ru.ifmo.android_2015.search_song.list.FirstRecyclerAdapter;
import ru.ifmo.android_2015.search_song.list.FirstSelectedListener;
import ru.ifmo.android_2015.search_song.list.RecylcerDividersDecorator;
import ru.ifmo.android_2015.search_song.list.SingerRecyclerAdapter;
import ru.ifmo.android_2015.search_song.model.Album;
import ru.ifmo.android_2015.search_song.model.AlbumInfo;
import ru.ifmo.android_2015.search_song.model.ArrayOfAlbums;
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
            Log.e(LOGTAG, e.getMessage());
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


    //      ищет по запросу группы, вытаскивает из html  страницы яндекс музыки информацию о найденых группах в формате json
    public static JsonReader getJson(String searchName) throws IOException {
        Log.w("AlbumsAsyncTask", "We in getJson");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document html = Jsoup.connect(searchName).get();
        Log.w("SingerAsyncTask", "We in getJson1");
        String json = html.body().child(0).data();
        Log.w("SingerAsyncTask", "We in getJson2");
        String groupInformation = json.substring(json.indexOf('{'), json.lastIndexOf("}") + 1);
        Log.w("SingerAsyncTask", "We in getJson3");
        // создает JsonReader  и парсит его
        StringReader reader = new StringReader(groupInformation);
        Log.w("SingerAsyncTask", "We in getJson4");
        JsonReader jsonReader = Json.createReader(reader);
        Log.w("SingerAsyncTask", "We out getJson");
        return jsonReader;
    }

    // json  парсер, парсит  найденые группы
//    // JsonObject , всеравно все уже выгрузили.
    public static Group[] getGroup(String searchName) throws IOException {

        Log.w("SingerAsyncTask", "We in getGroup");
        JsonReader jsonReader = getJson("https://music.yandex.ru/search?text=" + searchName);
        Log.w("SingerAsyncTask", "We in getGroup 0");
        JsonArray JsonGroups = jsonReader.readObject().getJsonObject("pageData").getJsonObject("result").getJsonObject("artists").getJsonArray("items");
        Group groups [] = new Group[JsonGroups.size()];
        Log.w("SingerAsyncTask", "We in getGroup 1");
        for (int i = 0; i < JsonGroups.size(); i++) {
            Log.w("SingerAsyncTask", "We in getGroup 2");
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

        Log.w("SingerAsyncTask", "We go out getGroup");
        return groups;
    }


}

