package teckvillage.developer.khaled_pc.teckvillagetrue.model;

/**
 * Created by khaled-pc on 2/16/2019.
 */

public class UserContactData {

    public String userimageUrl;
    public String usercontacName;
    public String country;
    int type;

    public UserContactData(String userimageUrl,String usercontacName,String country)
    {
        this.userimageUrl=userimageUrl;
        this.usercontacName=usercontacName;
        this.country=country;
    }

    public UserContactData() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
