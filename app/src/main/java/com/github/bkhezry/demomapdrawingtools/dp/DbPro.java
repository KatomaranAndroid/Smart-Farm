package com.github.bkhezry.demomapdrawingtools.dp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DbPro extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "proManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "pro";

    // Contacts Table Columns names
    private static final String KEY_PROID = "proid";
    private static final String KEY_DATA = "data";

    public DbPro(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_PROID + " TEXT,"
                + KEY_DATA + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new shgName
    public void addData(String farmerid, String data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROID, farmerid); // ShgName Name
        values.put(KEY_DATA, data); // ShgName Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public ArrayList<String> getDataByFarmerid(String farmerid) {
        SQLiteDatabase db = this.getReadableDatabase();

        final Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_PROID,
                        KEY_DATA}, KEY_PROID + "=?",
                new String[]{farmerid}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return new ArrayList<String>() {{
                add(cursor.getString(0));
                add(cursor.getString(1));
            }};
        }
        return null;
    }

    // Getting single contact
    public ArrayList<String> getDataByProid(String proid) {
        SQLiteDatabase db = this.getReadableDatabase();

        final Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_PROID,
                        KEY_DATA}, KEY_PROID + "=?",
                new String[]{proid}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return new ArrayList<String>() {{
                add(cursor.getString(0));
                add(cursor.getString(1));
            }};
        }
        return null;
    }

    public List<ArrayList<String>> getAllData() {
        List<ArrayList<String>> dataList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getWritableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                dataList.add(new ArrayList<String>() {{
                    add(cursor.getString(0));
                    add(cursor.getString(1));
                }});
            } while (cursor.moveToNext());
        }
        return dataList;
    }

    // Updating single shgName
    public int updatedataByProid(String proid, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PROID, proid); // ShgName Name
        values.put(KEY_DATA, data); // ShgName Phone
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_PROID + " = ?",
                new String[]{proid});
    }

    // Deleting single shgName
    public void deleteFarmerid(String farmerid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_PROID + " = ?",
                new String[]{farmerid});
        db.close();
    }

    // Deleting single shgName
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null,
                null);
        db.close();
    }

    public int getCountByFarmerid(String farmeid) {
        int result;
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_PROID + "='" + farmeid + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        result = cursor.getCount();
        cursor.close();
        // return count
        return result;
    }

}
