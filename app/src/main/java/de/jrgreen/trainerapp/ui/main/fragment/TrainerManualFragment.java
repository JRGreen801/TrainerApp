package de.jrgreen.trainerapp.ui.main.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.jrgreen.trainerapp.R;

public class TrainerManualFragment extends Fragment {

    public static boolean DROPDOWN_UI = true;

    public TrainerManualFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (DROPDOWN_UI){
           return buildDropDownUI(inflater, container);
        }
        else {
            return buildImageLinkUI(inflater, container);
        }

    }

    private View buildImageLinkUI(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_trainer_manual_imagelink_ui, container, false);
        return view;
    }

    private View buildDropDownUI(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_trainer_manual_dropdown_ui, container, false);

        View training_manual_dropButton = view.findViewById(R.id.training_manual_dropButton);
        ImageView training_manual_dropImg = view.findViewById(R.id.training_manual_Img);
        View training_manual_container = view.findViewById(R.id.training_manual_layout);

        View topic_manual_dropButton = view.findViewById(R.id.topic_manual_dropButton);
        ImageView topic_manual_dropImg = view.findViewById(R.id.topic_manual_Img);
        View topic_manual_container = view.findViewById(R.id.topic_manual_layout);

        View detail_manual_dropButton = view.findViewById(R.id.detail_manual_dropButton);
        ImageView detail_manual_dropImg = view.findViewById(R.id.detail_manual_Img);
        View detail_manual_container = view.findViewById(R.id.detail_manual_layout);

        training_manual_dropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (training_manual_container.getVisibility() == View.GONE){
                    training_manual_dropImg.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    training_manual_container.setVisibility(View.VISIBLE);
                } else {
                    training_manual_dropImg.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    training_manual_container.setVisibility(View.GONE);
                }
            }
        });

        topic_manual_dropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topic_manual_container.getVisibility() == View.GONE){
                    topic_manual_dropImg.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    topic_manual_container.setVisibility(View.VISIBLE);
                } else {
                    topic_manual_dropImg.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    topic_manual_container.setVisibility(View.GONE);
                }
            }
        });

        detail_manual_dropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail_manual_container.getVisibility() == View.GONE){
                    detail_manual_dropImg.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    detail_manual_container.setVisibility(View.VISIBLE);
                } else {
                    detail_manual_dropImg.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    detail_manual_container.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }
}
