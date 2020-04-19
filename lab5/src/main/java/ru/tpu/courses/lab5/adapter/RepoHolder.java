package ru.tpu.courses.lab5.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.courses.lab5.R;

class RepoHolder extends RecyclerView.ViewHolder {

    final TextView repo;
    final TextView description;

    RepoHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab5_item_repo, parent, false));
        repo = itemView.findViewById(R.id.item_repo);
        LinearLayout mainLayout = itemView.findViewById(R.id.HolderLayout);
        description = itemView.findViewById(R.id.description);
        repo.setTextSize(20);
        description.setTextSize(15);
        description.setMaxLines(5);
        description.setTextColor(Color.GRAY);
        mainLayout.setPadding(0,5,0,5);
    }
}
