package com.rudrik.noteapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.rudrik.noteapp.R;
import com.rudrik.noteapp.adapters.AdptAudioSpeedDial;
import com.rudrik.noteapp.adapters.AdptImagesSpeedDial;
import com.rudrik.noteapp.models.Folder;
import com.rudrik.noteapp.models.Note;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import uk.co.markormesher.android_fab.FloatingActionButton;
import uk.co.markormesher.android_fab.SpeedDialMenuOpenListener;

import static com.rudrik.noteapp.MyApplication.SEL_FOLDER;
import static com.rudrik.noteapp.MyApplication.SEL_NOTE;
import static com.rudrik.noteapp.MyApplication.db;
import static com.rudrik.noteapp.MyApplication.getStrToday;
import static com.rudrik.noteapp.MyApplication.getToday;
import static com.rudrik.noteapp.MyApplication.main;
import static com.rudrik.noteapp.activities.NotesActivity.REQ_CODE_NOTE;

public class AddNoteActivity extends AppCompatActivity implements SpeedDialMenuOpenListener {

    private Note selNote;

    private AppBarLayout appBar;
    private MaterialToolbar toolbar;
    private EditText edtSearch;

    private TextInputEditText edtNtitle;
    private AppCompatMultiAutoCompleteTextView edtNdesc;

    private FloatingActionButton fabAudioAction;
    private FloatingActionButton fabImageAction;
    private FloatingActionButton fabDirectionAction;

    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        checkNote();
        init();
    }

    private void init() {
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        edtSearch = (EditText) findViewById(R.id.edtSearch);

        edtNtitle = (TextInputEditText) findViewById(R.id.edtNtitle);
        edtNdesc = (AppCompatMultiAutoCompleteTextView) findViewById(R.id.edtNdesc);

        fabAudioAction = (FloatingActionButton) findViewById(R.id.fab_audio_action);
        fabImageAction = (FloatingActionButton) findViewById(R.id.fab_image_action);
        fabDirectionAction = (FloatingActionButton) findViewById(R.id.fab_direction_action);

        toolbar.setTitle("Add new Note");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtSearch.setVisibility(View.GONE);

        edtNtitle.setText(selNote.getTitle());
        edtNdesc.setText(selNote.getDesc());

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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            overridePendingTransition(0, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkNote() {
        selNote = (Note) getIntent().getSerializableExtra(SEL_NOTE);
        if (selNote != null){
            System.out.println("from note : " + selNote.getTitle());
        }else{
            selNote = new Note();
        }

    }

    @Override
    public void onOpen(@NotNull FloatingActionButton fab) {
        switch (fab.getId()) {
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

    @Override
    public void onBackPressed() {
        selNote.setTitle(edtNtitle.getText().toString());
        selNote.setDesc(edtNdesc.getText().toString());

        if (selNote.getTitle().isEmpty() && selNote.getDesc().isEmpty()){
            if (selNote.getTitle().isEmpty()){
                edtNtitle.setError("No title!");
            }
            if (selNote.getDesc().isEmpty()){
                edtNdesc.setError("No description!");
            }

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

        }else{
            Intent intent = new Intent();
            intent.putExtra(SEL_NOTE, selNote);
            setResult(REQ_CODE_NOTE, intent);
            super.onBackPressed();
        }
    }
}
