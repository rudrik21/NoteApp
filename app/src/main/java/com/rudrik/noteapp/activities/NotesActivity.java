package com.rudrik.noteapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rudrik.noteapp.MyApplication;
import com.rudrik.noteapp.R;
import com.rudrik.noteapp.adapters.AdptMyNotes;
import com.rudrik.noteapp.models.Folder;

import java.util.ArrayList;

import static com.rudrik.noteapp.MyApplication.NOTES_DATA_CHANGES;
import static com.rudrik.noteapp.MyApplication.SEL_FOLDER;
import static com.rudrik.noteapp.MyApplication.editor;
import static com.rudrik.noteapp.MyApplication.prefs;
import static com.rudrik.noteapp.models.Note.myNotes;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    public static int REQ_CODE_NOTE = 10;

    private AppBarLayout appBar;
    private MaterialToolbar toolbar;
    private EditText edtSearch;
    private RecyclerView recyclerNotes;
    private FloatingActionButton fabAdd;

    private Folder selFolder;
    private AdptMyNotes adpt = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        init();
        checkFolder();
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myNotes.clear();
        if (selFolder != null) {
            MyApplication.noteUpdates(selFolder.getId());
        }
    }

    private void checkFolder() {
        selFolder = (Folder) getIntent().getSerializableExtra(SEL_FOLDER);
        if (selFolder != null){
            System.out.println("from folder : " + selFolder.getfName());
        }
    }

    private void init() {
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        recyclerNotes = (RecyclerView) findViewById(R.id.recyclerView);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);

        toolbar.setTitle("Notes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adpt = new AdptMyNotes(this, new ArrayList<>());
        recyclerNotes.setHasFixedSize(true);
        recyclerNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerNotes.setAdapter(adpt);

        fabAdd.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            overridePendingTransition(0,0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add) {
            Intent i = new Intent(this, AddNoteActivity.class);
            i.putExtra(SEL_FOLDER, selFolder);
            startActivityForResult(i, REQ_CODE_NOTE);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ON_ACTIVITY_RES", "TRUE");

//        if (requestCode == REQ_CODE_NOTE) {
//            if (data != null) {
//                selFolder = (Folder) data.getSerializableExtra(SEL_FOLDER);
//            }
//
//            if (selFolder != null){
//                System.out.println("from folder : " + selFolder.getfName());
//            }
//        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if (key.equals(NOTES_DATA_CHANGES)) {
            if (preferences.getBoolean(NOTES_DATA_CHANGES, false)) {
                Log.e("HAS_DATA_CHANGES", String.valueOf(preferences.getBoolean(NOTES_DATA_CHANGES, false)));
                if (adpt != null) {
                    recyclerNotes.swapAdapter(new AdptMyNotes(this, myNotes), true);

                    editor.putBoolean(NOTES_DATA_CHANGES, false);
                    editor.apply();
                }
            }
        }
    }
}
