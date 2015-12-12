package ru.ifmo.android_2015.search_song;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.list.GroupSelectedListener;
import ru.ifmo.android_2015.search_song.model.Group;

/**
 * Created by vorona on 02.12.15.
 */
public class ChooseGroupActivity extends AppCompatActivity implements GroupSelectedListener {

    public static final String EXTRA_SINGER = "singer";

    private String singer;
    private TextView name;

    private ChooseGroupAsyncTask downloadTask;

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
            downloadTask = (ChooseGroupAsyncTask) getLastCustomNonConfigurationInstance();
        }
        if (downloadTask == null) {
            downloadTask = new ChooseGroupAsyncTask(this);
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
        Intent gr = new Intent(this, SongsOfSingerActivity.class);
        gr.putExtra(SongsOfSingerActivity.EXTRA_ID, group.id);
        gr.putExtra(SongsOfSingerActivity.EXTRA_ALBUM, "asdf");
        gr.putExtra(SongsOfSingerActivity.EXTRA_TITLE, group.title);
        gr.putExtra(SongsOfSingerActivity.EXTRA_COVER, group.coverURI);
        gr.putExtra(SongsOfSingerActivity.EXTRA_RELEVANT, group.relevant);
        gr.putExtra(SongsOfSingerActivity.EXTRA_BIO, group.bioURI);
        startActivity(gr);

    }
}
