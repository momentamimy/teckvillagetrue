package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.ContactAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.RecycleviewResult_SearchAPIAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.Signup;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.Item_Search;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.retrofitHead;

public class RecycleviewResult_SearchAPI extends AppCompatActivity {

     RecyclerView resultRecycle;
    LinearLayoutManager lLayout;
    RecycleviewResult_SearchAPIAdapter adapter;
    TextView textnoresult;
    ArrayList<Item_Search> resultList=new ArrayList<>();
    String searchvalue,Codecountry;
    String searchMethod;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview_result__search_api);

        getIntentDetails();

        getSupportActionBar().setTitle("Search Result");
        getSupportActionBar().setDisplayShowHomeEnabled(true);   //back button on App Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //back button on App Bar

        resultRecycle=findViewById(R.id.result_searchapi_recycle);
        textnoresult=findViewById(R.id.noresultsearch);

        //lLayout = new LinearLayoutManager(RecycleviewResult_SearchAPI.this);

        //resultRecycle.setLayoutManager(lLayout);

        initViews();

    }

    private void initViews() {

        resultRecycle.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        resultRecycle.setLayoutManager(layoutManager);
        if(searchMethod.equals("searchMethodName")){
            loadJSONMethodName();
        }else if(searchMethod.equals("searchMethodPhoneNumber")){
            loadJSONMethodPhoneNumber();
        }

    }

    private void loadJSONMethodName() {

        mProgressDialog = new ProgressDialog(RecycleviewResult_SearchAPI.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
        WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
        Call<ArrayList<Item_Search>> searchByName = whoCallerApi.SearchName(ApiAccessToken.getAPIaccessToken(RecycleviewResult_SearchAPI.this),searchvalue,Codecountry);

        searchByName.enqueue(new Callback<ArrayList<Item_Search>>() {
            @Override
            public void onResponse(Call<ArrayList<Item_Search>> call, Response<ArrayList<Item_Search>> response) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if(response.isSuccessful()){

                    resultList=response.body();
                    if(resultList!=null||resultList.size()>0){
                        for(int i = 0; i< resultList.size(); i++){
                            Log.w("success", resultList.get(i).getName());
                            Log.w("success", resultList.get(i).getPhone());
                            Log.w("success", String.valueOf(resultList.get(i).getId()));
                        }

                        adapter = new RecycleviewResult_SearchAPIAdapter(RecycleviewResult_SearchAPI.this, resultList);
                        resultRecycle.setAdapter(adapter);
                        toggleEmptyRecycle(resultList);
                    }else {
                        toggleEmptyRecycle(resultList);
                    }

                }else {
                    toggleEmptyRecycle(resultList);
                    TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again in at Other Time", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Item_Search>> call, Throwable t) {
                Log.w("onFailure", t.toString());
                toggleEmptyRecycle(resultList);
                TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

            }

        });
    }

    private void loadJSONMethodPhoneNumber() {
        mProgressDialog = new ProgressDialog(RecycleviewResult_SearchAPI.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
        WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
        Log.w("countrycode",Codecountry);
        Call<ArrayList<Item_Search>> searchByNumber = whoCallerApi.SearchPhoneNumber(ApiAccessToken.getAPIaccessToken(RecycleviewResult_SearchAPI.this),searchvalue,Codecountry);

        searchByNumber.enqueue(new Callback<ArrayList<Item_Search>>() {
            @Override
            public void onResponse(Call<ArrayList<Item_Search>> call, Response<ArrayList<Item_Search>> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if(response.isSuccessful()){

                    resultList=response.body();
                    if(resultList!=null||resultList.size()>0){
                        for(int i = 0; i< resultList.size(); i++){
                            Log.w("success", resultList.get(i).getName());
                            Log.w("success", resultList.get(i).getPhone());
                            Log.w("success", String.valueOf(resultList.get(i).getId()));
                        }
                        adapter = new RecycleviewResult_SearchAPIAdapter(RecycleviewResult_SearchAPI.this, resultList);
                        resultRecycle.setAdapter(adapter);
                        toggleEmptyRecycle(resultList);
                    }else {
                        toggleEmptyRecycle(resultList);
                    }

                }else {
                    toggleEmptyRecycle(resultList);
                    TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again in at Other Time", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Item_Search>> call, Throwable t) {
                Log.w("onFailure", t.toString());
                toggleEmptyRecycle(resultList);
                TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

            }
        });

    }

    private void toggleEmptyRecycle(ArrayList<Item_Search> itemList) {
        if(itemList.size()>0){
            textnoresult.setVisibility(View.GONE);
        }else {
            textnoresult.setVisibility(View.VISIBLE);
        }
    }


    private void getIntentDetails() {
        searchvalue=getIntent().getStringExtra("searchvalue");
        Codecountry=getIntent().getStringExtra("countrycodesearch");
        searchMethod=getIntent().getStringExtra("SearchMethod");
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
