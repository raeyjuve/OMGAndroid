package config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pojo.ObjectStudent;

/**
 * Created by Frog-Grammar on 25-Nov-15.
 */
public class TableControllerStudent extends DatabaseHandler {

    public TableControllerStudent(Context context) {
        super(context);
    }

    public boolean create(ObjectStudent student) {
        ContentValues values = new ContentValues();
        values.put("firstname", student.getFirstname());
        values.put("email", student.getEmail());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean success = db.insert("students", null, values) > 0;
        db.close();

        return success;
    }

    public int countRow() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM students";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;
    }

    public List<ObjectStudent> collectObject() {
        List<ObjectStudent> listStudent = new ArrayList<ObjectStudent>();

        String sql = "SELECT * FROM students ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        /**
         * 1.hasil query masuk dibuat dalam bentuk object cursor
         * 2.kemudian isi cursor di loop
         * 3.di dalam loop dilakukan pembuatan & pengisian TableRow ke dalam TableLayout
         */
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                addObjectToList(cursor, listStudent);
            } while (cursor.moveToNext());
        }

        /**
         * tutup cursor & db
         */
        cursor.close();
        db.close();

        return listStudent;
    }

    private void addObjectToList(Cursor cursor, List<ObjectStudent> listStudent) {
        /**
         * buat variable kemudian isikan masing-masing entity
         */
        int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
        String firstName = cursor.getString(cursor.getColumnIndex("firstname"));
        String email = cursor.getString(cursor.getColumnIndex("email"));

        ObjectStudent student = new ObjectStudent();
        student.setId(id);
        student.setFirstname(firstName);
        student.setEmail(email);

        listStudent.add(student);
    }
}
