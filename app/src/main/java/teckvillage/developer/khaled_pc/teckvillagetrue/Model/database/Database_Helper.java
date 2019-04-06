package teckvillage.developer.khaled_pc.teckvillagetrue.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.View.BlockList;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.database.tables.BlockListHistory;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.database.tables.block;

/**
 * Created by khaled-pc on 3/30/2019.
 */

public class Database_Helper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    public static final String DATABASE_NAME = "WhoCaller_db";


    public Database_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create Block table
        db.execSQL(block.CREATE_TABLE);
        // create Block List History table
        db.execSQL(BlockListHistory.CREATE_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + block.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BlockListHistory.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    //TODO:#####################################################3/31/2019  block Operation  ########################################################

    public long insertBlock(String name,String number,String type) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id`  will be inserted automatically.
        // no need to add them
        values.put(block.COLUMN_NAME, name);
        values.put(block.COLUMN_NUMBER, number);
        values.put(block.COLUMN_TYPE, type);
        // insert row
        long id = db.insert(block.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public List<block> getAllBlocklist() {
        List<block> courtsList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + block.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                block block1 = new block();
                block1.setId(cursor.getInt(cursor.getColumnIndex(block.COLUMN_ID)));
                block1.setName(cursor.getString(cursor.getColumnIndex(block.COLUMN_NAME)));
                block1.setNumber(cursor.getString(cursor.getColumnIndex(block.COLUMN_NUMBER)));

                courtsList.add(block1);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Court list
        return courtsList;
    }


    public List<String> getAllBlocklistNumbers() {
        List<String> courtsList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  "+block.COLUMN_NUMBER +" FROM " + block.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String num=cursor.getString(cursor.getColumnIndex(block.COLUMN_NUMBER));

                courtsList.add(num);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Court list
        return courtsList;
    }




    public int getblockCount() {
        String countQuery = "SELECT  * FROM " + block.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public void deleteblock(block block2) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(block.TABLE_NAME, block.COLUMN_ID + " = ?",
                new String[]{String.valueOf(block2.getId())});
        db.close();
    }

    public void deleteBlockByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(block.TABLE_NAME, block.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteBlockByNumber(String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(block.TABLE_NAME, block.COLUMN_NUMBER + " = ?",
                new String[]{String.valueOf(number)});
        db.close();
    }


    public int updateblocksbyID(int id,String name,String city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(block.COLUMN_NAME, name);
        values.put(block.COLUMN_NUMBER, city);


        // updating row
        return db.update(block.TABLE_NAME, values, block.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    //TODO:#####################################################3/31/2019  block List History Operation  ########################################################

    public long insertBlockListHistory(long ID,String number) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id`  will be inserted automatically.
        // no need to add them
        values.put(BlockListHistory.COLUMN_CALLLOGID, ID);
        values.put(BlockListHistory.COLUMN_NUMBER, number);

        // insert row
        long id = db.insert(BlockListHistory.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }



    public List<BlockListHistory> getAllBlocklistHistory() {
        List<BlockListHistory> courtsList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + BlockListHistory.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BlockListHistory block1 = new BlockListHistory();
                block1.setId(cursor.getInt(cursor.getColumnIndex(BlockListHistory.COLUMN_ID)));
                block1.setCallLogID(cursor.getLong(cursor.getColumnIndex(BlockListHistory.COLUMN_CALLLOGID)));
                block1.setNumber(cursor.getString(cursor.getColumnIndex(BlockListHistory.COLUMN_NUMBER)));

                courtsList.add(block1);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Court list
        return courtsList;
    }

    public List<Long> getAllBlocklistHistoryCallLogIDCOLUMN() {
        List<Long> courtsList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  "+BlockListHistory.COLUMN_CALLLOGID +" FROM " + BlockListHistory.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Long CalllogID=cursor.getLong(cursor.getColumnIndex(BlockListHistory.COLUMN_CALLLOGID));

                courtsList.add(CalllogID);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Court list
        return courtsList;
    }

    public boolean deleteLogCallIDFromHistory(long ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        int del=db.delete(BlockListHistory.TABLE_NAME, BlockListHistory.COLUMN_CALLLOGID + " = ?",
                new String[]{String.valueOf(ID)});
        db.close();
        return del>0;
    }



}
