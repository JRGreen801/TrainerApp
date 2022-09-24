package de.jrgreen.trainerapp.recycler.viewHolder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.jrgreen.trainerapp.R;

public class DetailViewHolder extends RecyclerView.ViewHolder {

    private TextView detailText;
    private CheckBox detailCheckBox;
    private ImageView imageView;

    public DetailViewHolder(@NonNull View itemView) {
        super(itemView);
        detailText = itemView.findViewById(R.id.detailTextView);
        detailCheckBox = itemView.findViewById(R.id.detailCheckBox);
        imageView = itemView.findViewById(R.id.detail_imageView);
    }

    public TextView getDetailText() {
        return detailText;
    }
    public CheckBox getDetailCheckBox() {
        return detailCheckBox;
    }
    public ImageView getImageView(){return imageView;}
}
