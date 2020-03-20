package ru.tpu.courses.lab2;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Px;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Простейший пример самописного View. В данном случае мы просто наследуемся от LinearLayout-а и
 * добавляем свою логику, но также есть возможность отнаследоваться от {@link android.view.ViewGroup},
 * если необходимо реализовать контейнер для View полностью с нуля, либо отнаследоваться от {@link android.view.View}.
 * <p/>
 * Здесь можно было бы добавить автоматическое сохранение и восстановление состояния, переопределив методы
 * {@link #onSaveInstanceState()} и {@link #onRestoreInstanceState(Parcelable)}.
 */
public class Lab2ViewsContainer extends LinearLayout {

    private int minViewsCount;
    private int viewsCount;
    private ArrayList<Record> records = new ArrayList<>();

    /**
     * Этот конструктор используется при создании View в коде.
     */
    public Lab2ViewsContainer(Context context) {
        this(context, null);
    }

    /**
     * Этот конструктор вызывается при создании View из XML.
     */
    public Lab2ViewsContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Конструктор, вызывается при инфлейте View, когда у View указан дополнительный стиль.
     * Почитать про стили можно здесь https://developer.android.com/guide/topics/ui/look-and-feel/themes
     *
     * @param attrs атрибуты, указанные в XML. Стандартные android атрибуты обрабатываются внутри родительского класса.
     *              Здесь необходимо только обработать наши атрибуты.
     */
    public Lab2ViewsContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Свои атрибуты описываются в файле res/values/attrs.xml
        // Эта строчка объединяет возможные применённые к View стили
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Lab2ViewsContainer, defStyleAttr, 0);

        minViewsCount = a.getInt(R.styleable.Lab2ViewsContainer_lab2_minViewsCount, 0);
        if (minViewsCount < 0) {
            throw new IllegalArgumentException("minViewsCount can't be less than 0");
        }

        // Полученный TypedArray необходимо обязательно очистить.
        a.recycle();

        setViewsCount(minViewsCount);
    }

    /**
     * Программно создаём {@link TextView} и задаём его атрибуты, альтернативно можно описать его в
     * xml файле и инфлейтить его через класс LayoutInflater.
     */
//    public void incrementViews(String title, Float rating) {
//        TextView textView = new TextView(getContext());
//        textView.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
//        textView.setTextSize(16);
//        textView.setText(title);
//
//        // У каждого View, который находится внутри ViewGroup есть LayoutParams,
//        // в них содержится информация для лэйаута компонентов.
//        // Базовая реализация LayoutParams содержит только определение ширины и высоты
//        // (то, что мы указываем в xml в атрибутах layout_widget и layout_height).
//        // Получить их можно через метод getLayoutParams у View. Метод addView смотрит, если у View
//        // не установлены LayoutParams, то создаёт дефолтные, вызывая метод generateDefaultLayoutParams
//        addView(textView);
//    }


    public void incrementViews(String title, Float rate) {
        if ((title == null) || (rate == null)) {
            return;
        }
        int id = records.size();

        Record newRecord = new Record(id, title, rate);
        records.add(newRecord);

        Collections.sort(records, Record.rateComparator);
        removeAllViews();
        for (Record record:records) {
            createView(record.getId(), record.getTitle(), record.getRate());
        }
    }

    public void createView(int id, String optionText, Float rate) {
        Log.i("t","create view");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(HORIZONTAL);
        layout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

        TextView optionTextView = new TextView(getContext());
        optionTextView.setText(optionText);
        optionTextView.setLayoutParams(new LayoutParams(dpToPx(130), ViewGroup.LayoutParams.WRAP_CONTENT, 2));

        TextView scoreTextView = new TextView(getContext());
        scoreTextView.setText(String.format("%.1f", rate));
        scoreTextView.setGravity(Gravity.END);
        scoreTextView.setLayoutParams(new LayoutParams(dpToPx(40), ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        relativeLayout.setLayoutParams(new LayoutParams(dpToPx(200), ViewGroup.LayoutParams.MATCH_PARENT, 4));
        relativeLayout.setPadding(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6));

        View strip = new View(getContext());
        strip.setLayoutParams(new LayoutParams(dpToPx(200 * rate / 10), dpToPx(8)));
        strip.setBackgroundColor(0xFF000000);
        strip.setTag(id);

        relativeLayout.addView(strip);

        layout.addView(optionTextView);
        layout.addView(relativeLayout);
        layout.addView(scoreTextView);

        addView(layout);
    }

    public void setViewsCount(int viewsCount) {
        if (this.viewsCount == viewsCount) {
            return;
        }
//        viewsCount = viewsCount < minViewsCount ? minViewsCount : viewsCount;

        removeAllViews();
        this.viewsCount = 0;
//        for (int i = 0; i < viewsCount; i++) {
//            incrementViews();
//        }
    }

    public int getViewsCount() {
        return viewsCount;
    }

    /**
     * Метод трансформирует указанные dp в пиксели, используя density экрана.
     */
    @Px
    public int dpToPx(float dp) {
        if (dp == 0) {
            return 0;
        }
        float density = getResources().getDisplayMetrics().density;
        return (int) Math.ceil(density * dp);
    }
}