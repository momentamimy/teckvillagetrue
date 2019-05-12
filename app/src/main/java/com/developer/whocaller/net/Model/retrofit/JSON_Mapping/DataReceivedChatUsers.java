package com.developer.whocaller.net.Model.retrofit.JSON_Mapping;

import java.util.List;

public class DataReceivedChatUsers {

    boolean response;
            String msg;
            List<RoomModel> rooms;
    List<LastMessageModel> latestMessages;

    public List<RoomModel> getRoom() {
        return rooms;
    }

    public void setRoom(List<RoomModel> room) {
        this.rooms = room;
    }



    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<LastMessageModel> getLatestMessages() {
        return latestMessages;
    }

    public void setLatestMessages(List<LastMessageModel> latestMessages) {
        this.latestMessages = latestMessages;
    }
}
