package com.rudrik.noteapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.rudrik.noteapp.R;
import com.rudrik.noteapp.adapters.AdptAudioSpeedDial;
import com.rudrik.noteapp.adapters.AdptImagesSpeedDial;
import com.rudrik.noteapp.models.Folder;

import org.jetbrains.annotations.NotNull;

import uk.co.markormesher.android_fab.FloatingActionButton;
import uk.co.markormesher.android_fab.SpeedDialMenuOpenListener;

import static com.rudrik.noteapp.MyApplication.SEL_FOLDER;
import static com.rudrik.noteapp.activities.NotesActivity.REQ_CODE_NOTE;

public class AddNoteActivity extends AppCompatActivity implements SpeedDialMenuOpenListener {

    private Folder selFolder;

    private AppBarLayout appBar;
    private MaterialToolbar toolbar;
    private EditText edtSearch;
    private FloatingActionButton fabAudioAction;
    private FloatingActionButton fabImageAction;
    private FloatingActionButton fabDirectionAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        checkFolder();
        init();
    }

    private void init() {
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        edtSearch = (EditText) findViewById(R.id.edtSearch);

        fabAudioAction = (FloatingActionButton) findViewById(R.id.fab_audio_action);
        fabImageAction = (FloatingActionButton) findViewById(R.id.fab_image_action);
        fabDirectionAction = (FloatingActionButton) findViewById(R.id.fab_direction_action);

        toolbar.setTitle("Add new Note");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtSearch.setVisibility(View.GONE);

        fabAudioAction.setOnSpeedDialMenuOpenListener(this);
        fabAudioAction.setSpeedDialMenuAdapter(new AdptAudioSpeedDial(this));

        fabImageAction.setOnSpeedDialMenuOpenListener(this);
        fabImageAction.setSpeedDialMenuAdapter(new AdptImagesSpeedDial(this));

        fabDirectionAction.setOnClickListener(v -> {
            // on direction click
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            overridePendingTransition(0,0);
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkFolder() {
        selFolder = (Folder) getIntent().getSerializableExtra(SEL_FOLDER);
        if (selFolder != null){
            System.out.println("from folder : " + selFolder.getfName());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (selFolder != null) {
            Intent intent = new Intent();
            intent.putExtra(SEL_FOLDER, selFolder);
            setResult(REQ_CODE_NOTE, intent);
        }
    }

    @Override
    public void onOpen(@NotNull FloatingActionButton fab) {
        switch (fab.getId()){
            case R.id.fab_image_action:
                System.out.println("fab_image_action");
                break;

            case R.id.fab_audio_action:
                System.out.println("fab_audio_action");
                break;

            case R.id.fab_direction_action:
                System.out.println("fab_direction_action");
                break;
        }
    }
}
