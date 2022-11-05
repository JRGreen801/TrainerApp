package de.jrgreen.trainerapp.helper;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.jrgreen.trainerapp.object.Feedback;
import de.jrgreen.trainerapp.object.Runner;
import de.jrgreen.trainerapp.object.RunnerList;
import de.jrgreen.trainerapp.object.TraineeList;
import de.jrgreen.trainerapp.object.TrainerList;

public class FireStoreHelper {
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static Future<ArrayList<Runner>> getRunners(Activity activity){

        return executor.submit(() -> {
            if (auth.getCurrentUser() == null){
                auth(activity);
            }

            ArrayList<Runner> runners = new ArrayList<>();

            db.collection("runners").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.e("FIREBASE OUTPUT", document.getId() + " => " + document.getData());
                            runners.add(Runner.fromMap(document.getData()));
                        }
                        RunnerList.set(runners);
                        TrainerList.set(RunnerList.getTrainers());
                        TraineeList.set(RunnerList.getTrainees());
                    } else {
                        Toast.makeText(activity, "Error getting data from Firebase: " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            return runners;
        });
    }

    public static Future<ArrayList<Feedback>> getFeedback(Activity activity){
        return executor.submit(() -> {
            if (auth.getCurrentUser() == null){
                auth(activity);
            }

            ArrayList<Feedback> feedbacks = new ArrayList<>();

            db.collection("runners").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.e("FIREBASE OUTPUT", document.getId() + " => " + document.getData());
                            feedbacks.add();
                        }
                    } else {
                        Toast.makeText(activity, "Error getting data from Firebase: " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            return feedbacks;
        });
    }

    private static void auth(Activity activity){
        auth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("FIREBASE AUTH", "signInAnonymously:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Log.d("FIREBASE AUTH", String.valueOf(user));
                        } else {
                            Toast.makeText(activity, "Authentication failed: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
