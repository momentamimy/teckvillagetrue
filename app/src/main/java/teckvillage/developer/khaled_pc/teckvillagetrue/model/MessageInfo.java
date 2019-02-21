package teckvillage.developer.khaled_pc.teckvillagetrue.model;

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

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String imageUrl;
    public String logName;
    public String logMessage;
    public String logDate;
    public String logAddress;
    public MessageInfo(String imageUrl,String logName,String logMessage,String logDate,String logAddress)
    {
        this.imageUrl=imageUrl;
        this.logName=logName;
        this.logMessage=logMessage;
        this.logDate=logDate;
        this.logAddress=logAddress;
    }
}
