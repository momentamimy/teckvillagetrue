package com.developer.whocaller.net.Model.retrofit.JSON_Mapping;

import java.util.List;

/**
 * Created by khaled-pc on 4/7/2019.
 */

public class ListChatUserResultModel {
    private List<DataReceived> listUsers;

    public List<DataReceived> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<DataReceived> listUsers) {
        this.listUsers = listUsers;
    }
}
