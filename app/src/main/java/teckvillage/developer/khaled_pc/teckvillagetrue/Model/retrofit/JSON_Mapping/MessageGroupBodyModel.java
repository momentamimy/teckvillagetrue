package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping;

/**
 * Created by khaled-pc on 4/7/2019.
 */

public class MessageGroupBodyModel {


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    int group_id,chat_rooms_id;
    String text;

    public MessageGroupBodyModel(int group_id, int chat_rooms_id, String text) {
        this.group_id = group_id;
        this.chat_rooms_id = chat_rooms_id;
        this.text = text;
    }
}
