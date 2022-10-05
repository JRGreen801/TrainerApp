package de.jrgreen.trainerapp.ui.main.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import de.jrgreen.trainerapp.helper.FileHelper;
import de.jrgreen.trainerapp.helper.SheetHelper;
import de.jrgreen.trainerapp.listener.OnRatingChangeListener;
import de.jrgreen.trainerapp.object.Feedback;
import de.jrgreen.trainerapp.object.FeedbackList;
import de.jrgreen.trainerapp.object.Rating;
import de.jrgreen.trainerapp.object.SelectedRatings;
import de.jrgreen.trainerapp.object.Trainee;
import de.jrgreen.trainerapp.object.Trainer;
import de.jrgreen.trainerapp.recycler.adapter.DetailAdapter;
import de.jrgreen.trainerapp.recycler.adapter.TopicAdapter;
import de.jrgreen.trainerapp.R;
import de.jrgreen.trainerapp.ui.main.MainActivity;
import de.jrgreen.trainerapp.ui.main.MainViewModel;

public class AddFeedbackFragment extends Fragment {

    private MainViewModel mViewModel;

    private TextView traineeNameTextView;
    private RecyclerView topicView;
    private RecyclerView detailView;
    private Button submitButton;

    private int validCounter;

    private SelectedRatings selectedRatings = new SelectedRatings(true, true);
    private Trainer selectedTrainer;
    private Trainee selectedTrainee;
    private String date;

    public AddFeedbackFragment(Trainer trainer, Trainee trainee){
        selectedTrainer = trainer;
        selectedTrainee = trainee;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_feedback, container, false);

        TextView toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Add Feedback");

        ProgressBar progressBar = getActivity().findViewById(R.id.progressBar);

        traineeNameTextView = view.findViewById(R.id.trainee_name_textView);
        traineeNameTextView.setText(selectedTrainee.getName());

        detailView = view.findViewById(R.id.detailRecycler);
        detailView.setLayoutManager(new LinearLayoutManager(this.getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        detailView.setAdapter(new DetailAdapter(selectedRatings));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Was genau ist schiefgelaufen?");
        View viewInfalted = LayoutInflater.from(getContext()).inflate(R.layout.freetext_input_card_view, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInfalted.findViewById(R.id.freeform_feedback_editText);
        builder.setView(viewInfalted);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

                SheetHelper.postFeedback(getActivity(), new Feedback(date, selectedTrainee, selectedTrainer, selectedRatings,input.getText().toString()), progressBar);

                if (viewInfalted.getParent() != null){
                    ((ViewGroup)viewInfalted.getParent()).removeView(viewInfalted);
                }
                getParentFragmentManager().beginTransaction().replace(container.getId(), new SelectionFragment(selectedTrainer, selectedTrainee)).commitNow();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                if (viewInfalted.getParent() != null){
                    ((ViewGroup)viewInfalted.getParent()).removeView(viewInfalted);
                }
            }
        });

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isBad = false;
                if (selectedRatings != null) {
                    for (Rating r :
                            selectedRatings) {
                        if (r.getRating() > 2) {
                            isBad = true;
                        }
                    }
                }
                if (isBad){
                    builder.show();
                }else {
                    date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                    FileHelper.saveToJson(getContext(),
                        new Feedback(date, selectedTrainee, selectedTrainer, selectedRatings, input.getText().toString()));
                    FeedbackList.get().add(new Feedback(date, selectedTrainee, selectedTrainer, selectedRatings, input.getText().toString()));
                    SheetHelper.postFeedback(getActivity(), new Feedback(date, selectedTrainee, selectedTrainer, selectedRatings,input.getText().toString()), progressBar);

                    getParentFragmentManager().beginTransaction().replace(container.getId(), new SelectionFragment(selectedTrainer, selectedTrainee)).commitNow();
                }

            }
        });

        validCounter = selectedRatings.getTopicRatings().size() +1;
        submitButton.setEnabled(false);

        topicView = view.findViewById(R.id.feedbackRecycler);
        topicView.setLayoutManager(new LinearLayoutManager(this.getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        ArrayList<Rating> ratings = new ArrayList<>();
        ratings.addAll(selectedRatings);
        topicView.setAdapter(new TopicAdapter(selectedRatings, selectedTrainee ,new OnRatingChangeListener() {
            @Override
            public void onRatingChanged(Rating rating) {
                for (Iterator<Rating> iterator = ratings.iterator();iterator.hasNext();) {
                    Rating value = iterator.next();
                    if (value.get_short() == rating.get_short() && rating.getRating() != -1){
                        iterator.remove();
                        validCounter--;
                    }
                }
                if (validCounter == 0){
                    submitButton.setEnabled(true);
                }
            }
        }));

        return view;
    }

}