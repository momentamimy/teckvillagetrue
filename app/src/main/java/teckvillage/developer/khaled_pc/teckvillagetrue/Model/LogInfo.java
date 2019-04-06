package teckvillage.developer.khaled_pc.teckvillagetrue.model;

import java.util.Comparator;
import java.util.Date;

public class LogInfo {
    public String imageUrl;
    public String logName;
    public String logIcon;
    public Date logDate;
    public String numberType;
    public String hour;
    String dateString;
    String number;
    int type;
    int numberofcall;


    public LogInfo(String imageUrl,String logName,String logIcon,Date logDate,String numberType,String hour,String number,int numberofcall)
    {
        this.imageUrl=imageUrl;
        this.logName=logName;
        this.logIcon=logIcon;
        this.logDate=logDate;
        this.numberType=numberType;
        this.hour=hour;
        this.number=number;
        this.numberofcall=numberofcall;
    }

    public LogInfo(String logName,String logIcon,Date logDate,String numberType,String hour,String number)
    {

        this.logName=logName;
        this.logIcon=logIcon;
        this.logDate=logDate;
        this.numberType=numberType;
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

    public String getLogIcon() {
        return logIcon;
    }

    public void setLogIcon(String logIcon) {
        this.logIcon = logIcon;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getNumberofcall() {
        return numberofcall;
    }

    public void setNumberofcall(int numberofcall) {
        this.numberofcall = numberofcall;
    }

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
