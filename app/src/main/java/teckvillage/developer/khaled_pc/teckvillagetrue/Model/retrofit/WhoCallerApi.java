package teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit;

import android.graphics.Bitmap;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
}
