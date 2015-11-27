package config;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import pojo.ObjectStudent;

/**
 * Created by Frog-Grammar on 27-Nov-15.
 */
public class OnLongClickListenerStudentRecord implements View.OnLongClickListener{
    Context context;
    String id;

    @Override
    public boolean onLongClick(final View view) {
        context = view.getContext();
        id = view.getTag().toString();

        final CharSequence[] item = {"Edit", "Delete"};
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    editRecord(Integer.parseInt(id));
                }

                dialog.dismiss();
            }
        });
        alert.show();

        return false;
    }

    public void editRecord(final int studentId) {
        final TableControllerStudent tableControllerStudent = new TableControllerStudent(context);
        ObjectStudent objectStudent = tableControllerStudent.readSingleRecord(studentId);
    }
}
