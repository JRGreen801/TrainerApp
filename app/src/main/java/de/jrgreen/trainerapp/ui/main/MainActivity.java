package de.jrgreen.trainerapp.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.DialogCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import de.jrgreen.trainerapp.R;
import de.jrgreen.trainerapp.helper.FileHelper;
import de.jrgreen.trainerapp.helper.FireStoreHelper;
import de.jrgreen.trainerapp.helper.SheetHelper;
import de.jrgreen.trainerapp.listener.OnRequestResultListener;
import de.jrgreen.trainerapp.object.Feedback;
import de.jrgreen.trainerapp.object.FeedbackList;
import de.jrgreen.trainerapp.object.Runner;
import de.jrgreen.trainerapp.object.RunnerList;
import de.jrgreen.trainerapp.object.Settings;
import de.jrgreen.trainerapp.object.TraineeList;
import de.jrgreen.trainerapp.object.TrainerList;
import de.jrgreen.trainerapp.ui.main.fragment.AddFeedbackFragment;
import de.jrgreen.trainerapp.ui.main.fragment.InspectFeedbackFragment;
import de.jrgreen.trainerapp.ui.main.fragment.SelectionFragment;
import de.jrgreen.trainerapp.ui.main.fragment.SettingsFragment;
import de.jrgreen.trainerapp.ui.main.fragment.StartFragment;
import de.jrgreen.trainerapp.ui.main.fragment.TrainerManualFragment;

public class MainActivity extends AppCompatActivity {

    private final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 23;
    public ProgressBar progressBar;

    private boolean MANUAL_DROPDOWN_UI = true; // true=DropDownUI false=ImageLinkUI


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                READ_EXTERNAL_STORAGE_PERMISSION_CODE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer, new StartFragment(false))
                .commit();

        ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.execute(() -> {
            Settings.set(FileHelper.convertJsonToSettings(MainActivity.this));
        });
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FireStoreHelper.getRunners(this, new OnRequestResultListener() {
            @Override
            public void onResult(String response) {}

            @Override
            public void onResult(boolean response) {
                if (response) {
                    FireStoreHelper.getFeedback(getParent(), new OnRequestResultListener() {
                        @Override
                        public void onResult(String response) {
                        }

                        @Override
                        public void onResult(boolean response) {
                            if (response){
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frameContainer, new StartFragment(true))
                                        .commit();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Fragment f;
        switch (item.getItemId()){
            case R.id.menu_settings_item:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainer, new SettingsFragment())
                        .commit();
                break;
            case R.id.menu_clear_trainers_item:
                FileHelper.clearFIle("trainers");
                TrainerList.get().clear();
                f = getSupportFragmentManager().findFragmentById(R.id.frameContainer);
                if (f instanceof SelectionFragment){
                    getSupportFragmentManager().beginTransaction().detach(f).commitNow();
                    getSupportFragmentManager().beginTransaction().attach(f).commitNow();
                }
                break;
            case R.id.menu_clear_trainees_item:
                FileHelper.clearFIle("trainees");
                TraineeList.get().clear();
                f = getSupportFragmentManager().findFragmentById(R.id.frameContainer);
                if (f instanceof SelectionFragment){
                    getSupportFragmentManager().beginTransaction().detach(f).commitNow();
                    getSupportFragmentManager().beginTransaction().attach(f).commitNow();
                }
                break;
            case R.id.menu_clear_feedback_item:
                FileHelper.clearFIle("feedback");
                FeedbackList.get().clear();
                break;
            case R.id.menu_swap_manual_ui:
                TrainerManualFragment.DROPDOWN_UI = !TrainerManualFragment.DROPDOWN_UI;
                f = getSupportFragmentManager().findFragmentById(R.id.frameContainer);
                if (f instanceof TrainerManualFragment){
                    getSupportFragmentManager().beginTransaction().detach(f).commitNow();
                    getSupportFragmentManager().beginTransaction().attach(f).commitNow();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameContainer);
        if (f == null) {
            finish();
        } else if (f instanceof StartFragment){
            finish();
        } else if (f instanceof SelectionFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameContainer, new StartFragment(true))
                    .commitNow();
        } else if (f instanceof InspectFeedbackFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameContainer, new SelectionFragment())
                    .commitNow();
        } else if (f instanceof AddFeedbackFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameContainer, new SelectionFragment())
                    .commitNow();
        } else if (f instanceof SettingsFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameContainer, new SelectionFragment())
                    .commitNow();
        } else if (f instanceof TrainerManualFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameContainer, new StartFragment(true))
                    .commitNow();
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}