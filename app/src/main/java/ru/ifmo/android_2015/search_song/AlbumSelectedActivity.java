package ru.ifmo.android_2015.search_song;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.list.AlbumSelectedListener;
import ru.ifmo.android_2015.search_song.list.SingerSelectedListener;

/**
 * Created by vorona on 24.11.15.
 */
public class AlbumSelectedActivity extends AppCompatActivity implements SingerSelectedListener {

    public static final String EXTRA_ALBUM = "album";
    public static final String EXTRA_SINGER = "singer";
    public static final String EXTRA_ID = "id";

    private String album, singer;
    private TextView name;
    private SongAsyncTask downloadTask;
    private  int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "I'm here");
        setContentView(R.layout.activity_album_selected);
        album = getIntent().getStringExtra(EXTRA_ALBUM);
        singer = getIntent().getStringExtra(EXTRA_SINGER);
        id = getIntent().getIntExtra(EXTRA_ID, 0);
        name = (TextView) findViewById(R.id.album_name);

        if (album == null) {
            Log.w(TAG, "object not provided in extra parameter: " + EXTRA_ALBUM);
            finish();
        }
        name.setText(album);

        if (savedInstanceState != null) {
            downloadTask = (SongAsyncTask) getLastCustomNonConfigurationInstance();
        }
        if (downloadTask == null) {
            downloadTask = new SongAsyncTask(this);
            downloadTask.execute(id);
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

    private static final String TAG = "AlbumSelected";

    @Override
    public void onSingerSelected(String song) {
        Log.v(TAG, "onTextClicked: ");
        Intent text = new Intent(this, TextSelectedActivity.class);
        text.putExtra(TextSelectedActivity.EXTRA_TEXT, song);
        text.putExtra(TextSelectedActivity.EXTRA_SINGER, name.getText());
        startActivity(text);
    }
}
