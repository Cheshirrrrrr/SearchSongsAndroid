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

public class SingerRecyclerAdapter extends RecyclerView.Adapter<SingerRecyclerAdapter.ViewHolder>
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album album = ArrayOfAlbums.getAlbum(position);
        Log.w("AlbumsAsyncTask", "We're trying to bind");
        holder.sNameView.setText(album.title);
        holder.itemView.setTag(R.id.tag, album);
        Log.w("AlbumsAsyncTask", "We're trying to bind" + album.title);
    }

    @Override
    public int getItemCount() {
        return ArrayOfAlbums.getCount();
    }

    @Override
    public void onClick(View v) {
        Album album = (Album) v.getTag(R.id.tag);
        if (albumSelectedListener != null && album!= null) {
            albumSelectedListener.onAlbumSelected(album);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView sNameView;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.w("AlbumsAsyncTask", "We're trying to create holder");
            sNameView = (TextView) itemView.findViewById(R.id.album_name);
        }
    }
}
