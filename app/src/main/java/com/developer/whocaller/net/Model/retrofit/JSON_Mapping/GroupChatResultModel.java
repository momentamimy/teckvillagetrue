package com.developer.whocaller.net.Model.retrofit.JSON_Mapping;

/**
 * Created by khaled-pc on 4/7/2019.
 */

public class GroupChatResultModel {
    private GroupChatModel group;
    private String chat_rooms_id;

    public String getChat_rooms_id() {
        return chat_rooms_id;
    }

    public void setChat_rooms_id(String chat_rooms_id) {
        this.chat_rooms_id = chat_rooms_id;
    }

    public GroupChatModel getGroup() {
        return group;
    }

    public void setGroup(GroupChatModel group) {
        this.group = group;
    }
}
