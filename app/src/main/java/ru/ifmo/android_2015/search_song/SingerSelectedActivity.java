package ru.ifmo.android_2015.search_song;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.list.AlbumSelectedListener;
import ru.ifmo.android_2015.search_song.list.SingerSelectedListener;
import ru.ifmo.android_2015.search_song.model.Album;

public class SingerSelectedActivity extends AppCompatActivity implements AlbumSelectedListener {

    public static final String EXTRA_SINGER = "singer";

    private String singer;
    private TextView name;

    private AlbumsAsyncTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "I'm here");
        setContentView(R.layout.activity_singer_selected);
        singer = getIntent().getStringExtra(EXTRA_SINGER);
        name = (TextView) findViewById(R.id.txtSingerName);

        if (singer == null) {
            Log.w(TAG, "object not provided in extra parameter: " + EXTRA_SINGER);
            finish();
        }
        name.setText(singer);

        if (savedInstanceState != null) {
            downloadTask = (AlbumsAsyncTask) getLastCustomNonConfigurationInstance();
        }
        if (downloadTask == null) {
            downloadTask = new AlbumsAsyncTask(this);
            downloadTask.execute(singer);
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

    private static final String TAG = "SingerSelected";

    @Override
    public void onAlbumSelected(Album album1) {
        Log.v(TAG, "onAlbumClicked: ");
        Intent album = new Intent(this, AlbumSelectedActivity.class);
        String str = album1.title;
        album.putExtra(AlbumSelectedActivity.EXTRA_ALBUM, str);
        album.putExtra(AlbumSelectedActivity.EXTRA_ID, album1.id);
        album.putExtra(AlbumSelectedActivity.EXTRA_SINGER, singer);
        startActivity(album);
    }
}
