package com.developer.whocaller.net.Model;

public class ContactInfo {
    public String imageUrl;
    public String contacName;
    public String numberType;
    public String phoneNum;

    public ContactInfo(String imageUrl,String contacName,String numberType,String phoneNum)
    {
        this.imageUrl=imageUrl;
        this.contacName=contacName;
        this.numberType=numberType;
        this.phoneNum=phoneNum;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContacName() {
        return contacName;
    }

    public void setContacName(String contacName) {
        this.contacName = contacName;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }


    boolean isUserFeched=false;

    public boolean isUserFeched() {
        return isUserFeched;
    }

    public void setUserFeched(boolean userFeched) {
        isUserFeched = userFeched;
    }

    boolean needToFech=false;

    public boolean isNeedToFech() {
        return needToFech;
    }

    public void setNeedToFech(boolean needToFech) {
        this.needToFech = needToFech;
    }
}
