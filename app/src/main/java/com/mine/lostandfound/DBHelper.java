package com.mine.lostandfound;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME    = "LostAndFound.db";
    private static final int    DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "items";
    private static final String COL_ID      = "ID";
    private static final String COL_NAME    = "ITEM_NAME";
    private static final String COL_DESC    = "DESCRIPTION";
    private static final String COL_DATE    = "DATE_REPORTED";
    private static final String COL_LOC     = "LOCATION";
    private static final String COL_PHONE   = "PHONE";
    private static final String COL_STATUS  = "STATUS";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ("
            + COL_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME   + " TEXT, "
            + COL_DESC   + " TEXT, "
            + COL_DATE   + " TEXT, "
            + COL_LOC    + " TEXT, "
            + COL_PHONE  + " TEXT, "
            + COL_STATUS + " TEXT"
            + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        if (oldV < 2) {
            // schema v1 → v2: add PHONE
            db.execSQL("ALTER TABLE " + TABLE_NAME +
                " ADD COLUMN " + COL_PHONE + " TEXT");
        }
        // if you bump to v3, handle that here…
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldV, int newV) {
        // simplest: drop and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /** Insert a new lost/found advert */
    public boolean insertItem(String name,
                              String desc,
                              String date,
                              String loc,
                              String phone,
                              String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME,   name);
        cv.put(COL_DESC,   desc);
        cv.put(COL_DATE,   date);
        cv.put(COL_LOC,    loc);
        cv.put(COL_PHONE,  phone);
        cv.put(COL_STATUS, status);
        long id = db.insert(TABLE_NAME, null, cv);
        return id != -1;
    }

    /** Fetch all items (including phone & status) */
    public List<Item> getAllItems() {
        List<Item> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                int    id     = c.getInt   (c.getColumnIndexOrThrow(COL_ID));
                String name   = c.getString(c.getColumnIndexOrThrow(COL_NAME));
                String desc   = c.getString(c.getColumnIndexOrThrow(COL_DESC));
                String date   = c.getString(c.getColumnIndexOrThrow(COL_DATE));
                String loc    = c.getString(c.getColumnIndexOrThrow(COL_LOC));
                String phone  = c.getString(c.getColumnIndexOrThrow(COL_PHONE));
                String status = c.getString(c.getColumnIndexOrThrow(COL_STATUS));
                list.add(new Item(id, name, desc, date, loc, phone, status));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    /** Lookup one item by ID */
    public Item getItemById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
            TABLE_NAME,
            null,                    // all columns
            COL_ID + "=?",           // WHERE ID=?
            new String[]{String.valueOf(id)},
            null, null, null
        );
        Item item = null;
        if (c.moveToFirst()) {
            String name   = c.getString(c.getColumnIndexOrThrow(COL_NAME));
            String desc   = c.getString(c.getColumnIndexOrThrow(COL_DESC));
            String date   = c.getString(c.getColumnIndexOrThrow(COL_DATE));
            String loc    = c.getString(c.getColumnIndexOrThrow(COL_LOC));
            String phone  = c.getString(c.getColumnIndexOrThrow(COL_PHONE));
            String status = c.getString(c.getColumnIndexOrThrow(COL_STATUS));
            item = new Item(id, name, desc, date, loc, phone, status);
        }
        c.close();
        return item;
    }

    /** Remove an item by ID */
    public boolean removeItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
            TABLE_NAME,
            COL_ID + "=?",
            new String[]{ String.valueOf(id) }
        ) > 0;
    }
}
