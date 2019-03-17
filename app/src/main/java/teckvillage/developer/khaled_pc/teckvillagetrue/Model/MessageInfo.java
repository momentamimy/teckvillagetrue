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
    public MessageInfo(String imageUrl, String logName, String logMessage, String logDate, String logAddress, String  read) {
        this.imageUrl = imageUrl;
        this.logName = logName;
        this.logMessage = logMessage;
        this.logDate = logDate;
        this.logAddress = logAddress;
        this.read = read;
    }

}
