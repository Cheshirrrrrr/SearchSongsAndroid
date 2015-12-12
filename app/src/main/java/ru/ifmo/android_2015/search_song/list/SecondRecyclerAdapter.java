package ru.ifmo.android_2015.search_song.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.R;
import ru.ifmo.android_2015.search_song.model.ArrayOfSongs2;
import ru.ifmo.android_2015.search_song.model.Tracks;

/**
 * Created by vorona on 12.12.15.
 */
public class SecondRecyclerAdapter extends RecyclerView.Adapter<SecondRecyclerAdapter.GroupsViewHolder>
        implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private SecondSelectedListener songSelectedListener;

    public SecondRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setSongSelectedListener(SecondSelectedListener listener) {
        songSelectedListener = listener;
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        view.setOnClickListener(this);
        return new GroupsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder holder, int position) {
        Tracks track = ArrayOfSongs2.getSong(position);
        Log.w("SingerAsyncTask", "We're trying to bind");
        holder.SongName.setText(track.artist + " - " + track.title );
        holder.itemView.setTag(R.id.tag, track); //TODO
    }

    @Override
    public int getItemCount() {
        return ArrayOfSongs2.getCount();
    }

    @Override
    public void onClick(View v) {
        Tracks track = (Tracks) v.getTag(R.id.tag);
        if (songSelectedListener != null) {
            songSelectedListener.onSongSelected(track);
        }
    }

    static class GroupsViewHolder extends RecyclerView.ViewHolder {
        TextView SongName;

        public GroupsViewHolder(View itemView) {
            super(itemView);
            Log.w("SingerAsyncTask", "We're trying to create holder" + R.id.txtSongName);
            SongName = (TextView) itemView.findViewById(R.id.album_name);
        }
    }
}