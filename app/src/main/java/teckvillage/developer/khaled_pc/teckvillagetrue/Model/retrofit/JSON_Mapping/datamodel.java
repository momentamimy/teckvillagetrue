package teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by khaled-pc on 4/11/2019.
 */

public class datamodel {
    @SerializedName("contacts")
    @Expose
    private List<Send_Top_Ten_Contacts_JSON_Arraylist> contacts;

    public List<Send_Top_Ten_Contacts_JSON_Arraylist> getContacts() {
        return contacts;
    }

    public void setContacts(List<Send_Top_Ten_Contacts_JSON_Arraylist> contacts) {
        this.contacts = contacts;
    }
}
