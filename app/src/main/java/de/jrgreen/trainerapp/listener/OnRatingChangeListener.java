package de.jrgreen.trainerapp.listener;

import java.util.EventListener;

import de.jrgreen.trainerapp.object.Rating;

public interface OnRatingChangeListener extends EventListener {
    public void onRatingChanged(Rating rating);
}
