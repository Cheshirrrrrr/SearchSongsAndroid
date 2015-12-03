package ru.ifmo.android_2015.search_song.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.R;
import ru.ifmo.android_2015.search_song.model.ArrayOfSongs;

/**
 * Created by vorona on 29.11.15.
 */
public class AlbumRecyclerAdapter extends RecyclerView.Adapter<AlbumRecyclerAdapter.CityViewHolder>
        implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private SingerSelectedListener citySelectedListener;

    public AlbumRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setCitySelectedListener(SingerSelectedListener listener) {
        citySelectedListener = listener;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_city, parent, false);
        view.setOnClickListener(this);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        String song = ArrayOfSongs.getSong(position);
//        Album album = ArrayOfAlbums.getAlbum(position);
        Log.w("SongAsyncTask", "We're trying to bind");
        holder.cityNameView.setText(song);
        holder.itemView.setTag(R.id.tag, song); //TODO
//        Log.w("SongAsyncTask", "We're trying to bind" + album.title);
    }

    @Override
    public int getItemCount() {
        return ArrayOfSongs.getCount();
    }

    @Override
    public void onClick(View v) {
        String song = (String) v.getTag(R.id.tag);
        if (citySelectedListener != null && song!= null) {
            citySelectedListener.onSingerSelected(song);
        }
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {
        final TextView cityNameView;

        public CityViewHolder(View itemView) {
            super(itemView);
            Log.w("SongAsyncTask", "We're trying to create holder");
            cityNameView = (TextView) itemView.findViewById(R.id.album_name);
        }
    }
}
