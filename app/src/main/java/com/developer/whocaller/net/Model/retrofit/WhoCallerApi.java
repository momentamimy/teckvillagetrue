package com.developer.whocaller.net.Model.retrofit;

import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.BodyNumberModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.BodyNumbersListModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.BodyUpdateGroup;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.FetchedUserData;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.GroupBodyModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.GroupChatResultModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.InitialDataModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.Item_Search;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.ListMessagesChatModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.MessageBodyModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.MessageGroupBodyModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.NotificattionDataReceived;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.ResultModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.ResultModelUploadVCF;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.ResultModel_Update_User_data;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.TokenBodyModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.TokenDataReceived;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.datamodel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.datamodelBlocklist;

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
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.DataReceivedChatUsers;

import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.MessageChatModel;


/**
 * Created by khaled-pc on 4/7/2019.
 */

public interface WhoCallerApi {




    @GET("initial")
    Call<InitialDataModel> GetInitial();

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
            @Header("Accept") String Accept,
            @Query("api_token") String ApiAccessToken,
            @Part MultipartBody.Part vcfFile
    );



    @POST("addUserMostCalled")
    Call<ResultModelUploadVCF> UploadTopTenContacts(
            @Header("Accept") String Accept,
            @Query("api_token") String ApiAccessToken,
            @Body datamodel contacts
    );

    @POST("addOrEditContact")
    Call<ResultModelUploadVCF> UploadNewContacts(
            @Header("Accept") String Accept,
            @Query("api_token") String ApiAccessToken,
            @Body datamodel contact
    );

    @POST("updateUserBlock")
    Call<ResultModelUploadVCF> UploadBlockListRequest(
            @Header("Accept") String Accept,
            @Query("api_token") String ApiAccessToken,
            @Body datamodelBlocklist block
    );

    @FormUrlEncoded
    @POST("searchContact")
    Call<ArrayList<Item_Search>> SearchPhoneNumber(
            @Query("api_token") String ApiAccessToken,
            @Field("number") String phoneNumber,
            @Field("code") String code,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("searchContact")
    Call<ArrayList<Item_Search>> SearchName(
            @Query("api_token") String ApiAccessToken,
            @Field("name") String phoneNumber,
            @Field("code") String code,
            @Field("page") int page
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
            @Part("about")   RequestBody  about,
            @Part("remove_img")   RequestBody  removeimage
    );


    @Multipart
    @POST("updateUserData")
    Call<ResultModel_Update_User_data> UptadeUserProfileWithoutImage(
            @Query("api_token") String ApiAccessToken,
            @Part("name")       RequestBody  username,
            @Part("email")      RequestBody  email,
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
            @Query("chat_rooms_id") String chatId,
            @Query("page") String page);


    @GET("getMessages?")
    Call<ListMessagesChatModel> getMessageWhenOpenGroupChat(
            @Query("api_token") String ApiAccessToken,
            @Query("group_id") String id,
            @Query("chat_rooms_id") String chatId,
            @Query("page") String page);

    @POST("updateGroup/{ID}?")
    Call<GroupChatResultModel> updateGroupChat(
            @Path("ID") int id,
            @Query("api_token") String ApiAccessToken,
            @Body BodyUpdateGroup bodyUpdateGroup);


    @POST("updateUserMobileToken?")
    Call<TokenDataReceived> uploadFirbaseToken(
            @Header("Accept") String Accept,
            @Query("api_token") String ApiAccessToken,
            @Body TokenBodyModel tokenBodyModel);


    @POST("fetchContactInfo?")
    Call<FetchedUserData> fetchUserData(
            @Query("api_token") String ApiAccessToken,
            @Body BodyNumberModel bodyNumberModel);

    @POST("fetchPhonesContact?")
    Call<List<FetchedUserData>> fetchUsersData(
            @Query("api_token") String ApiAccessToken,
            @Body BodyNumbersListModel bodyNumbersListModel);


    @GET("notifications?")
    Call<List<NotificattionDataReceived>> getallNotification(
            @Query("api_token") String ApiAccessToken);
}
