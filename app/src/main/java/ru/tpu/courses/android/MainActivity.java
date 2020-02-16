package ru.tpu.courses.android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.tpu.courses.lab1.Lab1Activity;
import ru.tpu.courses.lab2.Lab2Activity;
import ru.tpu.courses.lab3.Lab3Activity;
import ru.tpu.courses.lab4.Lab4Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Переопределяя методы жизненного цикла Activity
        super.onCreate(savedInstanceState);

        // Метод setContentView задаёт корневую View у Activity.
        // У него есть 2 перегрузки, которые отражают 2 способа верстки UI:
        // 1. Программно, аллоцируя и конфигурируя в коде все необходимые View. Это будет эффективнее второго способа, но весьма неудобно.
        // Впрочем, есть отдельные библиотеки, которые упрощают процесс верстки UI в коде (например https://github.com/Kotlin/anko),
        // но в лабораторных они рассматриваться не будут.
        //
        // 2. Принимает идентификатор ресурса на иерархию View в формате XML. В этом случае
        // андроид будет парсить указанный файл и за нас создавать всю иерархию View.
        // Этот процесс называется инфлейтом и за него отвечает класс LayoutInflater.

        // Ресурсы приложения должны добавляться в папку res, где уже распределяются по типам в разные папки.
        // Файлы вёрстки находятся в папке layout. Для всех ресурсов генерируются уникальные идентификаторы
        // в классе R. Найти его можно в папке build/generated/r_class_sources/debug/r.
        // Если проект сбилдился без ошибок, то по ресурсу можно перейти в соответствующий XML файл,
        // нажав по нему левой кнопкой мыши с зажатой клавишей Ctrl.
        setContentView(R.layout.activity_main);

        // После инфлейта View, нам необходимо найти кнопки по их идентификатору, используя метод findViewById.
        // Далее вызываем метод setOnClickListener, в который передаём лямбду, содержащую то, что мы хотим сделать при нажатии на кнопку.
        // Каждая из лабораторных работ выполняется в отдельном изолированном модуле.
        // В основной модуль (app) подключаются модули с лабораторными работами.
        // Для сборки проекта используется билд система Gradle. Его конфигурация находится в файлах с расширением *.gradle.
        findViewById(R.id.lab1).setOnClickListener((v) -> {
            // Для того, чтобы перейти на другой экран (Activity), используется метод startActivity.
            // Ему в параметры приходит объект класса Intent, содержащий информацию о том,
            // какую Activity необходимо запустить и с какими параметрами.
            // В случае, если приложение было выгржено из памяти системой
            // (например, это может произойти, когда мы свернули приложение), система сохранит
            // в файловую систему все объекты Intent, использованные для переходов между Activity.
            // Тогда, когда мы развернём приложение, используя эту сохраненную информацию, система
            // сможет восстановить приложение в той точке, на которой был пользователь.
            // Подробнее о системе Intent-ов - https://developer.android.com/guide/components/intents-filters
            startActivity(Lab1Activity.newIntent(this));
        });
        findViewById(R.id.lab2).setOnClickListener((v) -> startActivity(Lab2Activity.newIntent(this)));
        findViewById(R.id.lab3).setOnClickListener((v) -> startActivity(Lab3Activity.newIntent(this)));
        findViewById(R.id.lab4).setOnClickListener((v) -> startActivity(Lab4Activity.newIntent(this)));
        findViewById(R.id.lab5).setOnClickListener((v) -> { });
        findViewById(R.id.lab6).setOnClickListener((v) -> { });
    }
}
