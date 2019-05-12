package com.developer.whocaller.net.Model;

public class MessageInfo {
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public long getLogDate() {
        return logDate;
    }

    public void setLogDate(long logDate) {
        this.logDate = logDate;
    }

    public String imageUrl;
    public String logName;
    public String logMessage;
    public long logDate;
    public String logAddress;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String type;

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String typeMessage;

    public String getLogAddress() {
        return logAddress;
    }

    public void setLogAddress(String logAddress) {
        this.logAddress = logAddress;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String read;

    public String thread_id;
    public MessageInfo(String imageUrl, String logName, String logMessage, long logDate, String logAddress, String  read) {
        this.imageUrl = imageUrl;
        this.logName = logName;
        this.logMessage = logMessage;
        this.logDate = logDate;
        this.logAddress = logAddress;
        this.read = read;

    }

    public MessageInfo(String imageUrl, String logName, String logMessage, long logDate, String logAddress, String  read,String thread_id) {
        this.imageUrl = imageUrl;
        this.logName = logName;
        this.logMessage = logMessage;
        this.logDate = logDate;
        this.logAddress = logAddress;
        this.read = read;
        this.thread_id = thread_id;

    }

    public MessageInfo(String imageUrl,String logName,String logMessage,long logDate)
    {
        this.imageUrl = imageUrl;
        this.logName = logName;
        this.logMessage = logMessage;
        this.logDate=logDate;
    }
}
