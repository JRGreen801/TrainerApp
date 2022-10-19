package de.jrgreen.trainerapp.ui.main.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

        setButtonsEnabled(buttonsEnabled, true);

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Password required:");
        View viewInfalted = LayoutInflater.from(getContext()).inflate(R.layout.freetext_input_card_view, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInfalted.findViewById(R.id.freeform_feedback_editText);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(viewInfalted);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if (input.getText().toString().equals("picnicrt")) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameContainer, new SelectionFragment())
                            .commit();
                }
                input.setText("");
                if (viewInfalted.getParent() != null) {
                    ((ViewGroup) viewInfalted.getParent()).removeView(viewInfalted);
                }
            }
        });
        feedbackIOButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
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

    public boolean checkPassword(){
        return false;
    }

    public void setButtonsEnabled(boolean enabled, boolean debug){
        feedbackIOButton.setEnabled(enabled);
        trainerManualButton.setEnabled(!debug); //<-- DISABLED WHILE BUILDING FEATURE
    }
}