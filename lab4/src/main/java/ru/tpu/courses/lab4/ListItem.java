package ru.tpu.courses.lab4;

public abstract class ListItem {
    static final int TYPE_GROUP = 0;
    static final int TYPE_STUDENT = 1;

    abstract public int getType();
}
