package com.darrylbayliss.omgandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import config.TableControllerStudent;
import pojo.ObjectStudent;

public class MainActivity extends AppCompatActivity {

    private TextView txtRecordSize;
    private TableLayout tblLayout;

    private int recordCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Component Here
        initComponent();

        /*
        *   process all database load here
        */
        load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {
        Button btnCreateStudent = (Button) findViewById(R.id.btnCreate);
        btnCreateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View viewInput = inflater.inflate(R.layout.student_input_form, null, false);

                final EditText editFirstName = (EditText) viewInput.findViewById(R.id.editTextStudentFirstname);
                final EditText editEmail = (EditText) viewInput.findViewById(R.id.editTextStudentEmail);

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setView(viewInput);
                dialog.setTitle("Create Student");
                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        doSave(context, editFirstName, editEmail);
                    }
                });
                dialog.show();
            }
        });

        txtRecordSize = (TextView) findViewById(R.id.textViewRecordCount);
        txtRecordSize.setText(recordCount + " record(s) found.");

        tblLayout = (TableLayout) findViewById(R.id.tableLayout);
    }

    private void doSave(Context context, EditText nama, EditText email) {
        String firstName = nama.getText().toString();
        String mail = email.getText().toString();

        ObjectStudent student = new ObjectStudent();
//        student.setId(System.currentTimeMillis());
        student.setFirstname(firstName);
        student.setEmail(mail);

        boolean success = new TableControllerStudent(context).create(student);
        if (success) {
            Toast.makeText(context, "Student information was saved.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Unable to save student information.", Toast.LENGTH_SHORT).show();
        }
        countRecordSize();
        getRecords();
    }

    public void load() {
        // count record row or size
        countRecordSize();

        // getRecords, tampilkan seluruh data dari table
        getRecords();
    }

    public void countRecordSize() {
        recordCount = new TableControllerStudent(this).countRow();

        txtRecordSize.setText(recordCount + " record(s) found.");
    }

    public void getRecords() {
        tblLayout.removeAllViews();
        List<ObjectStudent> listStudents = new TableControllerStudent(this).collectObject();
        int i = 0;

        if (listStudents.size() > 0) {
            TableRow tblRow = null;
            for (ObjectStudent student : listStudents) {
                /**
                 * 1.looping isi list object student
                 * 2.Buat tablerow, buat text nama & email
                 * 3.taruh text nama & email ke dalam tablerow
                 * 4.taruh tablerow ke dalam tablelayout
                 */
                tblRow = new TableRow(this);
                tblRow.setId(100 + i);
                tblRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                i++;

                TextView txtName = new TextView(this);
                txtName.setText(student.getFirstname());

                TextView txtEmail = new TextView(this);
                txtEmail.setText(student.getEmail());

                tblRow.addView(txtName);
                tblRow.addView(txtEmail);

                tblLayout.addView(tblRow, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
            }
        } else {
            TableRow tblRow = new TableRow(this);
            tblRow.setId(100 + i);
            tblRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            TextView txtRecords = new TextView(this);
            txtRecords.setText("0 records");

            tblRow.addView(txtRecords);

            tblLayout.addView(tblRow, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
