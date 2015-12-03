package ru.ifmo.android_2015.search_song.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.ifmo.android_2015.search_song.R;
import ru.ifmo.android_2015.search_song.model.ArrayOfSingers;
import ru.ifmo.android_2015.search_song.model.Group;

/**
 * Created by vorona on 02.12.15.
 */
public class FirstRecyclerAdapter extends RecyclerView.Adapter<FirstRecyclerAdapter.GroupsViewHolder>
        implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private FirstSelectedListener singerSelectedListener;

    public FirstRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setSingerSelectedListener(FirstSelectedListener listener) {
        singerSelectedListener = listener;
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_city, parent, false);
        view.setOnClickListener(this);
        return new GroupsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder holder, int position) {
        Group group = ArrayOfSingers.getGroup(position);
        Log.w("SingerAsyncTask", "We're trying to bind");
        holder.groupName.setText(group.title);
        holder.itemView.setTag(R.id.tag, group); //TODO
//        Log.w("SongAsyncTask", "We're trying to bind" + album.title);
    }

    @Override
    public int getItemCount() {
        return ArrayOfSingers.getCount();
    }

    @Override
    public void onClick(View v) {
        Group group = (Group) v.getTag(R.id.tag);
        if (singerSelectedListener != null) {
            singerSelectedListener.onSingerSelected(group);
        }
    }

    static class GroupsViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;

        public GroupsViewHolder(View itemView) {
            super(itemView);
            Log.w("SingerAsyncTask", "We're trying to create holder" + R.id.txtWritten);
            groupName = (TextView) itemView.findViewById(R.id.album_name);
        }
    }
}