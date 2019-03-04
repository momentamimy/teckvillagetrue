package teckvillage.developer.khaled_pc.teckvillagetrue.Model;

import java.util.Comparator;
import java.util.Date;

public class LogInfo {
    public String imageUrl;
    public String logName;
    public String logIcon;
    public Date logDate;
    public String callType;
    public String hour;
    String dateString;
    String number;
    int type;

    public LogInfo(String imageUrl,String logName,String logIcon,Date logDate,String callType,String hour,String number)
    {
        this.imageUrl=imageUrl;
        this.logName=logName;
        this.logIcon=logIcon;
        this.logDate=logDate;
        this.callType=callType;
        this.hour=hour;
        this.number=number;
    }

    public LogInfo()
    {

    }

    public static final Comparator<LogInfo> BY_DATE = new Comparator<LogInfo>() {
        @Override
        public int compare(LogInfo cases, LogInfo t1) {

            return t1.logDate.compareTo(cases.logDate);
        }
    };

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
