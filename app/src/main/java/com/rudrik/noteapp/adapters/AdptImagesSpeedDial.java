package com.rudrik.noteapp.adapters;

import android.content.Context;
import android.widget.LinearLayout;

import com.rudrik.noteapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import uk.co.markormesher.android_fab.SpeedDialMenuAdapter;
import uk.co.markormesher.android_fab.SpeedDialMenuItem;

public class AdptImagesSpeedDial extends SpeedDialMenuAdapter {

    Context context;
    List<SpeedDialMenuItem> list = new ArrayList<>();
    public AdptImagesSpeedDial(Context context) {
        this.context = context;

        //  0
        SpeedDialMenuItem camera = new SpeedDialMenuItem(context);
        camera.setIcon(R.drawable.ic_camera);
        camera.setLabel("Camera");

        //  1
        SpeedDialMenuItem gallery = new SpeedDialMenuItem(context);
        gallery.setIcon(R.drawable.ic_image);
        gallery.setLabel("Gallery");

        //  2
        SpeedDialMenuItem images = new SpeedDialMenuItem(context);
        images.setIcon(R.drawable.ic_file);
        images.setLabel("Images");

        list.add(camera);
        list.add(gallery);
        list.add(images);
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
                //  camera

                break;

            case 1 :
                //  gallery

                break;

            case 2:
                //  images

                break;
        }

        return super.onMenuItemClick(position);
    }
}
