package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by khaled-pc on 4/11/2019.
 */

public class datamodel {

    @SerializedName("contacts")
    @Expose
    private ArrayList<Send_Top_Ten_Contacts_JSON_Arraylist> contacts;

    public ArrayList<Send_Top_Ten_Contacts_JSON_Arraylist> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Send_Top_Ten_Contacts_JSON_Arraylist> contacts) {
        this.contacts = contacts;
    }
}
