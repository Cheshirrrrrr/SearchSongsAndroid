package ru.ifmo.android_2015.search_song;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

import ru.ifmo.android_2015.search_song.model.Group;
import android.renderscript.Element;

/**
 * Created by vorona on 01.12.15.
 */
public class TextAsyncTask extends AsyncTask<String, Void, Void> {
    private static final String LOGTAG = "DownloadingText";
    private Activity activity;
    private DownloadState state;
    private TextView title, text, translation, text1;
    private Button btnStart, btnStop,btnStart1;
    private static String textOfSong = "";
    private static String translOfSong = "", group = "", song = "", source = "";
    private boolean trans = true;
    public static String url1 = "";

    enum DownloadState {
        DOWNLOADING(R.string.downloading),
        DONE(R.string.done),
        ERROR(R.string.error),
        NOSONGS(R.string.no_info),
        NOTRANSL(R.string.no_transl)
        ;

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
        text1 = (TextView) activity.findViewById(R.id.scrollText1);
        btnStart = (Button) activity.findViewById(R.id.btnResume2);
        btnStart1 = (Button) activity.findViewById(R.id.btnResume1);
        btnStop = (Button) activity.findViewById(R.id.btnStop2);

        title.setText(R.string.downloading);
        Log.w("TextAsyncTask", "We started Async");
        textOfSong = "";
        translOfSong = "";
    }

    public void attachActivity(Activity activity) {
        this.activity = activity;
        updateView(activity);
        Log.w("TextAsyncTask", "We've attached");
    }

    private void updateView(Activity activity) {
        Log.w("TextAsyncTask", "We try to update");
    }

    @Override
    protected Void doInBackground(String... params) {
        song = params[0];
        group = params[1];
        source = params[2];
        Log.w("TextAsyncTask", "We went into do..." + group + " " + song);
        state = DownloadState.DOWNLOADING;
        try {
            url1 = listen(group, song);
        } catch (IOException e) {
            Log.w("TextAsyncTask", "We got exc in do...");
            return null;
        }
        Log.w("TextAsyncTask", url1);

        if (Amalgama(group)) {
            return null;
        }
        else {
            Megalyrics(group);
            state = DownloadState.NOTRANSL;
        }

        Log.w("TextAsyncTask", "Group: " + group);
        return null;
    }

    boolean Megalyrics(String str) {
            Log.w("TextAsyncTask", "Russian group");
            try {
                textOfSong = originalFromMegalyrics(source);
            } catch (Exception e) {
                Log.w("TextAsyncTask", "We got exc in Megalyrics");
                return false;
            }
            trans = false;
            return true;
    }

