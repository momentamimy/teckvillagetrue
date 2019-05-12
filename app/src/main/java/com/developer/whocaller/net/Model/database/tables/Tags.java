package com.developer.whocaller.net.Model.database.tables;

/**
 * Created by khaled-pc on 5/2/2019.
 */

public class Tags {

    public static final String TABLE_NAME = "Tags";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PARENT_ID = "parentid";
    public static final String COLUMN_NAME = "name";



    private int id;
    private String tagname;
    private String parent_Id;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY ,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PARENT_ID + " TEXT"
                    + ")";

    public Tags() {
    }

    public Tags(int id, String tagname,String parent_Id) {
        this.id = id;
        this.tagname = tagname;
        this.parent_Id=parent_Id;

    }

    public Tags(int id, String tagname) {
        this.id = id;
        this.tagname = tagname;


    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getParent_Id() {
        return parent_Id;
    }

    public void setParent_Id(String parent_Id) {
        this.parent_Id = parent_Id;
    }
}
