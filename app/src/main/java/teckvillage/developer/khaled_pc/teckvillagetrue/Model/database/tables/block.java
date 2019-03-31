package teckvillage.developer.khaled_pc.teckvillagetrue.model.database.tables;

/**
 * Created by khaled-pc on 3/30/2019.
 */

public class block {
    public static final String TABLE_NAME = "Block";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER = "number";


    private int id;
    private String name;
    private String number;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_NUMBER + " TEXT"
                    + ")";

    public block() {
    }

    public block(int id, String name,String number) {
        this.id = id;
        this.name = name;
        this.number = number;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
