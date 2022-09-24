package de.jrgreen.trainerapp.recycler.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.jrgreen.trainerapp.object.FeedbackList;
import de.jrgreen.trainerapp.object.Settings;
import de.jrgreen.trainerapp.listener.OnRatingChangeListener;
import de.jrgreen.trainerapp.object.Rating;
import de.jrgreen.trainerapp.object.SelectedRatings;
import de.jrgreen.trainerapp.object.Topic;
import de.jrgreen.trainerapp.object.Trainee;
import de.jrgreen.trainerapp.recycler.viewHolder.TopicViewHolder;
import de.jrgreen.trainerapp.R;

public class TopicAdapter extends RecyclerView.Adapter<TopicViewHolder> {
    private SelectedRatings selectedRatings;
    private Trainee selectedTrainee;

    private OnRatingChangeListener ratingChangeListener;

    public TopicAdapter(SelectedRatings selectedRatings, Trainee selectedTrainee, OnRatingChangeListener ratingChangeListener) {
        super();
        this.selectedRatings = selectedRatings;
        this.selectedTrainee = selectedTrainee;
        this.ratingChangeListener = ratingChangeListener;
    }
    public TopicAdapter(SelectedRatings selectedRatings, Trainee selectedTrainee) {
        super();
        this.selectedRatings = selectedRatings;
        this.selectedTrainee = selectedTrainee;
        this.ratingChangeListener = null;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_card_view, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = Settings.get().topics.get(position);
        holder.getFeedbackTopicView().setText(topic.getText());
        if (selectedRatings.isEditMode()){
            holder.getFeedbackRadioGroup().setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int rating;

                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.feedback_happy:
                            rating = 1;
                            break;
                        case R.id.feedback_middle:
                            rating = 2;
                            break;
                        case R.id.feedback_sad:
                            rating = 3;
                            break;
                        default:
                            rating = -1;
                    }
                    ratingChangeListener.onRatingChanged(new Rating(topic.get_short(), rating));

                    int previous_rating = rating;
                    if (FeedbackList.getLastFeedback(selectedTrainee) != null){
                        previous_rating = FeedbackList.getLastFeedback(selectedTrainee).getRatings().get(holder.getAdapterPosition()).getRating();
                    }

                    if (rating < previous_rating){
                        holder.getStonksView().setVisibility(View.VISIBLE);
                        holder.getStonksView().setImageResource(R.drawable.ic_baseline_trending_up_24);
                    } else if (rating == previous_rating){
                        holder.getStonksView().setVisibility(View.INVISIBLE);
                    } else if (rating > previous_rating){
                        holder.getStonksView().setVisibility(View.VISIBLE);
                        holder.getStonksView().setImageResource(R.drawable.ic_baseline_trending_down_24);
                    }

                    if (!selectedRatings.isEmpty() && selectedRatings.size() > holder.getAdapterPosition()) {
                        selectedRatings.set(holder.getAdapterPosition(), new Rating(Settings.get().topics.get(holder.getAdapterPosition()).get_short(), rating));
                    } else {
                        selectedRatings.add(holder.getAdapterPosition(), new Rating(Settings.get().topics.get(holder.getAdapterPosition()).get_short(), rating));
                    }
                }
            });
        }
        else {
            if (selectedRatings.get(holder.getAdapterPosition()) != null) {
                int rating = selectedRatings.get(holder.getAdapterPosition()).getRating();
                int previous_rating = FeedbackList.getPreviousFeedback(selectedTrainee).getRatings().get(holder.getAdapterPosition()).getRating();
                switch (rating) {
                    case 1:
                        holder.getFeedbackRadioGroup().check(R.id.feedback_happy);
                        break;
                    case 2:
                        holder.getFeedbackRadioGroup().check(R.id.feedback_middle);
                        break;
                    case 3:
                        holder.getFeedbackRadioGroup().check(R.id.feedback_sad);
                        break;
                    default:
                        break;
                }
                holder.getFeedbackRadioGroup().getChildAt(0).setEnabled(false);
                holder.getFeedbackRadioGroup().getChildAt(1).setEnabled(false);
                holder.getFeedbackRadioGroup().getChildAt(2).setEnabled(false);

                if (rating < previous_rating){
                    holder.getStonksView().setVisibility(View.VISIBLE);
                    holder.getStonksView().setImageResource(R.drawable.ic_baseline_trending_up_24);
                } else if (rating == previous_rating){
                    holder.getStonksView().setVisibility(View.INVISIBLE);
                } else if (rating > previous_rating){
                    holder.getStonksView().setVisibility(View.VISIBLE);
                    holder.getStonksView().setImageResource(R.drawable.ic_baseline_trending_down_24);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return Settings.get().topicCount;
    }
}
