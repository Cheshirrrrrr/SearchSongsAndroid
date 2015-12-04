package ru.ifmo.android_2015.search_song;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonReader;

import ru.ifmo.android_2015.search_song.model.ArrayOfSongs;

/**
 * Created by vorona on 01.12.15.
 */
public class TextAsyncTask extends AsyncTask<String, Void, Void> {
    private static final String LOGTAG = "DownloadingText";
    private Activity activity;
    private DownloadState state;
    private TextView title, text, translation;
    private static String textOfSong  = "";
    private static String translOfSong = "", group = "", song = "";
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

    public TextAsyncTask(Activity activity) {
        this.activity = activity;
        title = (TextView) activity.findViewById(R.id.txtName);
        text = (TextView) activity.findViewById(R.id.scrollText);
        translation = (TextView) activity.findViewById(R.id.scrollTranslation);
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
    protected Void doInBackground(String... params) {
        try {
            song = params[0];
            group = params[1];
            Log.w("TextAsyncTask", "We went into do..." + params[0]);
            state = DownloadState.DOWNLOADING;
            String[] songs = amalgamaTranslations(group);

            Log.w("TextAsyncTask", "We got groups");
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
            int flag = onAmalgama(song, songs);

            if (flag >= 0 && flag < songs.length) {
                printInformation(group, song);
            }  else {
                state  = DownloadState.NOSONGS;
            }


        } catch (Exception e) {
            Log.e(LOGTAG, e.getMessage());
            Log.w("TextAsyncTask", "We got exc...");

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void voi) {

        title = (TextView) activity.findViewById(R.id.txtName);

        Log.w("TextAsyncTask", "We in Post...");
        if ( state == DownloadState.NOSONGS) {
            title.setText(R.string.no_info);
            text.setVisibility(View.INVISIBLE);
            translation.setVisibility(View.INVISIBLE);
            activity.findViewById(R.id.textView3).setVisibility(View.INVISIBLE);
            return;
        }
        if(state == DownloadState.ERROR){
            title.setText(R.string.error);
        }
        else {
            title.setText(group + " - " + song);
            text.setText("Text");
            if (!textOfSong.equals("")) text.setText(textOfSong);
            translation.setText("Translation");
            if (!translOfSong.equals("")) translation.setText(translOfSong);
        }
    }


    final static boolean IGNORE_THE = true;
    final static boolean NON_IGNORE_THE = false;

    public static String[] amalgamaTranslations(String groupName) throws IOException, InterruptedException {
        groupName = amalgamaFormat(groupName, IGNORE_THE, "_");
//        Thread.sleep(500);
        Document html = Jsoup.connect("http://www.amalgama-lab.com/songs/" + groupName.charAt(0) +"/" + groupName + "/").get();
        Object listOfUl[] = html.body().getElementById("songs_nav").select("ul").toArray();
        Document translationList = Jsoup.parse(listOfUl[1].toString());
        Object listOfObjTranslations[] = translationList.select("li").select("li").toArray();

        String listOfTranslation[] = new String[listOfObjTranslations.length];
        for (int i = 0; i < listOfObjTranslations.length; i++) {
            listOfTranslation[i] = Jsoup.parse(listOfObjTranslations[i].toString()).text();
            listOfTranslation[i] = amalgamaFormat(listOfTranslation[i], NON_IGNORE_THE, "_");
            System.out.println(listOfTranslation[i]);
        }
        return listOfTranslation;
    }

    public static String amalgamaFormat(String oldName, boolean flag, String divider) {
        String name[] = oldName.split(" ");
        String newName = "";
        for (int i = 0; i < name.length; i++) {
            name[i] = name[i].toLowerCase();
            if (flag && name[i].equals("the") && i == 0) {
                continue;
            }
            if (newName.length() > 0) {
                newName = newName.concat(divider);
            }
            newName = newName.concat(name[i]);
        }
        return newName;
    }

    public static int onAmalgama(String trackName, String[] listOfTranslations) {
        trackName = amalgamaFormat(trackName, NON_IGNORE_THE,"_");
         return Arrays.binarySearch(listOfTranslations, trackName);
    }

    public static void printInformation(String artist, String song) throws IOException {
        artist = amalgamaFormat(artist, IGNORE_THE, "_");
        song = amalgamaFormat(song, NON_IGNORE_THE, "_");

        Document html = Jsoup.connect("http://www.amalgama-lab.com/songs/" + artist.charAt(0) +"/" + artist + "/" + song + ".html").get();

//        printTextByClass(html, "original");
        Object text[] = html.getElementsByClass("original").toArray();
        String txt = "";
        for (Object aText : text) {
            txt += Jsoup.parse(aText.toString()).text() + "\n";
        }

        text = html.getElementsByClass("translate").toArray();
        String transl = "";
        for (Object aText : text) {
           transl +=  Jsoup.parse(aText.toString()).text() + "\n";
        }
        textOfSong = txt;
        translOfSong = transl;
//        printTextByClass(html, "translate");
    }


}
