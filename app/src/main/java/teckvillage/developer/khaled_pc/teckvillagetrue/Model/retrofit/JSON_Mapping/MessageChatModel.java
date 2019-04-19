package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping;

/**
 * Created by khaled-pc on 4/7/2019.
 */

public class MessageChatModel {

 int id,sender_id,receiver_id,group_id,chat_rooms_id;String text,created_at, updated_at;
 DataReceived sender;

    public MessageChatModel(int id, int sender_id, int receiver_id, int group_id, int chat_rooms_id, String text, String created_at, String updated_at, DataReceived sender, boolean status, boolean showDayDate) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.group_id = group_id;
        this.chat_rooms_id = chat_rooms_id;
        this.text = text;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.sender = sender;
        Status = status;
        ShowDayDate = showDayDate;
    }

    public MessageChatModel(int receiver_id, String text, String created_at) {
        this.receiver_id = receiver_id;
        this.text = text;
        this.created_at=created_at;
    }

    public MessageChatModel(int id, int sender_id, int receiver_id, int group_id,int chat_rooms_id, String text, String created_at, String updated_at) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.group_id = group_id;
        this.chat_rooms_id=chat_rooms_id;
        this.text = text;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public DataReceived getSender() {
        return sender;
    }

    public void setSender(DataReceived sender) {
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public int getChat_rooms_id() {
        return chat_rooms_id;
    }

    public void setChat_rooms_id(int chat_rooms_id) {
        this.chat_rooms_id = chat_rooms_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    boolean Status=false;

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }


    boolean ShowDayDate=false;

    public boolean isShowDayDate() {
        return ShowDayDate;
    }

    public void setShowDayDate(boolean showDayDate) {
        ShowDayDate = showDayDate;
    }
}
