package ru.tpu.courses.lab4.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.courses.lab4.R;

public class StudentGroupHolder extends RecyclerView.ViewHolder {
    public final TextView group;

    public StudentGroupHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab4_item_student, parent, false));
        group = itemView.findViewById(R.id.student);
        group.setTextSize(40);
        group.setTextColor(Color.BLACK);
    }
}
