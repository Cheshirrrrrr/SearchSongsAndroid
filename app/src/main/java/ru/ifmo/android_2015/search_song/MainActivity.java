package ru.ifmo.android_2015.search_song;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.widg
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView search;
    private static final String TAG = "Main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = (TextView) findViewById(R.id.textSearch);
        search.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        search = (TextView) findViewById(R.id.textSearch);
        search.setText("");
    }

    public void onSingerClicked(View view) {
        Log.v(TAG, "onSingerClicked: " + view);
        Intent singer = new Intent(this, ChooseGroupActivity.class);
        String str = search.getText().toString();
        singer.putExtra(ChooseGroupActivity.EXTRA_SINGER, str);
        startActivity(singer);
    }

    public void onSongClicked(View view) {
        Log.v(TAG, "onSongClicked: ");
        Intent song = new Intent(this, SongSelectedActivity.class);
        String str = search.getText().toString();
        song.putExtra(SongSelectedActivity.EXTRA_SONG, str);
        startActivity(song);
    }

}
