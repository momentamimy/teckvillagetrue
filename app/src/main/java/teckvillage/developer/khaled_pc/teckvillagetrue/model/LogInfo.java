package teckvillage.developer.khaled_pc.teckvillagetrue.model;

public class LogInfo {
    public String imageUrl;
    public String logName;
    public String logIcon;
    public String logDate;
    public String callType;
    public LogInfo(String imageUrl,String logName,String logIcon,String logDate,String callType)
    {
        this.imageUrl=imageUrl;
        this.logName=logName;
        this.logIcon=logIcon;
        this.logDate=logDate;
        this.callType=callType;
    }
}
