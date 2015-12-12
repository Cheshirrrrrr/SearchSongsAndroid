package ru.ifmo.android_2015.search_song;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.list.SongSelectedListener;
import ru.ifmo.android_2015.search_song.model.Group;
import ru.ifmo.android_2015.search_song.model.Track;

/**
 * Created by vorona on 24.11.15.
 */
public class SongsOfSingerActivity extends AppCompatActivity implements SongSelectedListener {


    public static final String EXTRA_ID = "id";
    public static final String EXTRA_COVER = "cover";
    public static final String EXTRA_TITLE= "title";
    public static final String EXTRA_RELEVANT= "relevant";
    public static final String EXTRA_BIO= "bio";
    public static final String EXTRA_ALBUM= "album";
//    public static final String EXTRA_GROUP = "group";


    private String album, singer, cover, rel, bio;
    private TextView name;
    private SongsOfSingerAsyncTask downloadTask;
    private  String id;
    private  Group gr = new Group();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "I'm here");
        setContentView(R.layout.activity_album_selected);

        album = getIntent().getStringExtra(EXTRA_ALBUM);
        singer = getIntent().getStringExtra(EXTRA_TITLE);
        id = getIntent().getStringExtra(EXTRA_ID);
        cover = getIntent().getStringExtra(EXTRA_COVER);
        bio = getIntent().getStringExtra(EXTRA_BIO);
        rel = getIntent().getStringExtra(EXTRA_RELEVANT);
//        gr = getIntent().getParcelableExtra(EXTRA_GROUP);

        name = (TextView) findViewById(R.id.album_name);

        if (album == null) {
            Log.w(TAG, "object not provided in extra parameter: " + EXTRA_ALBUM);
            finish();
        }
        name.setText(album);

        if (savedInstanceState != null) {
            downloadTask = (SongsOfSingerAsyncTask) getLastCustomNonConfigurationInstance();
        }
        if (downloadTask == null) {
            downloadTask = new SongsOfSingerAsyncTask(this);
            gr.id = id;
            gr.bioURI = bio;
            gr.relevant = rel;
            gr.coverURI = cover;
            gr.title = singer;

            downloadTask.execute(gr);
        } else {
            downloadTask.attachActivity(this);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return downloadTask;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("TITLE", name.getText().toString());
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        if (bundle != null) {
            name.setText(bundle.getString("TITLE"));
        }
        super.onRestoreInstanceState(bundle);
    }

    private static final String TAG = "SongSelected";

    @Override
    public void onSongSelected(Track song) {
        Log.v(TAG, "onTextClicked: ");
        Intent text = new Intent(this, TextSelectedActivity.class);
        text.putExtra(TextSelectedActivity.EXTRA_SONG, song.title);
        text.putExtra(TextSelectedActivity.EXTRA_SINGER, song.artist);
        text.putExtra(TextSelectedActivity.EXTRA_SOURCE, song.source); //TODO source
        startActivity(text);
    }
}
