package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by khaled-pc on 4/11/2019.
 */

public class datamodelBlocklist {

    @SerializedName("block")
    @Expose
    private ArrayList<Send_BlockList_JSON_Arraylist> block;

    public ArrayList<Send_BlockList_JSON_Arraylist> getBlock() {
        return block;
    }

    public void setBlock(ArrayList<Send_BlockList_JSON_Arraylist> block) {
        this.block = block;
    }
}
