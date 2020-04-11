package ru.tpu.courses.lab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.courses.lab3.adapter.StudentsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class Lab3Activity extends AppCompatActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab3Activity.class);
    }

    private final StudentsCache studentsCache = StudentsCache.getInstance();

    private RecyclerView list;
    private AddGroupDialog addGroupDialog;

    private static final int REQUEST_STUDENT_ADD = 1;


    private StudentsAdapter studentsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_activity);
        setTitle(getString(R.string.lab3_title, getClass().getSimpleName()));
        list = findViewById(android.R.id.list);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fabGroup = findViewById(R.id.fabGroup);
        addGroupDialog = new AddGroupDialog(this);


        /*
        Здесь идёт инициализация RecyclerView. Первое, что необходимо для его работы, это установить
        реализацию LayoutManager-а. Он содержит логику размещения View внутри RecyclerView. Так,
        LinearLayoutManager, который используется ниже, располагает View последовательно, друг за
        другом, по аналогии с LinearLayout-ом. Из альтернатив можно например использовать
        GridLayoutManager, который располагает View в виде таблицы. Необходимость написания своего
        LayoutManager-а возникает очень редко и при этом является весьма сложным процессом, поэтому
        рассматриваться в лабораторной работе не будет.
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(studentsAdapter = new StudentsAdapter());
        /*
        Следующий ключевой компонент - это RecyclerView.Adapter. В нём описывается вся информация,
        необходимая для заполнения RecyclerView. В примере мы выводим пронумерованный список
        студентов, подробнее о работе адаптера в документации к классу StudentsAdapter.
         */

        /*
        При нажатии на кнопку мы переходим на Activity для добавления студента. Обратите внимание,
        что здесь используется метод startActivityForResult. Этот метод позволяет организовывать
        передачу данных обратно от запущенной Activity. В нашем случае, после закрытия AddStudentActivity,
        у нашей Activity будет вызван метод onActivityResult, в котором будут данные, которые мы
        указали перед закрытием AddStudentActivity.
         */
        fab.setOnClickListener(
                v -> startActivityForResult(
                        AddStudentActivity.newIntent(this),
                        REQUEST_STUDENT_ADD
                )
        );
        fabGroup.setOnClickListener(v -> addGroupDialog.show());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
            addGroupDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STUDENT_ADD && resultCode == RESULT_OK) {
            Student student = AddStudentActivity.getResultStudent(data);

            studentsCache.addStudent(student);

            boolean isFound;
            List<Student> unsortedStudents = studentsCache.getStudents();
            List<ListItem> sortedStudents = new ArrayList<>();
            for (int j = 0; j < unsortedStudents.size(); j++) {
                isFound = false;
                Student currStudent = unsortedStudents.get(j);
                for (int i = 0; i < sortedStudents.size(); i++) {
                    if (sortedStudents.get(i).getType() == 0) continue;
                    Student st = (Student) sortedStudents.get(i);
                    String currStudentGroup = st.groupName;
                    if (currStudentGroup.equals(currStudent.groupName)) {
                        sortedStudents.add(i, currStudent);
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    Group group = new Group(currStudent.groupName);
                    sortedStudents.add(group);
                    sortedStudents.add(currStudent);
                }
            }

            studentsAdapter.setStudents(sortedStudents);
            int insertionIndx = sortedStudents.indexOf(student);
            int insertionCount = 1;
            if (insertionIndx == sortedStudents.size() - 1)
            {
                insertionCount++;
                insertionIndx--;
            }
            studentsAdapter.notifyItemRangeInserted(insertionIndx, insertionCount);
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }
    }
}
