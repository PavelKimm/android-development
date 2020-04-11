package ru.tpu.courses.lab4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import ru.tpu.courses.lab4.db.GroupDao;
import ru.tpu.courses.lab4.db.Lab4Database;

public class AddGroupDialog extends AlertDialog {
    private final GroupDao groupDao = Lab4Database.getInstance(this.getContext()).groupDao();
    private Builder builder;

    AddGroupDialog(Context context) {
        super(context);
        builder = new AddGroupDialog.Builder(this.getContext());
        EditText groupName = new EditText(this.getContext());
        builder.setTitle(R.string.lab4_dialog_title)
                .setView(groupName)
                .setPositiveButton(R.string.lab4_add_group_btn, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Group group = new Group(
                                groupName.getText().toString()
                        );
                        groupDao.insert(group);

                    }
                })
                .create();
    }


    @Override
    public void show() {
        builder.show();
    }

}
