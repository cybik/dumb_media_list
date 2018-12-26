package com.example.renaud.demos.abstracted;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.renaud.demos.R;

import java.util.ArrayList;
import java.util.List;

public class SongDB extends RecyclerView.Adapter<SongDB.SongDBViewHolder>{

    private static SongDB sInstance;
    private List<Song> mDataSet;

    private final String selection = MediaStore.Audio.Media.IS_MUSIC + " = 1";
    private final String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");
    private final String[] selectionArgsMp3 = new String[]{ mimeType };

    private SongDB(Activity mainActivity) {
        // populate mDataSet

        mDataSet = new ArrayList<Song>();

        Refresh(mainActivity);
    }

    public void Refresh(Activity mainActivity) {
        if(mDataSet.size() != 0) {
            mDataSet.clear();
        }
        mDataSet = new ArrayList<Song>();
        Cursor cur = mainActivity.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                null,
                null
        );
        if(cur != null && cur.moveToFirst()) {
            int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
            int numberCol = cur.getColumnIndex(MediaStore.Audio.Media.TRACK);
            while(cur.moveToNext()) {
                mDataSet.add(new Song(
                        cur.getString(titleColumn),
                        cur.getString(albumColumn),
                        cur.getInt(numberCol)
                ));
            }
        }
        notifyDataSetChanged();
    }

    public static SongDB getInstance(Activity mainActivity) {
        if(sInstance == null) {
            sInstance = new SongDB(mainActivity);
        }

        return sInstance;
    }

    public static class SongDBViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llayoutRv;
        public SongDBViewHolder(@NonNull LinearLayout that) {
            super(that);
            llayoutRv = that;
        }
        protected void setSong(Song song) {
            setArtist(song.artist);
            setTitle(song.title);
        }

        protected void setArtist(String artist) {
            ((TextView)llayoutRv.findViewById(R.id.tvArtist)).setText(artist);
        }
        protected void setTitle(String title) {
            ((TextView)llayoutRv.findViewById(R.id.tvTitle)).setText(title);
        }
    }

    @NonNull
    @Override
    public SongDBViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout theLayout =
                (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                                             .inflate(
                                                 R.layout.song_item, viewGroup,false
                                             );

        SongDBViewHolder svh = new SongDBViewHolder(theLayout);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SongDBViewHolder holder, int i) {
        holder.setSong(mDataSet.get(i));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}