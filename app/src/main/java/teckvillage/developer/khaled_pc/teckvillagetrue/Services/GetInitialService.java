package teckvillage.developer.khaled_pc.teckvillagetrue.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.InitialDataModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ItemsResultInitail;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ResultModelUploadVCF;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Send_BlockList_JSON_Arraylist;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.datamodelBlocklist;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.CheckNetworkConnection;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ConnectionDetector;

/**
 * Created by khaled-pc on 4/23/2019.
 */

public class GetInitialService extends JobIntentService {

    private static final String TAG = "GETINITIAL";


    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 141;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, GetInitialService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        /**
         * Download/Upload of file
         * The system or framework is already holding a wake lock for us at this point
         */

        try {




              //Check wifi or data available
            if (CheckNetworkConnection.hasInternetConnection(this)) {

                //Check internet Access
                if (ConnectionDetector.hasInternetConnection(this)) {


                    Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                    WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                    Call<InitialDataModel> GetInitialData = whoCallerApi.GetInitial();

                    GetInitialData.enqueue(new Callback<InitialDataModel>() {
                        @Override
                        public void onResponse(Call<InitialDataModel> call, Response<InitialDataModel> response) {

                            if(response.isSuccessful()){

                                int mesf = response.body().getSpamLimit();
                                List<ItemsResultInitail> tags  = response.body().getTags();
                                Log.w("successgetsPamNumbers", String.valueOf(mesf));
                                Log.w("success", String.valueOf(tags));


                            }

                        }

                        @Override
                        public void onFailure(Call<InitialDataModel> call, Throwable t) {
                           // Log.w("onFailure", t.getMessage());
                           // Log.w("onFailure", t.getCause());
                        }
                    });


                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}