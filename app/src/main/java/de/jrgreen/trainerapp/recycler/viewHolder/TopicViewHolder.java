package de.jrgreen.trainerapp.recycler.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.jrgreen.trainerapp.R;

public class TopicViewHolder extends RecyclerView.ViewHolder {

    private TextView feedbackTopicView;
    private RadioGroup feedbackRadioGroup;
    private ImageView stonksView;

    public TopicViewHolder(@NonNull View itemView) {
        super(itemView);
        feedbackTopicView = itemView.findViewById(R.id.feedbackTopicView);
        feedbackRadioGroup = itemView.findViewById(R.id.feedbackRadioGroup);
        stonksView = itemView.findViewById(R.id.stonks_image);
    }

    public TextView getFeedbackTopicView() {
        return feedbackTopicView;
    }
    public RadioGroup getFeedbackRadioGroup() {
        return feedbackRadioGroup;
    }
    public ImageView getStonksView() {
        return stonksView;
    }
}
