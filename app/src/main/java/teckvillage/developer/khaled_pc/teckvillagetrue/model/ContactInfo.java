package teckvillage.developer.khaled_pc.teckvillagetrue.model;

import android.net.Uri;

public class ContactInfo {
    public String imageUrl;
    public String contacName;
    public String numberType;
    public ContactInfo(String imageUrl,String contacName,String numberType)
    {
        this.imageUrl=imageUrl;
        this.contacName=contacName;
        this.numberType=numberType;
    }
}
