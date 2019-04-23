package teckvillage.developer.khaled_pc.teckvillagetrue.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ResultModelUploadVCF;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Send_BlockList_JSON_Arraylist;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Send_Top_Ten_Contacts_JSON;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Send_Top_Ten_Contacts_JSON_Arraylist;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.datamodel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.datamodelBlocklist;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.CheckNetworkConnection;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ConnectionDetector;

/**
 * Created by khaled-pc on 4/23/2019.
 */

public class UploadBlockListService extends JobIntentService {

    private static final String TAG = "UploadBlockListNumber";


    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 141;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, UploadBlockListService.class, JOB_ID, intent);
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


            ArrayList<Send_BlockList_JSON_Arraylist> listts = new ArrayList<>();;

            /*
            //Check if Empty
            if (list == null||list.size()<=0){
                Log.e(TAG, "onHandleWork: Invalid ");
                return;
            }


            //Add all
            for(int i = 0; i< list.size(); i++){
                ArrayList<String> listss = new ArrayList<String>();
                listss.add(list.get(i).getPhones());
                listts.add(new Send_Top_Ten_Contacts_JSON_Arraylist(list.get(i).getName(),listss));
            }*/


            //Correct Solution
            datamodelBlocklist datamodelBlocklist=new datamodelBlocklist();
            datamodelBlocklist.setBlock(listts);

            //Check wifi or data available
            if (CheckNetworkConnection.hasInternetConnection(this)) {

                //Check internet Access
                if (ConnectionDetector.hasInternetConnection(this)) {


                    Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                    WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                    Call<ResultModelUploadVCF> uploadVCF = whoCallerApi.UploadBlockListRequest("application/json", ApiAccessToken.getAPIaccessToken(getApplicationContext()), datamodelBlocklist);

                    uploadVCF.enqueue(new Callback<ResultModelUploadVCF>() {
                        @Override
                        public void onResponse(Call<ResultModelUploadVCF> call, Response<ResultModelUploadVCF> response) {

                            if(response.isSuccessful()){

                                String mesf = response.body().getMsg();
                                boolean me = response.body().isResponse();
                                Log.w("success", mesf);
                                Log.w("success", String.valueOf(me));

                                //Top Ten Upload success
                                SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean("UploadBlockList",false);
                                editor.commit();

                            }

                        }

                        @Override
                        public void onFailure(Call<ResultModelUploadVCF> call, Throwable t) {
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

}