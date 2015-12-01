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

public class SingerRecyclerAdapter extends RecyclerView.Adapter<SingerRecyclerAdapter.CityViewHolder>
        implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private AlbumSelectedListener albumSelectedListener;

    public SingerRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setSingerSelectedListener(AlbumSelectedListener listener) {
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
        Log.w("AlbumsAsyncTask", "We're trying to bind");
        holder.cityNameView.setText(album.title);
        holder.itemView.setTag(R.id.tag_city, album);
        Log.w("AlbumsAsyncTask", "We're trying to bind" + album.title);
    }

    @Override
    public int getItemCount() {
        return ArrayOfAlbums.getCount();
    }

    @Override
    public void onClick(View v) {
        Album album = (Album) v.getTag(R.id.tag_city);
        if (albumSelectedListener != null && album!= null) {
            albumSelectedListener.onAlbumSelected(album);
        }
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {
        final TextView cityNameView;

        public CityViewHolder(View itemView) {
            super(itemView);
            Log.w("AlbumsAsyncTask", "We're trying to create holder");
            cityNameView = (TextView) itemView.findViewById(R.id.album_name);
        }
    }
}
