package ru.ifmo.android_2015.search_song;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by vorona on 24.11.15.
 */
public class TextSelectedActivity extends AppCompatActivity{

    public static final String EXTRA_SONG = "song";
    public static final String EXTRA_SINGER = "singer";
    public static final String EXTRA_SOURCE = "source";

    private String song, singer, source;
    private TextView name;
    private TextAsyncTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "I'm here");
        setContentView(R.layout.activity_text_song);
        song = getIntent().getStringExtra(EXTRA_SONG);
        singer = getIntent().getStringExtra(EXTRA_SINGER);
        source = getIntent().getStringExtra(EXTRA_SOURCE);
        name = (TextView) findViewById(R.id.txtName);
        TextView textview= (TextView) findViewById(R.id.scrollText);
        textview.setMovementMethod(new ScrollingMovementMethod());
        textview= (TextView) findViewById(R.id.scrollTranslation);
        textview.setMovementMethod(new ScrollingMovementMethod());


        if (song == null) {
            Log.w(TAG, "object not provided in extra parameter: " + EXTRA_SONG);
            finish();
        }
        name.setText(singer + " - " + song);

        if (savedInstanceState != null) {
            downloadTask = (TextAsyncTask) getLastCustomNonConfigurationInstance();
        }
        if (downloadTask == null) {
            downloadTask = new TextAsyncTask(this);
            downloadTask.execute(song, singer, source);
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
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
    }

    private static final String TAG = "TextSelected";

}

