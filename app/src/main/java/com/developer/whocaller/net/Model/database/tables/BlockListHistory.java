package com.developer.whocaller.net.Model.database.tables;

/**
 * Created by khaled-pc on 4/5/2019.
 */

public class BlockListHistory {

    public static final String TABLE_NAME = "BlockListHistory";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CALLLOGID = "CallLogID";
    public static final String COLUMN_NUMBER = "number";


    private int id;
    private long CallLogID;
    private String number;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CALLLOGID + " INTEGER,"
                    + COLUMN_NUMBER + " TEXT"
                    + ")";

    public BlockListHistory() {
    }

    public BlockListHistory(int id, long CallLogID,String number) {
        this.id = id;
        this.CallLogID = CallLogID;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCallLogID() {
        return CallLogID;
    }

    public void setCallLogID(long callLogID) {
        CallLogID = callLogID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
