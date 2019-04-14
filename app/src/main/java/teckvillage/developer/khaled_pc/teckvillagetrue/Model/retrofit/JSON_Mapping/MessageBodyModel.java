package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping;

/**
 * Created by khaled-pc on 4/7/2019.
 */

public class MessageBodyModel {


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    int receiver_id;
    String text;

    public MessageBodyModel(int receiver_id, String text) {
        this.receiver_id = receiver_id;
        this.text = text;
    }

}
