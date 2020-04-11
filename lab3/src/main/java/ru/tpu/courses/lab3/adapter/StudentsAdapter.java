package ru.tpu.courses.lab3.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.courses.lab3.AddStudentActivity;
import ru.tpu.courses.lab3.Group;
import ru.tpu.courses.lab3.ListItem;
import ru.tpu.courses.lab3.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Задача Адаптера - управление View, которые содержатся в RecyclerView, с учётом его жизненного цикла.
 * Адаптер работает не с View, а с {@link RecyclerView.ViewHolder}. Этот класс содержит не только
 * View, которая будет показана на экране, но и дополнительную информацию, вроде позиции элемента
 * в списке.
 * <p>
 * Сначала мы переопределяем метод {@link #getItemCount()}. В нём необходимо вернуть количество
 * элементов в списке. В нашем случае это количество студентов помноженное на 2, т.к. на каждого
 * студента идёт 2 отдельных View. Одна - с номером студента, другая - с его ФИО.
 * <p>
 * Т.к. у нас идёт 2 разных типа View, то мы переопределяем метод {@link #getItemViewType(int)},
 * в котором должны вернуть номер типа View для переданной в методе позиции списка.
 * <p>
 * В методе {@link #onCreateViewHolder(ViewGroup, int)} мы создаём ViewHolder для
 * соответствующего типа View. Здесь мы производим инфлейт View из XML и ищем нужные нам View
 * в их иерархии.
 * <p>
 * В методе {@link #onBindViewHolder(RecyclerView.ViewHolder, int)} мы описываем заполнение
 * ViewHolder-а данными, соответствующими переданной нам позиции.
 * <p>
 * Когда мы только вызвали {@link RecyclerView#setAdapter(RecyclerView.Adapter)}, согласно алгоритму
 * лэйаута описанному в LayoutManager, RecyclerView начинает вызывать методы адаптера,
 * чтобы расположить столько элементов, сколько помещается на экране.
 * Для каждого такого элемента вызывается сначала getItemViewType, с полученным itemViewType
 * вызывается метод onCreateViewHolder и созданный viewHolder передаётся в onBindViewHolder для заполнения
 * данными. После этого измеряются размеры элемента и добавляются в RecyclerView. Как только мы вышли
 * за пределы размеров RecyclerView, процесс останавливается. При скролле списка вниз, верхние
 * ViewHolder, которые стали не видны, открепляются от RecyclerView и добавляются в
 * {@link RecyclerView.RecycledViewPool}. Снизу же, когда появляется пустое пространство, в
 * RecyclerViewPool ищется ViewHolder для viewType следующего элемента. Если такой найден, то для него
 * вызывается onBindViewHolder и ViewHolder добавляется снизу.
 * <p>
 * Для того, чтобы сказать RecyclerView, что список был обновлён, используются методы
 * {@link #notifyDataSetChanged()}, {@link #notifyItemInserted(int)} и т.д. notifyDataSetChanged
 * обновляет весь список, а остальные методы notify... говорят об изменении конкретного элемента и
 * что с ним произошло, что позволяет делать анимированные изменения в списке. При этом RecyclerView
 * всё также будет работать с теми же закэшированными ViewHolder и не будет пересоздавать все View.
 */

public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_STUDENT = 1;
    private static final int TYPE_GROUP = 0;

    private List<ListItem> students = new ArrayList<>();

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            /*case TYPE_NUMBER:
                return new NumberHolder(parent);*/
            case TYPE_STUDENT:
                return new StudentHolder(parent);
           case TYPE_GROUP:
                return new StudentGroupHolder(parent);
        }
        throw new IllegalArgumentException("unknown viewType = " + viewType);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            /*case TYPE_NUMBER:
                NumberHolder numberHolder = (NumberHolder) holder;
                // Высчитыванием номер студента начиная с 1
                numberHolder.bind((position + 1) / 2);
                break;*/
            case TYPE_STUDENT:
                StudentHolder studentHolder = (StudentHolder) holder;
                Student student = (Student) students.get(position);
                //Group group = groups.get(position);
                studentHolder.student.setText(
                        student.firstName + " " + student.secondName + " " + student.lastName
                );
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View view) {

                                                           Intent intent = new Intent(view.getContext(), AddStudentActivity.class);
                                                           intent.putExtra("Student", student);
                                                           view.getContext().startActivity(intent);

                                                       }
                                                   }

                );
                break;
            case TYPE_GROUP:
                StudentGroupHolder studentGroupHolder = (StudentGroupHolder) holder;
                Group group = (Group) students.get(position);
                studentGroupHolder.group.setText(
                        group.groupName
                );
                break;
        }

    }

    @Override
    public int getItemCount() {
       return students.size();
    }

    @Override
    public int getItemViewType(int position) {
       return students.get(position).getType();
    }

    public void setStudents(List<ListItem> students) {
        this.students = students;
    }

}

