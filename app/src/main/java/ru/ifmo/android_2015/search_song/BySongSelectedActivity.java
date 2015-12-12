package ru.ifmo.android_2015.search_song;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.list.SongSelectedListener;
import ru.ifmo.android_2015.search_song.model.Track;

/**
 * Created by vorona on 24.11.15.
 */
public class BySongSelectedActivity extends AppCompatActivity implements SongSelectedListener {
    public static final String EXTRA_SONG = "song";

    private String song;
    private TextView name;

    private BySongAsyncTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "I'm here");
        setContentView(R.layout.activity_song_selected);
        song = getIntent().getStringExtra(EXTRA_SONG);
        name = (TextView) findViewById(R.id.txtSongName);
        if (song == null) {
            Log.w(TAG, "object not provided in extra parameter: " + EXTRA_SONG);
            finish();
        }
        name.setText(song);

        if (savedInstanceState != null) {
            downloadTask = (BySongAsyncTask) getLastCustomNonConfigurationInstance();
        }
        if (downloadTask == null) {
            downloadTask = new BySongAsyncTask(this);
            downloadTask.execute(song);
        } else {
            downloadTask.attachActivity(this);
        }

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {

        super.onRestoreInstanceState(bundle);
    }

    private static final String TAG = "BySongSelectedActivity";

    @Override
    public void onSongSelected(Track track) {
        Log.v(TAG, "onTextClicked: ");
        Intent text = new Intent(this, TextSelectedActivity.class);
        text.putExtra(TextSelectedActivity.EXTRA_SONG, track.title);
        text.putExtra(TextSelectedActivity.EXTRA_SINGER, track.artist);
        text.putExtra(TextSelectedActivity.EXTRA_SOURCE, track.source);
        startActivity(text);
    }
}
