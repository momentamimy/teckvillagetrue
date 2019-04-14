package teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping;

/**
 * Created by khaled-pc on 4/11/2019.
 */

public class Send_Top_Ten_Contacts_JSON {

    String name;
    String phones;


    public Send_Top_Ten_Contacts_JSON( String name, String phones) {

        this.name = name;
        this.phones = phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getPhones() {
        return phones;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
