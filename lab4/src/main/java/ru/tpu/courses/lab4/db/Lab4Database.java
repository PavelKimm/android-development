package ru.tpu.courses.lab4.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Student.class, version = 1, exportSchema = false)
public abstract class Lab4Database extends RoomDatabase {

    private static Lab4Database db;

    @NonNull
    public static Lab4Database getInstance(@NonNull Context context) {
        if (db == null) {
            synchronized (Lab4Database.class) {
                if (db == null) {
                    db = Room.databaseBuilder(
                            context.getApplicationContext(),
                            Lab4Database.class,
                            "lab4_database"
                    )
                            // Запросы к БД могут быть весьма медленными и вообще практически любое
                            // обращение к файловой системе рекомендуется выполнять отдельно от
                            // основного потока. По умолчанию, если выполнить запросы к БД через
                            // Room, то приложение будет крашиться (это некоторый способ сказать
                            // программисту, что он делает неоптимальную вещь). Пока что мы
                            // отключаем эту проверку этим методом. В следующей лабораторной работе
                            // подробнее будет рассмотрено взаимодействие с БД не через основной поток.
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return db;
    }

    public abstract StudentDao studentDao();
}
