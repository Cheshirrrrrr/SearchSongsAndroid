package ru.ifmo.android_2015.search_song;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by vorona on 24.11.15.
 */
public class TextSelectedActivity extends AppCompatActivity{

    public static final String EXTRA_SONG = "song";
    public static final String EXTRA_SINGER = "singer";

    private String song, singer;
    private TextView name;
    private TextAsyncTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "I'm here");
        setContentView(R.layout.activity_text_song);
        song = getIntent().getStringExtra(EXTRA_SONG);
        singer = getIntent().getStringExtra(EXTRA_SINGER);
        name = (TextView) findViewById(R.id.txtName);

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
            downloadTask.execute(song, singer);
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
//        bundle.putString("TITLE", camTitle.getText().toString());
//        bundle.putString("LAT", lat.getText().toString());
//        bundle.putString("LON", lon.getText().toString());
//        Drawable temp = camImageView.getDrawable();
//        if (temp != null) {
//            bundle.putParcelable("IMAGE", ((BitmapDrawable) temp).getBitmap());
//        }
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
//        if (bundle != null) {
//            camTitle.setText(bundle.getString("TITLE"));
//            lat.setText(bundle.getString("LAT"));
//            lon.setText(bundle.getString("LON"));
//            if (downloadTask.getState() == AlbumsAsyncTask.DownloadState.DOWNLOADING) {
//                progressView.setVisibility(View.VISIBLE);
//            } else {
//                Bitmap bitmap = bundle.getParcelable("IMAGE");
//                camImageView.setImageBitmap(bitmap);
//                progressView.setVisibility(View.INVISIBLE);
//            }
//        }
        super.onRestoreInstanceState(bundle);
    }

    private static final String TAG = "TextSelected";

}

