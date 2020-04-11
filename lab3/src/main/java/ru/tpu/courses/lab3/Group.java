package ru.tpu.courses.lab3;

import androidx.annotation.NonNull;

public class Group extends ListItem {
    @NonNull
    public String groupName;

    Group(@NonNull String groupName) {
        this.groupName = groupName;
    }


    @NonNull
    @Override
    public String toString() {
        return groupName;
    }

    @Override
    public int getType() {
        return TYPE_GROUP;
    }
}