package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping;

/**
 * Created by khaled-pc on 4/12/2019.
 */

public class Item_Search {
    long id;
    String name,phone,full_phone;


    public String getFull_phone() {
        return full_phone;
    }

    public void setFull_phone(String full_phone) {
        this.full_phone = full_phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
