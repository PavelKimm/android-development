package ru.tpu.courses.lab2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * <b>Вёрстка UI. Сохранение состояния.</b>
 * <p/>
 * Андроид старается минимизировать объём занимаемой оперативной памяти, поэтому при любом удобном
 * случае выгружает приложение или Activity из памяти. Например, при повороте экрана (если включен автоповорот),
 * весь объект Activity будет пересоздан с 0. Сохранить введенные данные можно несколькими способами:
 * <ul>
 * <li>Сохранить значения в оперативной памяти. Тогда данные переживут пересоздание Activity,
 * но не переживут освобождение приложения из памяти. Этот пример будет рассмотрен в 3ей лабораторной</li>
 * <li>Сохранить значения в файловой системе. Тогда данные переживут освобождение приложения.
 * Взаимодействие с файловой системой может быть длительной операцией и привносит свои проблемы.
 * Рассмотрено оно будет в 4ой лабораторной</li>
 * <li>Используя встроенную в андроид систему сохранения состояния, которую мы используем в
 * этой лабораторной работе. Перед уничтожением Activity будет вызван метод {@link #onSaveInstanceState(Bundle)},
 * в котором можно записать все необходимые значения в переданный объект Bundle. Стоит учитывать,
 * что дополнительно андроид сохраняет в него состояние всех View на экране по их идентификаторам.
 * Так, многие реализации View автоматически сохраняют свои данные, если им указан идентификатор.
 * Например, {@link android.widget.ScrollView} должен сохранять, на сколько отскроллен его
 * контент и восстанавливать его</li>
 * </ul>
 * <p/>
 */
public class Lab2Activity extends AppCompatActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab2Activity.class);
    }

    private Lab2ViewsContainer lab2ViewsContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab2_activity);

        setTitle(getString(R.string.lab2_title, getClass().getSimpleName()));

        // findViewById - generic метод https://docs.oracle.com/javase/tutorial/extra/generics/methods.html,
        // который автоматически кастит (class cast) View в указанный класс.
        // Тип вью, в которую происходит каст, не проверяется, поэтому если здесь указать View,
        // отличную от View в XML, то приложение крашнется на вызове этого метода.
        lab2ViewsContainer = findViewById(R.id.container);

        EditText titleEditText = findViewById(R.id.titleEditText);
        String title = titleEditText.getText().toString();

//        findViewById(R.id.addElementBtn).setOnClickListener(view -> lab2ViewsContainer.incrementViews(title));

        Button addElementBtn = findViewById(R.id.addElementBtn);
        addElementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText titleEditText = findViewById(R.id.titleEditText);
                EditText ratingEditText = findViewById(R.id.ratingEditText);

                String title = titleEditText.getText().toString();
                Float rating = Float.parseFloat(ratingEditText.getText().toString());

                lab2ViewsContainer.incrementViews(title, rating);
            }
        });
        // Восстанавливаем состояние нашего View, добавляя заново все View
//        if (savedInstanceState != null) {
//            lab2ViewsContainer.setViewsCount(savedInstanceState.getInt(STATE_VIEWS_COUNT));
//        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt(STATE_VIEWS_COUNT, lab2ViewsContainer.getViewsCount());
    }
}
