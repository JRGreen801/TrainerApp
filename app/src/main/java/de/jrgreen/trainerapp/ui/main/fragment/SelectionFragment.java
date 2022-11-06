package de.jrgreen.trainerapp.ui.main.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.jrgreen.trainerapp.R;
import de.jrgreen.trainerapp.helper.FileHelper;
import de.jrgreen.trainerapp.helper.FireStoreHelper;
import de.jrgreen.trainerapp.helper.SheetHelper;
import de.jrgreen.trainerapp.listener.OnRequestResultListener;
import de.jrgreen.trainerapp.object.Feedback;
import de.jrgreen.trainerapp.object.FeedbackList;
import de.jrgreen.trainerapp.object.RunnerList;
import de.jrgreen.trainerapp.object.Trainee;
import de.jrgreen.trainerapp.object.TraineeList;
import de.jrgreen.trainerapp.object.Trainer;
import de.jrgreen.trainerapp.object.TrainerList;
import de.jrgreen.trainerapp.recycler.adapter.NothingSelectedSpinnerAdapter;

public class SelectionFragment extends Fragment {

    private View selectionDetailLayout;
    private Spinner trainerSpinner;
    private Spinner traineeSpinner;
    private Button addTrainerButton;
    private Button addTraineeButton;

    private TextView shiftCounterTextview;
    private TextView lastShiftWithTextView;
    private TextView lastShiftWithText;
    private Button addFeedbackButton;
    private Button inspectFeedbackButton;
    private ImageView peterView;
    private Button finalizeTrainingButton;

    private Feedback last_feedback;

    private Trainer lastTrainer;
    private Trainee lastTrainee;


    public SelectionFragment() {
        lastTrainer = null;
        lastTrainee = null;
    }

    public SelectionFragment(Trainer trainer, Trainee trainee) {
        lastTrainer = trainer;
        lastTrainee = trainee;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_selection, container, false);

