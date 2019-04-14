package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping;

import java.util.List;

/**
 * Created by khaled-pc on 4/7/2019.
 */

public class ListMessagesChatModel {
    public List<MessageChatModel> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageChatModel> messages) {
        this.messages = messages;
    }

    private List<MessageChatModel> messages;

}
