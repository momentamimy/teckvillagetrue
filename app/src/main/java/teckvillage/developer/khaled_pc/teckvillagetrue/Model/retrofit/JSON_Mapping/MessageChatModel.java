package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping;

/**
 * Created by khaled-pc on 4/7/2019.
 */

public class MessageChatModel {
 int id,sender_id,receiver_id,group_id;String text,seen, created_at, updated_at;

    public MessageChatModel(int receiver_id, String text,String created_at) {
        this.receiver_id = receiver_id;
        this.text = text;
        this.created_at=created_at;
    }

    public MessageChatModel(int id, int sender_id, int receiver_id, int group_id, String text, String seen, String created_at, String updated_at) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.group_id = group_id;
        this.text = text;
        this.seen = seen;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
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
