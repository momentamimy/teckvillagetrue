package teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit;

import java.util.List;

import retrofit2.http.POST;

/**
 * Created by khaled-pc on 4/7/2019.
 */

public class ResultModel {
    private DataReceived user;

    public DataReceived getUser() {
        return user;
    }

    public void setUser(DataReceived user) {
        this.user = user;
    }
}
