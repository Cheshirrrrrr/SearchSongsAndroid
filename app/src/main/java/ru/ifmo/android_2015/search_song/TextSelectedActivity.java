package ru.ifmo.android_2015.search_song;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

/**
 * Created by vorona on 24.11.15.
 */
public class TextSelectedActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    public static final String EXTRA_SONG = "song";
    public static final String EXTRA_SINGER = "singer";
    public static final String EXTRA_SOURCE = "source";

    private String song, singer, source;
    private TextView name;
    private TextAsyncTask downloadTask;
    private String url;
    MediaPlayer mediaPlayer;
    AudioManager am;


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
        am = (AudioManager) getSystemService(AUDIO_SERVICE);

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
            url = TextAsyncTask.url1;
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

    public void onStopClick(View view) {
        mediaPlayer.stop();
    }

    public void onStartClick(View view) {
        try {
            Log.w("TextAct", "start HTTP");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(TextAsyncTask.url1);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.w("TextAct", "Error");
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.w("TextSelectedActivity", "onPrepared");
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.w("TextSelectedActivity", "onCompletion");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                Log.w("TextAct", "in release error");
            }
        }
    }
}

