package ru.ifmo.android_2015.search_song;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.list.SingerRecyclerAdapter;
import ru.ifmo.android_2015.search_song.list.SingerSelectedListener;
import ru.ifmo.android_2015.search_song.list.RecylcerDividersDecorator;
import ru.ifmo.android_2015.search_song.list.SongSelectedListener;
import ru.ifmo.android_2015.search_song.model.Album;

/**
 * Created by vorona on 24.11.15.
 */
public class SongSelectedActivity extends AppCompatActivity implements SongSelectedListener {
    public static final String EXTRA_SONG = "song";

    private String song;
    private TextView name;
    private RecyclerView recyclerView;

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

        recyclerView = (RecyclerView) findViewById(R.id.list2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(Color.BLUE));
        SingerRecyclerAdapter adapter = new SingerRecyclerAdapter(this);
//        adapter.setSingerSelectedListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return null;
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

//    @Override
    public void onSongSelected(String song) {
        Log.v(TAG, "onTextClicked: ");
        Intent text = new Intent(this, TextSelectedActivity.class);
//        String str = album.title;
        text.putExtra(TextSelectedActivity.EXTRA_TEXT, song);
//        text.putExtra(TextSelectedActivity.EXTRA_SINGER, str);
        startActivity(text);
    }

    private static final String TAG = "SingerSelected";
}
