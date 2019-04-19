package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit;

import java.util.ArrayList;
import java.util.List;

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
import retrofit2.http.Query;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.BodyNumberModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.DataReceivedChatUsers;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.FetchedUserData;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Item_Search;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.NotificattionDataReceived;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ResultModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ResultModelUploadVCF;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.datamodel;

import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.GroupBodyModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.GroupChatResultModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ListMessagesChatModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.MessageBodyModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.MessageChatModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.MessageGroupBodyModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.TokenBodyModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.TokenDataReceived;

import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ResultModel_Update_User_data;



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
            @Part("country")      RequestBody  country,
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
    Call<ResultModel_Update_User_data> UptadeUserProfile(
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
            @Part("about")   RequestBody  about
    );



    @POST("createGroup?")
    Call<GroupChatResultModel> createGroup(
            @Query("api_token") String ApiAccessToken,
            @Body GroupBodyModel groupBodyModel);

    @POST("createMessage?")
    Call<MessageChatModel> sendMessage(
            @Query("api_token") String ApiAccessToken,
            @Body MessageBodyModel messageBodyModel);

    @POST("createMessage?")
    Call<MessageChatModel> sendGroupMessage(
            @Query("api_token") String ApiAccessToken,
            @Body MessageGroupBodyModel messageGroupBodyModel);

    @GET("getChatUsers?")
    Call<DataReceivedChatUsers> getallChatContact(
            @Query("api_token") String ApiAccessToken);

    @GET("getMessages?")
    Call<ListMessagesChatModel> getMessageWhenOpenChat(
            @Query("api_token") String ApiAccessToken,
            @Query("user_id") String id,
            @Query("chat_rooms_id") String chatId);


    @GET("getMessages?")
    Call<ListMessagesChatModel> getMessageWhenOpenGroupChat(
            @Query("api_token") String ApiAccessToken,
            @Query("group_id") String id,
            @Query("chat_rooms_id") String chatId);


    @POST("updateUserMobileToken?")
    Call<TokenDataReceived> uploadFirbaseToken(
            @Header("Accept") String Accept,
            @Query("api_token") String ApiAccessToken,
            @Body TokenBodyModel tokenBodyModel);


    @POST("fetchContactInfo?")
    Call<FetchedUserData> fetchUserData(
            @Query("api_token") String ApiAccessToken,
            @Body BodyNumberModel bodyNumberModel);


    @GET("notifications?")
    Call<List<NotificattionDataReceived>> getallNotification(
            @Query("api_token") String ApiAccessToken);
}
