package com.rudrik.noteapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.rudrik.noteapp.MyApplication;
import com.rudrik.noteapp.R;
import com.rudrik.noteapp.adapters.AdptAudioSpeedDial;
import com.rudrik.noteapp.adapters.AdptImagesSpeedDial;
import com.rudrik.noteapp.models.Note;

import org.jetbrains.annotations.NotNull;

import uk.co.markormesher.android_fab.FloatingActionButton;
import uk.co.markormesher.android_fab.SpeedDialMenuOpenListener;

import static com.rudrik.noteapp.MyApplication.LOC_REQ_CODE;
import static com.rudrik.noteapp.MyApplication.SEL_NOTE;
import static com.rudrik.noteapp.MyApplication.prefs;
import static com.rudrik.noteapp.MyApplication.statusCheck;
import static com.rudrik.noteapp.activities.NotesActivity.REQ_CODE_NOTE;

public class AddNoteActivity extends AppCompatActivity implements SpeedDialMenuOpenListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private Note selNote;
    private LatLng userLocation = null;

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

        checkNote();

        edtSearch.setVisibility(View.GONE);

        fabAudioAction.setOnSpeedDialMenuOpenListener(this);
        fabAudioAction.setSpeedDialMenuAdapter(new AdptAudioSpeedDial(this));

        fabImageAction.setOnSpeedDialMenuOpenListener(this);
        fabImageAction.setSpeedDialMenuAdapter(new AdptImagesSpeedDial(this));

        fabDirectionAction.setOnClickListener(v -> {
            // on direction click
            Intent i = new Intent(this, MapsActivity.class);
            i.putExtra(SEL_NOTE, selNote);
            startActivity(i);
        });

        edtNtitle.setText(selNote.getTitle());
        edtNdesc.setText(selNote.getDesc());
    }

    @Override
    protected void onResume() {
        super.onResume();

        init();

        Log.e("LOCATION_SERVICE", String.valueOf(statusCheck(this)));

        if (selNote.getLat() == 0f || selNote.getLng() == 0f) {
            if (checkPermissions()) {
                MyApplication.initUserLocation(this);
            }
        }

        userLocation = new LatLng(MyApplication.getPrefs().getFloat("U_LAT", 0f), MyApplication.getPrefs().getFloat("U_LNG", 0f));
        if (selNote.getLat() == 0f) {
            selNote.setLat((float) userLocation.latitude);
        }
        if (selNote.getLng() == 0f) {
            selNote.setLng((float) userLocation.longitude);
        }
        prefs.registerOnSharedPreferenceChangeListener(this);
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
        if (selNote != null) {
            System.out.println("from note : " + selNote.getTitle());
            toolbar.setTitle("Edit Note");
        } else {
            selNote = new Note();
        }

    }

    @SuppressLint("NewApi")
    private boolean checkPermissions() {
        boolean isPermitted = (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED;
        if (!isPermitted) {
            requestPermission();
        }
        return isPermitted;
    }

    @SuppressLint("NewApi")
    private void requestPermission() {
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOC_REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            if (requestCode == LOC_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("Application", "Location permission has been granted");
            }

            if (requestCode == LOC_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                this.finish();
                overridePendingTransition(0, 0);
            }
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

        if (selNote.getTitle().isEmpty() && selNote.getDesc().isEmpty()) {
            if (selNote.getTitle().isEmpty()) {
                edtNtitle.setError("No title!");
            }
            if (selNote.getDesc().isEmpty()) {
                edtNdesc.setError("No description!");
            }

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

        } else {
            Intent intent = new Intent();
            intent.putExtra(SEL_NOTE, selNote);
            setResult(REQ_CODE_NOTE, intent);
            super.onBackPressed();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("U_LAT") || key.equals("U_LNG")) {
            userLocation = new LatLng(prefs.getFloat("U_LAT", 0f), prefs.getFloat("U_LNG", 0f));
            if (selNote.getLat() == 0f) {
                selNote.setLat((float) userLocation.latitude);
            }
            if (selNote.getLng() == 0f) {
                selNote.setLng((float) userLocation.longitude);
            }
            Log.e("USER_LOCATION", userLocation.toString());
        }
    }
}