    boolean Amalgama(String group) {
        String[] songs;
        try {
            songs = amalgamaTranslations(group);
        } catch (Exception e) {
            Log.w("TextAsyncTask", "We got exc in Amalgama");
            return false;
        }
        Log.w("TextAsyncTask", "We got groups");
        if (songs != null) {
            if (songs[0] == null) {
                Log.w("TextAsyncTask", "We got empty first song");
                state = DownloadState.NOSONGS;
                return false;
            } else {
                state = DownloadState.DONE;
                int flag = onAmalgama(song, songs);

                if (flag >= 0 && flag < songs.length) {
                    try {
                        printInformation(group, song);
                    } catch (IOException e) {
                        return false;
                    }

                } else {
                    state = DownloadState.NOSONGS;
                    Log.w("TextAsyncTask", "We got exc in amalgama->print");
                    return false;
                }
            }

        } else {
            Log.w("TextAsyncTask", "We got empty first song1");
            state = DownloadState.NOSONGS;
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Void voi) {

        title = (TextView) activity.findViewById(R.id.txtName);

        Log.w("TextAsyncTask", "We in Post...");
        if (state == DownloadState.NOSONGS) {
            title.setText(R.string.no_info);
            return;
        }
        if (state == DownloadState.ERROR) {
            title.setText(R.string.error);
            return;
        }
        if (!trans || state == DownloadState.NOTRANSL) {
            title.setText(group + " - " + song);
            text1.setText("Text");
            if (!textOfSong.equals("")){
                text1.setText(textOfSong);
                text1.setVisibility(View.VISIBLE);
                btnStart1.setVisibility(View.VISIBLE);
            } else {
                title.setText(R.string.no_info);
            }
            translation.setHeight(0);

        } else {
            title.setText(group + " - " + song);

            if (!textOfSong.equals("") && !translOfSong.equals("")) {
                text.setText(textOfSong);
                translation.setText(translOfSong);
                text.setVisibility(View.VISIBLE);
                translation.setVisibility(View.VISIBLE);
                activity.findViewById(R.id.textView3).setVisibility(View.VISIBLE);
                btnStart1.setVisibility(View.VISIBLE);
            } else {
                title.setText(R.string.no_info);
            }
            text1.setVisibility(View.INVISIBLE);
        }
    }


    final static boolean IGNORE_THE = true;
    final static boolean NON_IGNORE_THE = false;

    public static String[] amalgamaTranslations(String groupName) throws IOException, InterruptedException {
        Log.w("TextAsyncTask", "We in amalgamaTransl...");
        try {
            groupName = amalgamaFormat(groupName, IGNORE_THE, "_");
            Document html = Jsoup.connect("http://www.amalgama-lab.com/songs/" + URLEncoder.encode(String.valueOf(groupName.charAt(0)), "utf-8") + "/" + URLEncoder.encode(groupName, "utf-8") + "/").get();
            Log.w("TextAsyncTask", "We in amalgamaTransl1...");

            Object listOfUl[] = html.body().getElementById("songs_nav").select("ul").toArray();
            Log.w("TextAsyncTask", "We in amalgamaTransl1,5...");
            Document translationList = Jsoup.parse(listOfUl[1].toString());
            Log.w("TextAsyncTask", "We in amalgamaTransl2...");
            Object listOfObjTranslations[] = translationList.select("li").select("li").toArray();
            Log.w("TextAsyncTask", "We in amalgamaTransl3...");
            String listOfTranslation[] = new String[listOfObjTranslations.length];
            for (int i = 0; i < listOfObjTranslations.length; i++) {
                listOfTranslation[i] = Jsoup.parse(listOfObjTranslations[i].toString()).text();
                listOfTranslation[i] = amalgamaFormat(listOfTranslation[i], NON_IGNORE_THE, "_");
                System.out.println(listOfTranslation[i]);
                Log.w("TextAsyncTask", "We in amalgamaTransl4...");
            }
            Log.w("TextAsyncTask", "We out amalgamaTransl...");
            return listOfTranslation;

        } catch (Exception ignored) {
        }
        return null;
    }

    public static String amalgamaFormat(String oldName, boolean flag, String divider) {
        Log.w("TextAsyncTask", "We in amalgamaFormat...");
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
        Log.w("TextAsyncTask", "We out amalgamaFormat...");
        return newName;
    }

    public static int onAmalgama(String trackName, String[] listOfTranslations) {
        Log.w("TextAsyncTask", "We in onAmalgama...");
        trackName = amalgamaFormat(trackName, NON_IGNORE_THE, "_");
        return Arrays.binarySearch(listOfTranslations, trackName);
    }

    public static void printInformation(String artist, String song) throws IOException {
        Log.w("TextAsyncTask", "We in printInformation...");
        artist = amalgamaFormat(artist, IGNORE_THE, "_");
        song = amalgamaFormat(song, NON_IGNORE_THE, "_");

        Document html = Jsoup.connect("http://www.amalgama-lab.com/songs/" + artist.charAt(0) + "/" + artist + "/" + song + ".html").get();

//        printTextByClass(html, "original");
        Object text[] = html.getElementsByClass("original").toArray();
        String txt = "";
        for (Object aText : text) {
            txt += Jsoup.parse(aText.toString()).text() + "\n";
        }

        text = html.getElementsByClass("translate").toArray();
        String transl = "";
        for (Object aText : text) {
            transl += Jsoup.parse(aText.toString()).text() + "\n";
        }
        textOfSong = txt;
        translOfSong = transl;
    }

    private static String originalFromMegalyrics(String songSource) throws IOException {
        Document html = Jsoup.connect(songSource).get();
        String text = html.getElementsByClass("text_inner").html().replace("<br>", "$$$");
        String txt = Jsoup.parse(text).text().replace("$$$", "\n");
        return txt;
    }

    public String listen(String artist, String song) throws IOException {
        Log.w("TextAsyncTask", "We in listen");
        String forSearch = (artist + "+" + song).replace(" ", "+");
        Log.w("TextAsyncTask", forSearch);
        Document html = Jsoup.connect("http://mp3.cc/search/f/" + URLEncoder.encode(forSearch, "utf-8")).get();
        String url = html.getElementsByClass("playlist-btn").select("a").get(1).absUrl("href");
        return url;
    }
}
