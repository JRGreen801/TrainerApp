package de.jrgreen.trainerapp.ui.main.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import de.jrgreen.trainerapp.R;
import de.jrgreen.trainerapp.object.Feedback;
import de.jrgreen.trainerapp.object.SelectedRatings;
import de.jrgreen.trainerapp.object.Trainee;
import de.jrgreen.trainerapp.object.Trainer;
import de.jrgreen.trainerapp.recycler.adapter.DetailAdapter;
import de.jrgreen.trainerapp.recycler.adapter.TopicAdapter;

public class InspectFeedbackFragment extends Fragment {

    private TextView traineeNameTextView;
    private RecyclerView topicView;
    private RecyclerView detailView;
    private Button submitButton;

    private View freetextLayout;
    private TextView freetextTextView;

    private SelectedRatings selectedRatings;
    private String freetextFeedback;
    private Trainer selectedTrainer;
    private Trainee selectedTrainee;
    private String date;

    public InspectFeedbackFragment(String date, Trainer trainer, Trainee trainee, SelectedRatings selected, String freetextFeedback){
        selectedTrainer = trainer;
        selectedTrainee = trainee;
        selectedRatings = selected;
        this.date = date;
        this.freetextFeedback = freetextFeedback;
    }
    public InspectFeedbackFragment(Feedback feedback){
        date = feedback.getDate();
        selectedTrainer = feedback.getTrainer();
        selectedTrainee = feedback.getTrainee();
        selectedRatings = new SelectedRatings(feedback.getRatings(), false);
        freetextFeedback = feedback.getFreeform();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inspect_feedback, container, false);

        TextView toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("View Feedback");

        traineeNameTextView = view.findViewById(R.id.trainee_name_textView);
        traineeNameTextView.setText(selectedTrainee.getName());

        topicView = view.findViewById(R.id.feedbackRecycler);
        topicView.setLayoutManager(new LinearLayoutManager(this.getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        topicView.setAdapter(new TopicAdapter(selectedRatings, selectedTrainee));

        detailView = view.findViewById(R.id.detailRecycler);
        detailView.setLayoutManager(new LinearLayoutManager(this.getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        detailView.setAdapter(new DetailAdapter(selectedRatings));
        detailView.setHasFixedSize(true);

        freetextLayout = view.findViewById(R.id.freetext_feedback_layout);
        freetextTextView = view.findViewById(R.id.freetext_textView);
        if (!StringUtils.isEmpty(freetextFeedback)){
            freetextTextView.setText(freetextFeedback);
            freetextLayout.setVisibility(View.VISIBLE);
        } else {
            freetextLayout.setVisibility(View.GONE);
        }

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("SELEC", selectedTrainer.getName() + " : " + selectedTrainee.getName());
                getParentFragmentManager().beginTransaction().replace(container.getId(), new SelectionFragment(selectedTrainer, selectedTrainee)).commitNow();
            }
        });
        return view;
    }

}