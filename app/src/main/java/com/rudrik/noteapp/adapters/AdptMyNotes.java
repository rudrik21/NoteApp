package com.rudrik.noteapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rudrik.noteapp.R;
import com.rudrik.noteapp.activities.NotesActivity;
import com.rudrik.noteapp.models.Folder;
import com.rudrik.noteapp.models.Note;
import com.tr4android.recyclerviewslideitem.SwipeAdapter;
import com.tr4android.recyclerviewslideitem.SwipeConfiguration;

import java.util.ArrayList;
import java.util.List;

import static com.rudrik.noteapp.MyApplication.SEL_FOLDER;
import static com.rudrik.noteapp.MyApplication.bg;
import static com.rudrik.noteapp.MyApplication.db;

public class AdptMyNotes extends SwipeAdapter {

    Context context;
    List<Note> list;

    public AdptMyNotes(Context context, List<Note> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyNoteViewHolder onCreateSwipeViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_folder, parent, true);

        return new MyNoteViewHolder(v);
    }

    @Override
    public void onBindSwipeViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {
        if (!list.isEmpty()) {
            if (viewHolder instanceof MyNoteViewHolder) {


                MyNoteViewHolder vh = (MyNoteViewHolder) viewHolder;

                Note note = list.get(pos);

                vh.cardNote.setOnClickListener(v -> {
                    Intent i = new Intent(context, NotesActivity.class);
                    i.putExtra(SEL_FOLDER, list.get(pos));
                    context.startActivity(i);
                    ((Activity)context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                });

                vh.tvNoteName.setText(note.getTitle());

                List<Note> notes = new ArrayList<>();
                bg.execute(() -> {
                    notes.addAll(db.notesDao().getFolderNotes(note.getId()));
                });

                vh.tvTotalNotes.setText(String.valueOf(notes.size()));
            }
        }
    }

    @Override
    public int getItemCount() {
//        Log.e("LIST SIZE: ", String.valueOf(list.size()));
        return list.size();
    }

    @Override
    public SwipeConfiguration onCreateSwipeConfiguration(Context context, int position) {
        Interpolator interpolator = new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                return 2;
            }
        };

        if (!list.isEmpty()) {
            return new SwipeConfiguration.Builder(context)
                    .setRightDrawableResource(R.drawable.ic_search)
                    .setLeftDrawableResource(R.drawable.ic_delete)
                    .setSwipeBehaviour(10, null)
                    .build();
        }
        return null;
    }

    @Override
    public void onSwipe(int position, int direction) {
        if (!list.isEmpty()) {
            if (direction == -1) {
                //  delete
                System.out.println("delete");
                bg.execute(() -> {
                    db.notesDao().deleteNote(list.get(position));
                });
            }

            if (direction == 1) {
                //  edit
//                Intent i = new Intent(context, MapsActivity.class);
//                String strPlace = new Gson().toJson(myFolders.get(position));
//                i.putExtra("PLACE", strPlace);
//                context.startActivity(i);


            }
        }
    }


    class MyNoteViewHolder extends RecyclerView.ViewHolder {

        CardView cardNote;
        TextView tvNoteName;
        TextView tvTotalNotes;

        public MyNoteViewHolder(@NonNull View iv) {
            super(iv);

            cardNote = (CardView) iv.findViewById(R.id.cardFolder);
            tvNoteName = (TextView) iv.findViewById(R.id.tvFolderName);
            tvTotalNotes = (TextView) iv.findViewById(R.id.tvTotalNotes);
        }

    }
}

