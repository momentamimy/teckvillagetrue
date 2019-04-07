package teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by khaled-pc on 4/7/2019.
 */

public class retrofitHead {

   public final static String URL="http://whocaller.net/api/";



   public static Retrofit headOfGetorPostReturnRes(){
       Gson gson = new GsonBuilder()
               .setLenient()
               .create();

           Retrofit retrofit=new Retrofit.Builder()
                   .baseUrl(URL)
                   .addConverterFactory(GsonConverterFactory.create(gson))
                   .build();

           return retrofit;
    }


   public static Retrofit headOfPost(){

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .build();

        return retrofit;
    }


}
