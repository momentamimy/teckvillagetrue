package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khaled-pc on 4/11/2019.
 */

public class Send_BlockList_JSON_Arraylist {


    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("type")
    @Expose
    private String type;



    public Send_BlockList_JSON_Arraylist(String phone, String type) {

        this.phone = phone;
        this.type = type;
    }

    public Send_BlockList_JSON_Arraylist() {

    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
