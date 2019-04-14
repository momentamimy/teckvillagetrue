package teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.Item_Search;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.ResultModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.ResultModelUploadVCF;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.Send_Top_Ten_Contacts_JSON_Arraylist;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.datamodel;

/**
 * Created by khaled-pc on 4/7/2019.
 */

public interface WhoCallerApi {



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


    @Multipart
    @POST("addUserContacts")
    Call<ResultModelUploadVCF> UploadVCF(
            @Query("api_token") String ApiAccessToken,
            @Part MultipartBody.Part vcfFile
    );



    @POST("addUserMostCalled")
    Call<ResultModelUploadVCF> UploadTopTenContacts(
            @Header("Accept") String Accept,
            @Query("api_token") String ApiAccessToken,
            @Body datamodel contacts
            );

    @FormUrlEncoded
    @POST("searchContact")
    Call<ArrayList<Item_Search>> SearchPhoneNumber(
                    @Query("api_token") String ApiAccessToken,
                    @Field("number") String phoneNumber,
                    @Field("code") String code
            );

    @FormUrlEncoded
    @POST("searchContact")
    Call<ArrayList<Item_Search>> SearchName(
            @Query("api_token") String ApiAccessToken,
            @Field("name") String phoneNumber,
            @Field("code") String code
    );

    @Multipart
    @POST("updateUserData")
    Call<ResultModel> UptadeUserProfile(
            @Query("api_token") String ApiAccessToken,
            @Part("name")       RequestBody  username,
            @Part("email")      RequestBody  email,
            @Part MultipartBody.Part img,
            @Part("tag_id") RequestBody  TagID,
            @Part("company") RequestBody  company,
            @Part("title") RequestBody  title,
            @Part("address")      RequestBody  address,
            @Part("website") RequestBody  website,
            @Part("gender") RequestBody  gender,
            @Part("about")   RequestBody  about,
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
