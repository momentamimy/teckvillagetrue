package teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.ResultModel;

/**
 * Created by khaled-pc on 4/7/2019.
 */

public interface WhoCallerApi {


    //@Field("mobile_type")   String mobilebrand,
    // @Field("mobile_os")     String mobileos


 //@FormUrlEncoded
 @Multipart
 @POST("createUser")
 Call<ResultModel> registeruser(
         @Part("phone_code") RequestBody  CodeCountry,
         @Part("phone")      RequestBody  Phonenumber,
         @Part("name")       RequestBody  username,
         @Part("email")      RequestBody  email,
         @Part MultipartBody.Part img,
         @Part("register_type") RequestBody  registerMethod,
         @Part("facebook_link") RequestBody  facebooklink,
         @Part("mobile_type")   RequestBody  mobilebrand,
         @Part("mobile_os")     RequestBody  mobileos
         );


    @GET("getMessages?api_token={accesstoken}&group_id={id}")
    Call<ResultModel> getMessageWhenOpenChat(
            @Path("accesstoken") String ApiAccessToken,
            @Path("id") String id);


    @GET("getMessages?api_token={accesstoken}&user_id={id}")
    Call<ResultModel> getMessageWhenOpenGroupChat(
            @Path("accesstoken") String ApiAccessToken,
            @Path("id") String id);





}
