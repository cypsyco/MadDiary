package com.example.tab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class PhoneBookDB extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "address.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "phone_info";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "phone_name";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_PHONE_PHOTO = "user_image";

    public PhoneBookDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_PHONE_NUMBER + " TEXT, "
                + COLUMN_PHONE_PHOTO + " BLOP);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //연락처 전체 가져오기
    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void addPhoneNumber(String name, String phone_number, byte[] phone_photo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PHONE_NUMBER, phone_number);
        cv.put(COLUMN_PHONE_PHOTO, phone_photo);

        long result = db.insert(TABLE_NAME, null, cv);

        if(result == -1){
            Toast.makeText(context, "Failed saving", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData(String id, String name, String phone_number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PHONE_NUMBER, phone_number);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{id});

        if (result == -1){
            Toast.makeText(context, "Failed Editing", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully Edited", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, "_id=?", new String[]{id});
        if (result == -1){
            Toast.makeText(context, "Failed Deleting", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readDataByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(name)});
    }
}

