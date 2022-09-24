package de.jrgreen.trainerapp.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import de.jrgreen.trainerapp.R;
import de.jrgreen.trainerapp.helper.FileHelper;
import de.jrgreen.trainerapp.helper.SheetHelper;
import de.jrgreen.trainerapp.object.Feedback;
import de.jrgreen.trainerapp.object.FeedbackList;
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
    private Runnable runnable1;

    private boolean MANUAL_DROPDOWN_UI = true; // true=DropDownUI false=ImageLinkUI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                READ_EXTERNAL_STORAGE_PERMISSION_CODE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        final int[] progress = {0};



        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                Log.e("STEP" ,"2");
                Log.e("FEEDBACK LENGTH", String.valueOf(FeedbackList.get().size()));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainer, new StartFragment())
                        .commit();
                }
        };

        runnable1 = new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setHomeButtonEnabled(false);
                TrainerList.set(RunnerList.getTrainers());
                TraineeList.set(RunnerList.getTrainees());
                Log.e("STEP" ,"1");
                SheetHelper.loadFeedback(MainActivity.this, runnable2, progressBar);
            }
        };

        SheetHelper.loadRunner(MainActivity.this , runnable1);


        ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Settings.set(FileHelper.convertJsonToSettings(MainActivity.this));
                //FeedbackList.set(FileHelper.convertJsonToFeedbackList(MainActivity.this));
            }
        });
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                    .replace(R.id.frameContainer, new StartFragment())
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
                    .replace(R.id.frameContainer, new StartFragment())
                    .commitNow();
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        FileHelper.backupToJson("feedback",FeedbackList.get());
    }

    @Override
    protected void onPause() {
        super.onPause();
       FileHelper.backupToJson("feedback",FeedbackList.get());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("BACKUP", String.valueOf(FileHelper.convertJsonToFeedbackList(MainActivity.this).size()));

    }
}