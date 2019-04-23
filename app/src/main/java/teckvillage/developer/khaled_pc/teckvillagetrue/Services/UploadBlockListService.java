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
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.tables.block;
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
    Database_Helper db;

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


            db=new Database_Helper(this);

            ArrayList<Send_BlockList_JSON_Arraylist> listts = new ArrayList<>();

            ArrayList<Send_BlockList_JSON_Arraylist> Blocklist = new ArrayList<>();;

            //Get data from database
            Blocklist=db.getAllBlocklistUpload();

            //Check if Empty
            if (Blocklist == null||Blocklist.size()<=0){
                Log.e(TAG, "onHandleWork: Invalid ");
                //Make add Block
                SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("UploadBlockList",false);
                editor.apply();
                return;
            }

            //Add all
            for(int i = 0; i< Blocklist.size(); i++){
                listts.add(new Send_BlockList_JSON_Arraylist(Blocklist.get(i).getPhone(),Blocklist.get(i).getType()));
            }


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