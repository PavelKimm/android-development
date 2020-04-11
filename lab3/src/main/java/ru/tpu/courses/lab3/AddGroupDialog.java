package ru.tpu.courses.lab3;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;

public class AddGroupDialog extends AlertDialog {
    private final GroupCache groupsCache = GroupCache.getInstance();
    private Builder builder;

    AddGroupDialog(Context context) {
        super(context);
        builder = new AddGroupDialog.Builder(this.getContext());
        EditText groupName = new EditText(this.getContext());
        builder.setTitle(R.string.lab3_dialog_title)
                .setView(groupName)
                .setPositiveButton(R.string.lab3_add_group_btn, (dialog, id) -> {

                    Group group = new Group(
                            groupName.getText().toString()
                    );
                    groupsCache.addGroup(group);

                })
                .create();
    }

    @Override
    public void show() {
        builder.show();
    }

}
