package com.rudrik.noteapp.adapters;

import android.content.Context;

import com.rudrik.noteapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import uk.co.markormesher.android_fab.SpeedDialMenuAdapter;
import uk.co.markormesher.android_fab.SpeedDialMenuItem;

public class AdptAudioSpeedDial extends SpeedDialMenuAdapter {

    Context context;
    List<SpeedDialMenuItem> list = new ArrayList<>();

    public AdptAudioSpeedDial(Context context) {
        this.context = context;

        //  0
        SpeedDialMenuItem mic = new SpeedDialMenuItem(context);
        mic.setIcon(R.drawable.ic_mic);
        mic.setLabel("Record");

        //  1
        SpeedDialMenuItem tracks = new SpeedDialMenuItem(context);
        tracks.setIcon(R.drawable.ic_file);
        tracks.setLabel("Tracks");

        list.add(mic);
        list.add(tracks);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NotNull
    @Override
    public SpeedDialMenuItem getMenuItem(@NotNull Context context, int i) {
        return list.get(i);
    }

    @Override
    public boolean onMenuItemClick(int position) {

        switch (position){
            case 0 :
                //  mic

                break;

            case 1 :
                //  tracks

                break;
        }

        return super.onMenuItemClick(position);
    }
}
