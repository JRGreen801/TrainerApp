package de.jrgreen.trainerapp.ui.main.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.jrgreen.trainerapp.R;

public class StartFragment extends Fragment {

    private Button feedbackIOButton;
    private Button trainerManualButton;

    private boolean buttonsEnabled;

    public StartFragment(boolean enabled) {
        buttonsEnabled = enabled;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_start, container, false);

        feedbackIOButton = view.findViewById(R.id.feedback_io_button);
        trainerManualButton = view.findViewById(R.id.trainer_manual_button);

        setButtonsEnabled(buttonsEnabled);

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

    public void setButtonsEnabled(boolean enabled){
        feedbackIOButton.setEnabled(enabled);
        trainerManualButton.setEnabled(enabled);
    }
}