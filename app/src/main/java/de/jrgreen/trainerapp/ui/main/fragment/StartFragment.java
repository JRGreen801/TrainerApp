package de.jrgreen.trainerapp.ui.main.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.jrgreen.trainerapp.R;

public class StartFragment extends Fragment {


    public StartFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_start, container, false);

        Button feedbackIOButton = view.findViewById(R.id.feedback_io_button);
        Button trainerManualButton = view.findViewById(R.id.trainer_manual_button);

        feedbackIOButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainer, new SelectionFragment())
                        .commit();
            }
        });
        trainerManualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainer, new TrainerManualFragment())
                        .commit();
            }
        });

        return view;
    }
}