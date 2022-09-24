package de.jrgreen.trainerapp.object;

import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

public class FeedbackList extends ArrayList<Feedback> {
    private static ArrayList<Feedback> instance;

    private FeedbackList(){instance = new ArrayList<Feedback>();}

    public static ArrayList<Feedback> get() {
        if (instance != null) {
            return instance;
        }
        instance = new ArrayList<Feedback>();
        return instance;
    }

    public static void set(ArrayList<Feedback> feebackList){
        instance = feebackList;
    }

    public static int getShiftCountOfTrainee(Trainee trainee){
        int shiftCount = 0;
        for (Feedback f:
             instance) {
            if (f.getTrainee() != null && Objects.equals(f.getTrainee().getEmployee_ID(), trainee.getEmployee_ID())){
                Log.e("TRAINEE CHECK", f.getTrainee().getEmployee_ID() + " =?= " + trainee.getEmployee_ID());
                shiftCount++;
            }
        }
        return shiftCount;
    }

    public static Trainer getTrainerOfLastShift(Trainee trainee){
        Trainer trainer = new Trainer("NULL", "NULL");
        for (Feedback f:
             instance) {
            if (f.getTrainee() != null && Objects.equals(f.getTrainee().getEmployee_ID(), trainee.getEmployee_ID())){
                if (f.getTrainer() != null) {
                    trainer = f.getTrainer();
                }
            }
        }
        return trainer;
    }

    public static Feedback getLastFeedback(Trainee trainee){
        Feedback feedback = null;
        for (Feedback f:
                instance) {
            if (f.getTrainee() != null && Objects.equals(f.getTrainee().getEmployee_ID(), trainee.getEmployee_ID())) {
                feedback =f;
            }
        }
        return feedback;
    }

    public static Feedback getPreviousFeedback(Trainee trainee){
        ArrayList<Feedback> lastFeedbacks = new ArrayList<>();
        Feedback previousFeedback = null;
        for (Feedback f:
                instance) {
            if (f.getTrainee() != null && Objects.equals(f.getTrainee().getEmployee_ID(), trainee.getEmployee_ID())) {
                lastFeedbacks.add(f);
            }
        }
        for (Feedback f:
                lastFeedbacks) {
            if (f.getTrainee() != null && Objects.equals(f.getTrainee().getEmployee_ID(), trainee.getEmployee_ID())) {
                previousFeedback = f;
            }
        }
        return previousFeedback;
    }
}
