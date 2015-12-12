package ru.ifmo.android_2015.search_song;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.list.FirstSelectedListener;
import ru.ifmo.android_2015.search_song.model.Group;

/**
 * Created by vorona on 02.12.15.
 */
public class ChooseGroupActivity extends AppCompatActivity implements FirstSelectedListener {

    public static final String EXTRA_SINGER = "singer";

    private String singer;
    private TextView name;

    private SingerAsyncTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "I'm here");
        setContentView(R.layout.activity_choose_singer);
        singer = getIntent().getStringExtra(EXTRA_SINGER);
        name = (TextView) findViewById(R.id.txtWritten);

        if (singer == null) {
            Log.w(TAG, "object not provided in extra parameter: " + EXTRA_SINGER);
            finish();
        }
        name.setText(singer);

        if (savedInstanceState != null) {
            downloadTask = (SingerAsyncTask) getLastCustomNonConfigurationInstance();
        }
        if (downloadTask == null) {
            downloadTask = new SingerAsyncTask(this);
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

    private static final String TAG = "ChooseSinger";

    @Override
    public void onSingerSelected(Group group) {
        Log.v(TAG, "onAlbumClicked: ");
        Intent gr = new Intent(this, AlbumSelectedActivity.class);
        gr.putExtra(AlbumSelectedActivity.EXTRA_ID, group.id);
        gr.putExtra(AlbumSelectedActivity.EXTRA_ALBUM, "asdf");
        gr.putExtra(AlbumSelectedActivity.EXTRA_TITLE, group.title);
        gr.putExtra(AlbumSelectedActivity.EXTRA_COVER, group.coverURI);
        gr.putExtra(AlbumSelectedActivity.EXTRA_RELEVANT, group.relevant);
        gr.putExtra(AlbumSelectedActivity.EXTRA_BIO, group.bioURI);
        startActivity(gr);

    }
}
