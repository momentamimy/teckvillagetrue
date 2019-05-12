package com.developer.whocaller.net.Model.retrofit.JSON_Mapping;

/**
 * Created by khaled-pc on 4/11/2019.
 */

public class ResultModelUploadVCF {
    boolean response;
    String msg;

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
}
