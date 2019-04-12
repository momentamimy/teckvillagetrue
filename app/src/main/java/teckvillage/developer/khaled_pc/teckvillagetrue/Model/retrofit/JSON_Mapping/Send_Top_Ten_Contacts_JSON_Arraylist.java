package teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khaled-pc on 4/11/2019.
 */

public class Send_Top_Ten_Contacts_JSON_Arraylist {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phones")
    @Expose
    private ArrayList<String> phones = new ArrayList<String>();;

    public Send_Top_Ten_Contacts_JSON_Arraylist(int id, String name, ArrayList<String> phones) {
        this.id = id;
        this.name = name;
        this.phones = phones;
    }

    public Send_Top_Ten_Contacts_JSON_Arraylist() {

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

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }
}
