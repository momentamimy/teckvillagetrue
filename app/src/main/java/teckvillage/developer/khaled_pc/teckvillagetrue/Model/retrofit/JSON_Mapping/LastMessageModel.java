package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping;

public class LastMessageModel {
int chatRoomId;
        String type;
        int id;
        String name;
        String img;
        String message;
        DateModel date;

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DateModel getDate() {
        return date;
    }

    public void setDate(DateModel date) {
        this.date = date;
    }
}
