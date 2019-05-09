package teckvillage.developer.khaled_pc.teckvillagetrue.Model;

public class PhoneContactNumbersModel {

    String Id;
    String PhoneNumber;
    String ContactName;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public PhoneContactNumbersModel(String id, String phoneNumber, String contactName) {
        Id = id;
        PhoneNumber = phoneNumber;
        ContactName = contactName;
    }
}
