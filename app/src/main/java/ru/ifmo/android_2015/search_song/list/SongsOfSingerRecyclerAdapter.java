package ru.ifmo.android_2015.search_song.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.R;
import ru.ifmo.android_2015.search_song.SongsOfSingerAsyncTask;
import ru.ifmo.android_2015.search_song.model.Track;

/**
 * Created by vorona on 29.11.15.
 */
public class SongsOfSingerRecyclerAdapter extends RecyclerView.Adapter<SongsOfSingerRecyclerAdapter.ViewHolder>
        implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private SongSelectedListener songSelectedListener;

    public SongsOfSingerRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setSelectedListener(SongSelectedListener listener) {
        songSelectedListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Track song = SongsOfSingerAsyncTask.getSongInPosition(position);
        Log.w("SongAsyncTask", "We're trying to bind");
        holder.nNameView.setText(song.title);
        holder.itemView.setTag(R.id.tag, song); //TODO
    }

    @Override
    public int getItemCount() {
        return SongsOfSingerAsyncTask.numberOfSongs();
    }

    @Override
    public void onClick(View v) {
        Track song = (Track) v.getTag(R.id.tag);
        if (songSelectedListener != null && song!= null) {
            songSelectedListener.onSongSelected(song);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nNameView;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.w("SongAsyncTask", "We're trying to create holder");
            nNameView = (TextView) itemView.findViewById(R.id.album_name);
        }
    }
}
