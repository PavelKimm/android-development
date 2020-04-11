package ru.tpu.courses.android;

import android.app.Application;

import ru.tpu.courses.lab3.Student;
import ru.tpu.courses.lab3.StudentsCache;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // lab3 region
        StudentsCache studentsCache = StudentsCache.getInstance();
        // lab3 region ends
    }
}
