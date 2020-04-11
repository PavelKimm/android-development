package ru.tpu.courses.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.courses.lab4.adapter.StudentsAdapter;
import ru.tpu.courses.lab4.db.Lab4Database;
import ru.tpu.courses.lab4.db.StudentDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lab4Activity extends AppCompatActivity {


    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab4Activity.class);
    }

    private RecyclerView list;
    private AddGroupDialog addGroupDialog;

    private StudentDao studentDao;

    private static final int REQUEST_STUDENT_ADD = 3;


    private StudentsAdapter studentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab4_activityl);
        setTitle(getString(R.string.lab4_title, getClass().getSimpleName()));
        list = findViewById(android.R.id.list);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fabGroup = findViewById(R.id.fabGroup);
        studentDao = Lab4Database.getInstance(this).studentDao();
        addGroupDialog = new AddGroupDialog(this);
        List<StudentGroup> unsortedStudents = studentDao.getStudentsWithGroups();

        list.setAdapter(studentsAdapter = new StudentsAdapter());
        studentsAdapter.setStudents(sortStudents(unsortedStudents));


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

        /*
        Следующий ключевой компонент - это RecyclerView.Adapter. В нём описывается вся информация,
        необходимая для заполнения RecyclerView. В примере мы выводим пронумерованный список
        студентов, подробнее о работе адаптера в документации к классу StudentsAdapter.
         */

        /*
        При нажатии на кнопку мы переходим на Activity для добавления студента. Обратите внимание,
        что здесь используется метод startActivityForResult. Этот метод позволяет организовывать
        передачу данных обратно от запущенной Activity. В нашем случае, после закрытия lab4_AddStudentActivity,
        у нашей Activity будет вызван метод onActivityResult, в котором будут данные, которые мы
        указали перед закрытием lab4_AddStudentActivity.
         */
        fab.setOnClickListener(
                v -> startActivityForResult(
                        AddStudentActivity.newIntent(this),
                        REQUEST_STUDENT_ADD
                )
        );
        fabGroup.setOnClickListener(v -> addGroupDialog.show());
    }


    boolean isFromAddStudentActivity = false;

    @Override
    protected void onResume() {
        if (!isFromAddStudentActivity) {
            final SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(this);
            int position = preferences.getInt("position", 0);
            list.scrollToPosition(position);
            isFromAddStudentActivity = false;
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        int firstVisiblePosition = ((LinearLayoutManager) Objects.requireNonNull(list.getLayoutManager()))
                .findFirstVisibleItemPosition();
        preferences.edit().putInt("position", firstVisiblePosition).apply();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addGroupDialog.dismiss();
    }

    public List<ListItem> sortStudents(List<StudentGroup> unsortedStudents) {
        boolean isFound;

        List<ListItem> sortedStudents = new ArrayList<>();
        for (int j = 0; j < unsortedStudents.size(); j++) {
            isFound = false;
            StudentGroup currStudent = unsortedStudents.get(j);
            for (int i = 0; i < sortedStudents.size(); i++) {
                if (sortedStudents.get(i).getType() == 0) continue;
                StudentGroup st = (StudentGroup) sortedStudents.get(i);
                String currStudentGroup = st.groupName;
                if (currStudentGroup.equals(currStudent.groupName)) {
                    sortedStudents.add(i, currStudent);
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                isNewGroup = 1;
                Group group = new Group(currStudent.groupName);
                sortedStudents.add(group);
                sortedStudents.add(currStudent);
            }
        }
        return sortedStudents;
    }

    public int isNewGroup = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STUDENT_ADD && resultCode == RESULT_OK) {
            Student student = AddStudentActivity.getResultStudent(data);
            studentDao.insert(student);

            List<StudentGroup> unsortedStudents = studentDao.getStudentsWithGroups();
            List<ListItem> sortedStudents = sortStudents(unsortedStudents);
            studentsAdapter.setStudents(sortedStudents);
            int insertionIndx = sortedStudents.indexOf(student);
            int insertionCount = 1;
            if (insertionIndx == sortedStudents.size() - 1)//при добавлении студента он ставится первым в группе(sortStudents)
            {
                insertionCount++;//в данном случае кроме студента добавляется группа
                insertionIndx--;//группа указывается перед студентом
            }
            studentsAdapter.notifyItemRangeInserted(insertionIndx, insertionCount);
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
            isFromAddStudentActivity = true;
            isNewGroup = 0;
        }
    }

}
