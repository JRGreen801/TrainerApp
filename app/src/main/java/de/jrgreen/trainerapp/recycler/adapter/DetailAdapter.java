package de.jrgreen.trainerapp.recycler.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.jrgreen.trainerapp.R;
import de.jrgreen.trainerapp.helper.FileHelper;
import de.jrgreen.trainerapp.object.Feedback;
import de.jrgreen.trainerapp.object.FeedbackList;
import de.jrgreen.trainerapp.object.Settings;
import de.jrgreen.trainerapp.object.Rating;
import de.jrgreen.trainerapp.object.SelectedRatings;
import de.jrgreen.trainerapp.recycler.viewHolder.DetailViewHolder;
import de.jrgreen.trainerapp.ui.main.fragment.SelectionFragment;

public class DetailAdapter extends RecyclerView.Adapter<DetailViewHolder> {
    private SelectedRatings selectedRatings;
    private boolean isValid;

    public DetailAdapter(SelectedRatings selectedRatings){
        this.selectedRatings = selectedRatings;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_card_view, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        int offsetedPosition = Settings.get().topicCount + position;
        holder.getDetailText().setText(Settings.get().details.get(position).getText());

        switch (Settings.get().details.get(position).get_short()){
            case "ST":
                holder.getImageView().setImageResource(R.mipmap.split_tote_01);
                break;
            case "LT":
                holder.getImageView().setImageResource(R.mipmap.logtag_01);
                break;
            case "PF":
                holder.getImageView().setImageResource(R.mipmap.return_free_bottles_01);
                break;
            case "FO":
                holder.getImageView().setImageResource(R.mipmap.delivery);
                break;
            default:
                holder.getImageView().setImageResource(R.drawable.ic_rt_appicon);
                break;
        }

        // < ONCLICK CONTENT IN DEVELOPMENT > //
//        holder.getImageView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                View viewInfalted = LayoutInflater.from(view.getContext()).inflate(R.layout.detail_image_dialog_layout, (ViewGroup) view.getParent(), false);
//                final ImageView detailImage = viewInfalted.findViewById(R.id.detail_dialog_imageView);
//                final TextView detailTextView = viewInfalted.findViewById(R.id.detail_dialog_textView);
//                detailImage.setImageDrawable(holder.getImageView().getDrawable());
//                detailTextView.setText("Dies ist ein beschreibender Text mit Absätzen\n\nUm zu Testen wie das ganze Aussieht und ob man das so machen kann\n\n" +
//                                       "Dies ist ein beschreibender Text mit Absätzen\n\nUm zu Testen wie das ganze Aussieht und ob man das so machen kann\n\n" +
//                                       "Dies ist ein beschreibender Text mit Absätzen\n\nUm zu Testen wie das ganze Aussieht und ob man das so machen kann\n\n");
//                builder.setTitle(holder.getDetailText().getText());
//                builder.setView(viewInfalted);
//                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                        if (viewInfalted.getParent() != null){
//                            ((ViewGroup)viewInfalted.getParent()).removeView(viewInfalted);
//                        }
//                    }
//                });
//                builder.show();
//            }
//      });

        if (selectedRatings.isEditMode()) {
            holder.getDetailCheckBox().setEnabled(true);
            holder.getDetailCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    short rating;
                    if (compoundButton.isChecked()) {
                        rating = 1;
                    } else {
                        rating = -1;
                    }
                    if (!selectedRatings.isEmpty() && selectedRatings.size() > offsetedPosition) {
                        selectedRatings.set(offsetedPosition, new Rating(Settings.get().details.get(holder.getAdapterPosition()).get_short(), rating));
                    } else {
                        selectedRatings.add(offsetedPosition, new Rating(Settings.get().details.get(holder.getAdapterPosition()).get_short(), rating));
                    }
                }
            });
        }
        else {
            if (selectedRatings.get(offsetedPosition) != null && selectedRatings.get(offsetedPosition).getRating() > 0)
                holder.getDetailCheckBox().setChecked(true);
            else {
                holder.getDetailCheckBox().setChecked(false);
            }
            holder.getDetailCheckBox().setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return Settings.get().detailCount;
    }
}
