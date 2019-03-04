package teckvillage.developer.khaled_pc.teckvillagetrue.model;

import java.util.List;

/**
 * Created by khaled-pc on 2/16/2019.
 */

public class UserContactData {

    public String userimageUrl;
    public String usercontacName;
    public String country;
    public String phoneNum;
    int type;
    Long id;
    List<String> contactPhones;

    public UserContactData(String userimageUrl,String usercontacName,String country,Long id)
    {
        this.userimageUrl=userimageUrl;
        this.usercontacName=usercontacName;
        this.country=country;
        this.id=id;
    }

    public UserContactData(String userimageUrl,String usercontacName,String country,Long id,String phoneNum)
    {
        this.userimageUrl=userimageUrl;
        this.usercontacName=usercontacName;
        this.country=country;
        this.id=id;
        this.phoneNum=phoneNum;
    }

    public UserContactData(String userimageUrl,String usercontacName,String country,Long id,List<String> contactPhones)
    {
        this.userimageUrl=userimageUrl;
        this.usercontacName=usercontacName;
        this.country=country;
        this.id=id;
        this.contactPhones=contactPhones;
    }


    public UserContactData() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
