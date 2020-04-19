package ru.tpu.courses.lab5;

public interface Observer<T> {
    void onLoading(Task<T> task);
    void onSuccess(Task<T> task, T data);
    void onError(Task<T> task, Exception e);
}

