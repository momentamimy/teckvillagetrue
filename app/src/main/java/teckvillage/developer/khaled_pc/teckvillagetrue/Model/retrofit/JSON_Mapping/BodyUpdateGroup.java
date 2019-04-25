package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping;

public class BodyUpdateGroup {
    String name;
    int[] users;

    public BodyUpdateGroup(String name, int[] users) {
        this.name = name;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getUsers() {
        return users;
    }

    public void setUsers(int[] users) {
        this.users = users;
    }

}
