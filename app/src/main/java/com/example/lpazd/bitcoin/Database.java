package com.example.lpazd.bitcoin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "database.db";
    public static final String TABLE_NAME = "wallet_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "CRYPTO";
    public static final String COL_3 = "AMOUNT";


    public Database(Context context) {
        super(context, DB_NAME, null, 1);
    }
//
//    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
//        super(context, name, factory, version, errorHandler);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String statement = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (id INTEGER PRIMARY KEY AUTOINCREMENT, crypto VARCHAR(200), amount DOUBLE)";
        db.execSQL(statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String crypto, Double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = db.rawQuery("select * FROM wallet_table WHERE CRYPTO = ?",new String[]{crypto});


        if (cursor != null){
            Log.v("hehe", "hahah");
                while (cursor.moveToNext()){
                    String id = String.valueOf(cursor.getInt(0));
                    updateData(id,amount);
                }
            return true;
        }else {
            Log.v("hehe2", "hahah2");
            contentValues.put(COL_2, crypto);
            contentValues.put(COL_3, amount);
            long result = db.insert(TABLE_NAME, null, contentValues);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public boolean updateData(String id, Double amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_3, amount);
        db.update(TABLE_NAME, contentValues,"ID = ?", new String[]{id});
        return true;
    }

}
