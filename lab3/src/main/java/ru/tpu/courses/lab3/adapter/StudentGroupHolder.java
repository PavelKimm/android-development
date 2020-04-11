package ru.tpu.courses.lab3.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.courses.lab3.R;

class StudentGroupHolder extends RecyclerView.ViewHolder {
    final TextView group;

    StudentGroupHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab3_item_student, parent, false));
        group = itemView.findViewById(R.id.student);
        group.setTextSize(40);
        group.setTextColor(Color.BLACK);
    }
}
