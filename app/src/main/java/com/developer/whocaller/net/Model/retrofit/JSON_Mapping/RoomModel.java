package com.developer.whocaller.net.Model.retrofit.JSON_Mapping;

public class RoomModel {
    int chatRoomId;
            String type;
            int id;
            String name;
            String img;
            String phone;
            int[] groupUsers;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public int[] getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(int[] groupUsers) {
        this.groupUsers = groupUsers;
    }
}
