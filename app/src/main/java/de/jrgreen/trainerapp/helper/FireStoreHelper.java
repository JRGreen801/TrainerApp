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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.jrgreen.trainerapp.listener.OnRequestResultListener;
import de.jrgreen.trainerapp.object.Feedback;
import de.jrgreen.trainerapp.object.FeedbackList;
import de.jrgreen.trainerapp.object.Rating;
import de.jrgreen.trainerapp.object.Runner;
import de.jrgreen.trainerapp.object.RunnerList;
import de.jrgreen.trainerapp.object.Settings;
import de.jrgreen.trainerapp.object.Trainee;
import de.jrgreen.trainerapp.object.TraineeList;
import de.jrgreen.trainerapp.object.Trainer;
import de.jrgreen.trainerapp.object.TrainerList;

public class FireStoreHelper {
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void getRunners(Activity activity, OnRequestResultListener onRequestResultListener){
            auth(activity);

            ArrayList<Runner> runners = new ArrayList<>();

            db.collection("runners").whereEqualTo("hub", Settings.get().hub).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    onRequestResultListener.onResult(task.isSuccessful());
                }
            });
    }

    public static void getFeedback(Activity activity, OnRequestResultListener onRequestResultListener){
            auth(activity);

            ArrayList<Feedback> feedbacks = new ArrayList<>();

            db.collection("feedback").whereEqualTo("hub", Settings.get().hub).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.e("FIREBASE OUTPUT", document.getId() + " => " + document.getData());
                            try{ feedbacks.add(Feedback.fromMap(document.getData()));
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        FeedbackList.set(feedbacks);
                    } else {
                        Toast.makeText(activity, "Error getting data from Firebase: " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                    onRequestResultListener.onResult(task.isSuccessful());
                }
            });
    }

    public static void addRunner(Activity activity, Runner runner, OnRequestResultListener onRequestResultListener){
        auth(activity);

        Map<String, Object> data = new HashMap<>();
        data.put("name", runner.getName());
        data.put("employee_id", runner.getEmployee_ID());
        if (runner instanceof Trainer){
            data.put("role", "trainer");
        }
        if (runner instanceof Trainee){
            data.put("role", "trainee");
        }
        data.put("hub", Settings.get().hub);

        db.collection("runners").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    onRequestResultListener.onResult(true);
                }else {
                    onRequestResultListener.onResult(false);
                }
            }
        });
    }

    public static void addFeedback(Activity activity, Feedback feedback, OnRequestResultListener onRequestResultListener){
        auth(activity);

        Map<String, Object> data = new HashMap<>();
        data.put("date", feedback.getDate());
        data.put("trainer_id", feedback.getTrainer().getEmployee_ID());
        data.put("trainee_id", feedback.getTrainee().getEmployee_ID());
        for (Rating r : feedback.getRatings()){
            data.put(r._short, r.rating);
        }
        data.put("freeform", feedback.getFreeform());
        data.put("hub", Settings.get().hub);

        db.collection("feedback").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    onRequestResultListener.onResult(true);
                }
                else {
                    onRequestResultListener.onResult(false);
                }
            }
        });
    }

    public static void removeRunner(Activity activity, Runner runner, OnRequestResultListener onRequestResultListener){
        auth(activity);

        db.collection("runners").whereEqualTo("employee_id", runner.getEmployee_ID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(DocumentSnapshot ds : task.getResult().getDocuments()){
                        ds.getReference().delete();
                    }
                    onRequestResultListener.onResult(true);
                } else  {
                    onRequestResultListener.onResult(false);
                }
            }
        });
    }

    public static void removeFeedback(Activity activity, Runner runner, OnRequestResultListener onRequestResultListener){
        auth(activity);

        db.collection("feedback").whereEqualTo("trainee_id", runner.getEmployee_ID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(DocumentSnapshot ds : task.getResult().getDocuments()){
                        ds.getReference().delete();
                    }
                    onRequestResultListener.onResult(true);
                } else  {
                    onRequestResultListener.onResult(false);
                }
            }
        });
    }

    private static void auth(Activity activity){
        if (auth.getCurrentUser() == null) {
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
}
