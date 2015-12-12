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
public class AlbumRecyclerAdapter extends RecyclerView.Adapter<AlbumRecyclerAdapter.ViewHolder>
        implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private SingerSelectedListener singerSelectedListener;

    public AlbumRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setSelectedListener(SingerSelectedListener listener) {
        singerSelectedListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String song = ArrayOfSongs.getSong(position);
        Log.w("SongAsyncTask", "We're trying to bind");
        holder.nNameView.setText(song);
        holder.itemView.setTag(R.id.tag, song); //TODO
    }

    @Override
    public int getItemCount() {
        return ArrayOfSongs.getCount();
    }

    @Override
    public void onClick(View v) {
        String song = (String) v.getTag(R.id.tag);
        if (singerSelectedListener != null && song!= null) {
            singerSelectedListener.onSingerSelected(song);
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