        TextView toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.app_name);


        selectionDetailLayout = view.findViewById(R.id.selection_detail_layout);
        peterView = view.findViewById(R.id.peter_view);

        if (lastTrainer == null || lastTrainee == null){
            selectionDetailLayout.setVisibility(View.GONE);
            peterView.setVisibility(View.VISIBLE);
        }

        /**
         Trainer Spinner init
         */
        trainerSpinner = view.findViewById(R.id.trainer_select_spinner);
        ArrayAdapter<Trainer> trainerArrayAdapter = new ArrayAdapter<Trainer>(getContext(), android.R.layout.simple_spinner_dropdown_item);
        NothingSelectedSpinnerAdapter trainerAdapter = new NothingSelectedSpinnerAdapter(trainerArrayAdapter, R.layout.trainer_spinner_row_nothing_selected, getContext());
        trainerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trainerSpinner.setAdapter(trainerAdapter);
        trainerArrayAdapter.addAll(TrainerList.get());

        if (lastTrainer != null){
            trainerSpinner.setSelection(
                    TrainerList.getIndexByID(lastTrainer.getEmployee_ID())+1
            );
        }

        trainerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                trainerSpinner.setVisibility(View.VISIBLE);
                addTrainerButton.setText("Add");

                if (adapterView.getSelectedItem() != null){
                    addFeedbackButton.setVisibility(View.VISIBLE);
                } else {
                    addFeedbackButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /**
         Trainee Spinner init
         */
        traineeSpinner = view.findViewById(R.id.trainee_select_spinner);
        ArrayAdapter<Trainee> traineeArrayAdapter = new ArrayAdapter<Trainee>(getContext(), android.R.layout.simple_spinner_dropdown_item);
        NothingSelectedSpinnerAdapter traineeAdapter = new NothingSelectedSpinnerAdapter(traineeArrayAdapter, R.layout.trainee_spinner_row_nothing_selected, getContext());
        traineeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        traineeSpinner.setAdapter(traineeAdapter);
        traineeArrayAdapter.addAll(TraineeList.get());

        /**
         Trainer AddButton init
         */
        AlertDialog.Builder trainerInputBuilder = new AlertDialog.Builder(getContext());
        trainerInputBuilder.setTitle(R.string.trainer_input_header);
        View trainerViewInflated = LayoutInflater.from(getContext()).inflate(R.layout.runner_input_card_view, (ViewGroup) getView(), false);
        final EditText trainerNameInput = (EditText) trainerViewInflated.findViewById(R.id.name_editText);
        final EditText trainerEmployee_ID_Input = (EditText) trainerViewInflated.findViewById(R.id.employee_id_editText);
        trainerInputBuilder.setView(trainerViewInflated);
        trainerInputBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                Trainer newTrainer = new Trainer(trainerNameInput.getText().toString(), trainerEmployee_ID_Input.getText().toString().toUpperCase());
                FireStoreHelper.addRunner(getActivity(), newTrainer, new OnRequestResultListener() {
                    @Override
                    public void onResult(String response) {}

                    @Override
                    public void onResult(boolean response) {
                        if (response){
                            TrainerList.get().add(newTrainer);
                            trainerArrayAdapter.add(newTrainer);
                            trainerArrayAdapter.notifyDataSetChanged();
                            trainerSpinner.setVisibility(View.VISIBLE);
                            addTrainerButton.setText("Add");
                            trainerSpinner.setSelection(TrainerList.getIndexByID(newTrainer.getEmployee_ID())+1);
                        }else {
                            Toast.makeText(getContext(), "failed to add Trainer", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                if (trainerViewInflated.getParent() != null){
                    ((ViewGroup)trainerViewInflated.getParent()).removeView(trainerViewInflated);
                }
            }
        });
        trainerInputBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

                if (trainerViewInflated.getParent() != null) {
                    ((ViewGroup) trainerViewInflated.getParent()).removeView(trainerViewInflated);
                }
            }
        });

        addTrainerButton = view.findViewById(R.id.add_trainer_button);
        addTrainerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trainerInputBuilder.show();
            }
        });

        /**
         Trainee AddButton init
         */
        addTraineeButton = view.findViewById(R.id.add_trainee_button);
        AlertDialog.Builder traineeInputBuilder = new AlertDialog.Builder(getContext());
        traineeInputBuilder.setTitle(R.string.trainee_input_header);
        View traineeViewInfalted = LayoutInflater.from(getContext()).inflate(R.layout.runner_input_card_view, (ViewGroup) getView(), false);
        final EditText traineeNameInput = (EditText) traineeViewInfalted.findViewById(R.id.name_editText);
        final EditText traineeEmployee_ID_Input = (EditText) traineeViewInfalted.findViewById(R.id.employee_id_editText);
        traineeInputBuilder.setView(traineeViewInfalted);
        traineeInputBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                Trainee newTrainee = new Trainee(traineeNameInput.getText().toString(), traineeEmployee_ID_Input.getText().toString().toUpperCase());
                FireStoreHelper.addRunner(getActivity(), newTrainee, new OnRequestResultListener() {
                    @Override
                    public void onResult(String response) {}

                    @Override
                    public void onResult(boolean response) {
                        if (response){
                            TraineeList.get().add(newTrainee);
                            traineeArrayAdapter.add(newTrainee);
                            traineeArrayAdapter.notifyDataSetChanged();
                            traineeSpinner.setVisibility(View.VISIBLE);
                            addTraineeButton.setText("Add");
                            traineeSpinner.setSelection(TraineeList.getIndexByID(newTrainee.getEmployee_ID())+1);
                        }else {
                            Toast.makeText(getContext(), "failed to add Trainee", Toast.LENGTH_LONG).show();
                        }
                        finalizeTrainingButton.setVisibility(View.GONE);
                    }
                });
                if (traineeViewInfalted.getParent() != null){
                    ((ViewGroup)traineeViewInfalted.getParent()).removeView(traineeViewInfalted);
                }
            }
        });
        traineeInputBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

                if (traineeViewInfalted.getParent() != null) {
                    ((ViewGroup) traineeViewInfalted.getParent()).removeView(traineeViewInfalted);
                }
            }
        });

        addTraineeButton = view.findViewById(R.id.add_trainee_button);
        addTraineeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                traineeInputBuilder.show();
            }
        });


        shiftCounterTextview = view.findViewById(R.id.shiftCounter_textView);
        lastShiftWithTextView = view.findViewById(R.id.last_with_textview);
        lastShiftWithText = view.findViewById(R.id.last_with_text);

        addFeedbackButton = view.findViewById(R.id.add_feedback_button);
        inspectFeedbackButton = view.findViewById(R.id.inspect_feedback_button);

        finalizeTrainingButton = view.findViewById(R.id.finalize_training_button);

        if (TrainerList.get().isEmpty()){
            trainerSpinner.setVisibility(View.GONE);
            addTrainerButton.setText(R.string.button_add_trainer);
        }
        if (TraineeList.get().isEmpty()){
            selectionDetailLayout.setVisibility(View.GONE);
            peterView.setVisibility(View.VISIBLE);
            traineeSpinner.setVisibility(View.GONE);
            addTraineeButton.setText(R.string.button_add_trainee);
        }

        if (lastTrainee != null){
            traineeSpinner.setSelection(
                    TraineeList.getIndexByID(lastTrainee.getEmployee_ID())+1
            );
        }
        traineeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItem() != null) {
                    selectionDetailLayout.setVisibility(View.VISIBLE);
                    peterView.setVisibility(View.GONE);

                    int count = FeedbackList.getShiftCountOfTrainee((Trainee) traineeSpinner.getItemAtPosition(i));
                    shiftCounterTextview.setText(Integer.toString(count));

                    if (count >= 6){
                        finalizeTrainingButton.setVisibility(View.VISIBLE);
                    } else {
                        finalizeTrainingButton.setVisibility(View.GONE);
                    }

                    last_feedback = FeedbackList.getLastFeedback(((Trainee) traineeSpinner.getItemAtPosition(i)));
                    if (last_feedback != null) {
                        lastShiftWithTextView.setText(last_feedback.getTrainer().getName());
                        lastShiftWithTextView.setVisibility(View.VISIBLE);
                        lastShiftWithText.setVisibility(View.VISIBLE);
                        inspectFeedbackButton.setVisibility(View.VISIBLE);
                    } else {
                        lastShiftWithTextView.setVisibility(View.GONE);
                        lastShiftWithText.setVisibility(View.GONE);

                        inspectFeedbackButton.setVisibility(View.GONE);
                    }
                }
                else {
                    selectionDetailLayout.setVisibility(View.GONE);
                    peterView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectionDetailLayout.setVisibility(View.GONE);
            }
        });

        addFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trainerSpinner.getSelectedItem() != null){
                    getParentFragmentManager().beginTransaction().replace(container.getId(),
                        new AddFeedbackFragment((Trainer) trainerSpinner.getSelectedItem(), (Trainee) traineeSpinner.getSelectedItem())).commitNow();
                }
            }
        });
        inspectFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (traineeSpinner.getSelectedItem() != null){
                    getParentFragmentManager().beginTransaction().replace(container.getId(),
                        new InspectFeedbackFragment(last_feedback)).commitNow();
                }
            }
        });

        //init finalize Button
        AlertDialog.Builder finalizeBuilder = new AlertDialog.Builder(getContext());
        finalizeBuilder.setTitle(R.string.finalize_check_header);
        View finalizeViewInfalted = LayoutInflater.from(getContext()).inflate(R.layout.finalize_dialog_layout, (ViewGroup) getView(), false);
        final TextView finalizeDialogTextView = (TextView) traineeViewInfalted.findViewById(R.id.finalize_dialog_textView);
        finalizeBuilder.setView(finalizeViewInfalted);
        finalizeBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                FireStoreHelper.removeRunner(getActivity(), (Trainee) traineeSpinner.getSelectedItem(), new OnRequestResultListener() {
                    @Override
                    public void onResult(String response) {}

                    @Override
                    public void onResult(boolean response) {
                        if (response){
                            TraineeList.get().remove(traineeSpinner.getSelectedItem());
                            traineeSpinner.setSelection(0);
                            traineeArrayAdapter.remove((Trainee) traineeSpinner.getSelectedItem());
                            traineeArrayAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "failed to delete Trainee", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                FireStoreHelper.removeFeedback(getActivity(), (Trainee) traineeSpinner.getSelectedItem(), new OnRequestResultListener() {
                    @Override
                    public void onResult(String response) {}

                    @Override
                    public void onResult(boolean response) {
                        if (response){
                            for (Feedback f : FeedbackList.get()){
                                if (f.getTrainee().equals(traineeSpinner.getSelectedItem())){
                                    FeedbackList.get().remove(f);
                                }
                            }
                            traineeSpinner.setSelection(0);
                        } else {
                            Toast.makeText(getContext(), "failed to delete Feedback", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                if (finalizeViewInfalted.getParent() != null) {
                    ((ViewGroup) finalizeViewInfalted.getParent()).removeView(finalizeViewInfalted);
                }
            }
        });
        finalizeBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if (finalizeViewInfalted.getParent() != null) {
                    ((ViewGroup) finalizeViewInfalted.getParent()).removeView(finalizeViewInfalted);
                }
            }
        });

        finalizeTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizeBuilder.show();
            }
        });

        return view;
    }
}