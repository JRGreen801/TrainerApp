package de.jrgreen.trainerapp.listener;

public interface OnRequestResultListener {
    void onResult(String response);
    void onResult(boolean response);
}
