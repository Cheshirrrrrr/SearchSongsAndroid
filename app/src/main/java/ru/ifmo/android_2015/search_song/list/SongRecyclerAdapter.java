package ru.ifmo.android_2015.search_song.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.R;
import ru.ifmo.android_2015.search_song.model.Album;
import ru.ifmo.android_2015.search_song.model.ArrayOfAlbums;

/**
 * Created by vorona on 01.12.15.
 */
public class SongRecyclerAdapter extends RecyclerView.Adapter<SongRecyclerAdapter.CityViewHolder>
        implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private SongSelectedListener albumSelectedListener;

    public SongRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setSongRecyclerAdapter(SongSelectedListener listener) {
        albumSelectedListener = listener;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_city, parent, false);
        view.setOnClickListener(this);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        Album album = ArrayOfAlbums.getAlbum(position);
        Log.w("TextAsyncTask", "We're trying to bind");
        holder.cityNameView.setText(album.title);
        holder.itemView.setTag(R.id.tag_city, album);
        Log.w("TextAsyncTask", "We're trying to bind" + album.title);
    }

    @Override
    public int getItemCount() {
        return ArrayOfAlbums.getCount();
    }

    @Override
    public void onClick(View v) {
        String song = (String) v.getTag(R.id.tag_city);
        if (albumSelectedListener != null && song!= null) {
            albumSelectedListener.onSongSelected(song);
        }
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {
        final TextView cityNameView;

        public CityViewHolder(View itemView) {
            super(itemView);
            Log.w("TextAsyncTask", "We're trying to create holder");
            cityNameView = (TextView) itemView.findViewById(R.id.txtName);
        }
    }
}

