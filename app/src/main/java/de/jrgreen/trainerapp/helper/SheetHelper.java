package de.jrgreen.trainerapp.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import de.jrgreen.trainerapp.R;
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
import de.jrgreen.trainerapp.ui.main.MainActivity;
import de.jrgreen.trainerapp.ui.main.fragment.SelectionFragment;
import de.jrgreen.trainerapp.ui.main.fragment.SettingsFragment;

public class SheetHelper {
    /*
    * ADDED RequestQueue start and stop
    * SET Should Cache to FALSE
    *
    * MainActivity needs to do progressbar stuff
    * */

    public static void loadRunner(Context context, Runnable runnable){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://docs.google.com/spreadsheets/d/e/2PACX-1vTAa_8Q7SXb-E8ai0MtVCMNR3s1S_WAecJR7nyULdMNQv1Hc545TekTFO4VyW-uWjVEcG1ADFZfGCnA/pub?gid=0&single=true&output=csv", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                requestQueue.stop();
                RunnerList.set(CSVtoRunnerList(response));
                ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.execute(runnable);
                executor.shutdown();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                Log.e("ERROR", error.toString());
            }
        });

        int socketTimeOut = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        stringRequest.setShouldCache(false);
        requestQueue.start();
        requestQueue.add(stringRequest);
        return;
    }

    public static void requestJson(){
        JsonRequest<RunnerList> jsonRequest = new JsonRequest(Request.Method.GET, "", null, new Response.Listener<RunnerList>() {
            @Override
            public void onResponse(RunnerList response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                return null;
            }

            @Override
            public int compareTo(Object o) {
                return 0;
            }
        };
    }


    public static void loadFeedback(Context context, Runnable runnable, ProgressBar progressBar){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://docs.google.com/spreadsheets/d/e/2PACX-1vTxbmLB_U2SN5e8LEY5YHTQVWxgOiGlTw555MWTrC6uUgmX8raf4pP0WvXo1uFMFnz1T5uPKdnSwSa4/pub?gid=0&single=true&output=csv", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                requestQueue.stop();
                FeedbackList.set(CSVtoFeedbackList(response));
                ArrayList<Feedback> backup = FileHelper.convertJsonToFeedbackList(context);
                if (FeedbackList.get().size() == 0){
                   // FeedbackList.set(backup);
                    Log.e("FEEDBACK NULL", "true");
                } else {
                    Log.e("FEEDBACK NULL", "false");
                }
                ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.execute(runnable);
                executor.shutdown();
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                progressBar.setVisibility(View.GONE);
                Log.e("ERROR", error.toString());
            }
        });

        int socketTimeOut = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        stringRequest.setShouldCache(false);
        requestQueue.start();
        requestQueue.add(stringRequest);

        return;
    }

    public static ArrayList<Runner> CSVtoRunnerList(String csv){
        ArrayList<Runner> runners = new ArrayList<Runner>();
        ArrayList<String> runnersCSV = new ArrayList<>();

        runnersCSV.addAll(Arrays.asList(csv.split("\n")));
        runnersCSV.remove(0);
        for (String s:
             runnersCSV) {
            String[] runnerCut = s.split(",");
            byte[] bytes = runnerCut[0].getBytes(StandardCharsets.ISO_8859_1);
            String name = new String(bytes, StandardCharsets.UTF_8);
            String employee_ID = runnerCut[1];
            String role = runnerCut[2];
            Log.e("R", name + "/" + employee_ID + "/" + role);
            if (role.contains("Trainer")){
                runners.add(new Trainer(name, employee_ID));
                Log.e("TRAINER", name + ", " + employee_ID);
            } else if (role.contains("Trainee")){
                runners.add(new Trainee(name, employee_ID));
                Log.e("TRAINEE", name + ", " + employee_ID);
            }

        }

        return runners;
    }

    public static ArrayList<Feedback> CSVtoFeedbackList(String csv){
        ArrayList<Feedback> feedbacks = new ArrayList<>();
        ArrayList<String> feedbacksCSV = new ArrayList<>();
        ArrayList<String> ratingShorts = new ArrayList<>();
        Log.e("GET", csv);
        feedbacksCSV.addAll(Arrays.asList(csv.split("\n")));
        ratingShorts.addAll(Arrays.stream(feedbacksCSV.get(0).split(",")).collect(Collectors.toList()));
        Log.e("RATING SHORTS", ratingShorts.toString());
        ratingShorts.remove(0);
        ratingShorts.remove(0);
        ratingShorts.remove(0);
        ratingShorts.remove(0);
        Log.e("RATING SHORTS", ratingShorts.toString());

        feedbacksCSV.remove(0);
        for (String s:
             feedbacksCSV) {
            String[] feedbackCut = s.split(",");
            String date = feedbackCut[0];
            String trainee_id = feedbackCut[1];
            String trainer_id = feedbackCut[2];
            String freeForm = feedbackCut[3];

            String[] ratings = Arrays.copyOfRange(feedbackCut, 4, feedbackCut.length);
            ArrayList<Rating> ratingArrayList = new ArrayList<>();
            for (int i = 0; i < ratings.length; i++) {
                ratingArrayList.add(new Rating(ratingShorts.get(i), Integer.parseInt(ratings[i].trim())));
            }
            feedbacks.add(new Feedback(date, TraineeList.getTraineeByID(trainee_id), TrainerList.getTrainerByID(trainer_id), ratingArrayList, freeForm));
        }

        return feedbacks;
    }

    public static void postFeedback(Context context, Feedback feedback, ProgressBar progressBar){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbx3GEBuKi4ap44Oo-s0kcezPobey4dwdCcXksm6QzqZA2yZy0Pde_zMg6wLmGY5zrK3/exec",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("POST RESPONSE", response);
                    progressBar.setVisibility(View.GONE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("action", "addFeedback");
                params.put("date", feedback.getDate());
                params.put("trainee", feedback.getTrainee().getEmployee_ID());
                params.put("trainer", feedback.getTrainer().getEmployee_ID());
                params.put("freeform", feedback.getFreeform());
                params.put("tp", String.valueOf(feedback.getRatings().get(0).getRating()));
                params.put("ds", String.valueOf(feedback.getRatings().get(1).getRating()));
                params.put("dp", String.valueOf(feedback.getRatings().get(2).getRating()));
                params.put("cs", String.valueOf(feedback.getRatings().get(3).getRating()));
                params.put("ta", String.valueOf(feedback.getRatings().get(4).getRating()));
                params.put("fo", String.valueOf(feedback.getRatings().get(5).getRating()));
                params.put("st", String.valueOf(feedback.getRatings().get(6).getRating()));
                params.put("lt", String.valueOf(feedback.getRatings().get(7).getRating()));
                params.put("pf", String.valueOf(feedback.getRatings().get(8).getRating()));

                return params;
            }
        };

        int socketTimeOut = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        return;
    }

}
