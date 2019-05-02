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
    Database_Helper db;

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

                                //set Share preference SpamLimit
                                setSpamLimitSharedPreference( mesf);

                                if(tags!=null){
                                    //insert database Tags and delete previous data
                                    insertTags(tags);

                                }


                            }else {
                                Log.w("onNotSuccess", "sss");
                            }

                        }

                        @Override
                        public void onFailure(Call<InitialDataModel> call, Throwable t) {
                            Log.w("onFailure", t.getMessage());
                            Log.w("onFailure", t.getCause());
                        }
                    });


                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void insertTags(List<ItemsResultInitail> tags) {
        db=new Database_Helper(this);
        db.DeleteAllDataTagsTable();

        for(int i=0;i<tags.size();i++){

            Log.w("tagsid", String.valueOf(tags.get(i).getId())+" || "+tags.get(i).getName());
            db.insertTag(tags.get(i).getId(),tags.get(i).getParent_id(),tags.get(i).getName());

        }

    }


    void setSpamLimitSharedPreference(int spamlimit){
        SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("SpamLimit", spamlimit);
        editor.apply();

    }

}