package com.developer.whocaller.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.developer.whocaller.net.Controller.LocaleHelper;
import com.developer.whocaller.net.View.ConnectionDetector;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.developer.whocaller.net.Controller.RecycleviewResult_SearchAPIAdapter;
import com.developer.whocaller.net.Model.retrofit.ApiAccessToken;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.Item_Search;
import com.developer.whocaller.net.Model.retrofit.WhoCallerApi;
import com.developer.whocaller.net.Model.retrofit.retrofitHead;
import com.developer.whocaller.net.View.CheckNetworkConnection;

public class RecycleviewResult_SearchAPI extends AppCompatActivity {

     RecyclerView resultRecycle;
    LinearLayoutManager lLayout;
    RecycleviewResult_SearchAPIAdapter adapter;
    TextView textnoresult;
    ArrayList<Item_Search> resultList=new ArrayList<>();
    String searchvalue,Codecountry;
    String searchMethod;
    ProgressDialog mProgressDialog;


    //Pagination
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    //private int TOTAL_PAGES = 3; //your total page
    private int currentPage = PAGE_START;

   final String TAG="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview_result__search_api);

        getIntentDetails();

        getSupportActionBar().setTitle(getResources().getString(R.string.search_result));
        getSupportActionBar().setDisplayShowHomeEnabled(true);   //back button on App Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //back button on App Bar

        resultRecycle=findViewById(R.id.result_searchapi_recycle);
        textnoresult=findViewById(R.id.noresultsearch);

        //Start Loading
        mProgressDialog = new ProgressDialog(RecycleviewResult_SearchAPI.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.show();

        //Check wifi or data available
        if (CheckNetworkConnection.hasInternetConnection(RecycleviewResult_SearchAPI.this)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(RecycleviewResult_SearchAPI.this)) {


                initViews();


            }else {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Internet not access Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }

        }else {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();


            TastyToast.makeText(RecycleviewResult_SearchAPI.this, "You're offline. Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }


    }

    private void initViews() {



        resultRecycle.setHasFixedSize(true);
        LinearLayoutManager  layoutManager = new LinearLayoutManager(RecycleviewResult_SearchAPI.this);
        resultRecycle.setLayoutManager(layoutManager);
        resultRecycle.setItemAnimator(new DefaultItemAnimator());

        adapter = new RecycleviewResult_SearchAPIAdapter(this);
        resultRecycle.setAdapter(adapter);


        /*if(searchMethod.equals("searchMethodName")){
            loadJSONMethodName(currentPage);
        }else if(searchMethod.equals("searchMethodPhoneNumber")){
            loadJSONMethodPhoneNumber(currentPage);
        }*/

        resultRecycle.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                currentPage += 1;

                        // mocking network delay for API call
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if(searchMethod.equals("searchMethodName")){
                                    loadNextPage();
                                }else if(searchMethod.equals("searchMethodPhoneNumber")){
                                    loadNextPageNumber();
                                }

                            }
                        }, 1000);


            }


            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        // mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(searchMethod.equals("searchMethodName")){
                    loadFirstPage();
                }else if(searchMethod.equals("searchMethodPhoneNumber")){
                    loadFirstPageNumber();
                }
            }
        }, 1000);


    }



    private void loadJSONMethodName(int page) {

        mProgressDialog = new ProgressDialog(RecycleviewResult_SearchAPI.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.show();

        Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
        WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
        Call<ArrayList<Item_Search>> searchByName = whoCallerApi.SearchName(ApiAccessToken.getAPIaccessToken(RecycleviewResult_SearchAPI.this),searchvalue,Codecountry,page);

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

    private void loadJSONMethodPhoneNumber(int page) {
        mProgressDialog = new ProgressDialog(RecycleviewResult_SearchAPI.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.show();

        Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
        WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
        Log.w("countrycode",Codecountry);
        Call<ArrayList<Item_Search>> searchByNumber = whoCallerApi.SearchPhoneNumber(ApiAccessToken.getAPIaccessToken(RecycleviewResult_SearchAPI.this),searchvalue,Codecountry,page);

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

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
        WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
        Log.w("countrycode",Codecountry);
        Call<ArrayList<Item_Search>> searchByNumber = whoCallerApi.SearchName(ApiAccessToken.getAPIaccessToken(RecycleviewResult_SearchAPI.this),searchvalue,Codecountry,currentPage);

        searchByNumber.enqueue(new Callback<ArrayList<Item_Search>>() {
            @Override
            public void onResponse(Call<ArrayList<Item_Search>> call, Response<ArrayList<Item_Search>> response) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if(response.isSuccessful()){

                    resultList=response.body();

                    if(resultList!=null&&resultList.size()>0){

                        for(int i = 0; i< resultList.size(); i++){

                            Log.w("success", resultList.get(i).getName());
                            Log.w("success", resultList.get(i).getPhone());
                            Log.w("success", String.valueOf(resultList.get(i).getId()));
                        }
                        Log.w("firstloadsuecress","firstloadsuecress");

                        //adapter.addAll(resultList);

                        //adapter.addLoadingFooter();

                        if(resultList.size()<10){

                            adapter.addAll(resultList);
                            isLastPage = true;

                        }else {

                            adapter.addAll(resultList);

                            adapter.addLoadingFooter();
                        }


                        //adapter = new RecycleviewResult_SearchAPIAdapter(RecycleviewResult_SearchAPI.this, resultList);
                        //resultRecycle.setAdapter(adapter);
                        //toggleEmptyRecycle(resultList);
                    }else {
                        toggleEmptyRecycle(resultList);
                        Log.w("No reuslt","No reuslt");
                    }

                }else {
                    toggleEmptyRecycle(resultList);
                    TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again in at Other Time", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Item_Search>> call, Throwable t) {
                Log.w("onFailure", t.toString());

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                isLastPage = true;

                toggleEmptyRecycle(resultList);

                TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);

            }
        });



    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        //Check wifi or data available
        if (CheckNetworkConnection.hasInternetConnection(RecycleviewResult_SearchAPI.this)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(RecycleviewResult_SearchAPI.this)) {


                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Log.w("countrycode",Codecountry);
                Call<ArrayList<Item_Search>> searchByNumber = whoCallerApi.SearchName(ApiAccessToken.getAPIaccessToken(RecycleviewResult_SearchAPI.this),searchvalue,Codecountry,currentPage);

                searchByNumber.enqueue(new Callback<ArrayList<Item_Search>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Item_Search>> call, Response<ArrayList<Item_Search>> response) {

                        if(response.isSuccessful()){

                            resultList=response.body();

                            //remove loading progress bar
                            adapter.removeLoadingFooter();
                            isLoading = false;
                            Log.w("removeLoadingFooter","removeLoadingFooter");

                            if(resultList!=null&&resultList.size()>0){

                                for(int i = 0; i< resultList.size(); i++){

                                    Log.w("success", resultList.get(i).getName());
                                    Log.w("success", resultList.get(i).getPhone());
                                    Log.w("success", String.valueOf(resultList.get(i).getId()));
                                }

                                Log.w("suecceloadaddmore","suecceloadaddmore");
                                if(resultList.size()<10){

                                    adapter.addAll(resultList);
                                    isLastPage = true;

                                }else {

                                    adapter.addAll(resultList);

                                    adapter.addLoadingFooter();

                                }



                            }else {

                                Log.w("responeReturned","zeroSizeLastresult");
                                isLastPage = true;
                            }

                        }else {

                            Log.w("ErrorSERVER","ErrorSERVER");


                            if (isLoading){ adapter.removeLoadingFooter(); isLoading = false; isLastPage = true; }

                            TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again in at Other Time", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Item_Search>> call, Throwable t) {
                        Log.w("onFailure", t.toString());

                        Log.w("requestfiald","requestfiald");

                        if (isLoading){ adapter.removeLoadingFooter(); isLoading = false; isLastPage = true; }


                        TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);


                    }
                });



      }else {
                Log.w("Internet nernet","Internet not access Please connect to the internet");
                if (isLoading){ adapter.removeLoadingFooter(); isLoading = false; isLastPage = true; }
                TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Internet not access Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
      }

    }else {
            Log.w("Intt","You're offline.");
            if (isLoading){ adapter.removeLoadingFooter(); isLoading = false; isLastPage = true; }
            TastyToast.makeText(RecycleviewResult_SearchAPI.this, "You're offline. Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
    }


    }

    private void loadFirstPageNumber() {
        Log.d(TAG, "loadFirstPage: ");

        Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
        WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
        Log.w("countrycode",Codecountry);
        Call<ArrayList<Item_Search>> searchByNumber = whoCallerApi.SearchPhoneNumber(ApiAccessToken.getAPIaccessToken(RecycleviewResult_SearchAPI.this),searchvalue,Codecountry,currentPage);

        searchByNumber.enqueue(new Callback<ArrayList<Item_Search>>() {
            @Override
            public void onResponse(Call<ArrayList<Item_Search>> call, Response<ArrayList<Item_Search>> response) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if(response.isSuccessful()){

                    resultList=response.body();

                    if(resultList!=null&&resultList.size()>0){

                        for(int i = 0; i< resultList.size(); i++){

                            Log.w("success", resultList.get(i).getName());
                            if(resultList.get(i).getPhone()!=null){
                                Log.w("success", resultList.get(i).getPhone());
                            }else {
                                if(resultList.get(i).getFull_phone()!=null){
                                    Log.w("success", resultList.get(i).getFull_phone());
                                }
                            }

                            Log.w("success", String.valueOf(resultList.get(i).getId()));
                        }
                        Log.w("firstloadsuecress","firstloadsuecress");

                        //adapter.addAll(resultList);

                        //adapter.addLoadingFooter();

                        if(resultList.size()<10){

                            adapter.addAll(resultList);
                            isLastPage = true;

                        }else {

                            adapter.addAll(resultList);

                            adapter.addLoadingFooter();
                        }


                        //adapter = new RecycleviewResult_SearchAPIAdapter(RecycleviewResult_SearchAPI.this, resultList);
                        //resultRecycle.setAdapter(adapter);
                        //toggleEmptyRecycle(resultList);
                    }else {
                        toggleEmptyRecycle(resultList);
                        Log.w("No reuslt","No reuslt");
                    }

                }else {
                    toggleEmptyRecycle(resultList);
                    TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again in at Other Time", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Item_Search>> call, Throwable t) {
                Log.w("onFailure", t.toString());

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                isLastPage = true;

                toggleEmptyRecycle(resultList);

                TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);

            }
        });



    }

    private void loadNextPageNumber() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        //Check wifi or data available
        if (CheckNetworkConnection.hasInternetConnection(RecycleviewResult_SearchAPI.this)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(RecycleviewResult_SearchAPI.this)) {


                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Log.w("countrycode",Codecountry);
                Call<ArrayList<Item_Search>> searchByNumber = whoCallerApi.SearchPhoneNumber(ApiAccessToken.getAPIaccessToken(RecycleviewResult_SearchAPI.this),searchvalue,Codecountry,currentPage);

                searchByNumber.enqueue(new Callback<ArrayList<Item_Search>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Item_Search>> call, Response<ArrayList<Item_Search>> response) {

                        if(response.isSuccessful()){

                            resultList=response.body();

                            //remove loading progress bar
                            adapter.removeLoadingFooter();
                            isLoading = false;
                            Log.w("removeLoadingFooter","removeLoadingFooter");

                            if(resultList!=null&&resultList.size()>0){

                                for(int i = 0; i< resultList.size(); i++){

                                    Log.w("success", resultList.get(i).getName());
                                    Log.w("success", resultList.get(i).getPhone());
                                    Log.w("success", String.valueOf(resultList.get(i).getId()));
                                }

                                Log.w("suecceloadaddmore","suecceloadaddmore");
                                if(resultList.size()<10){

                                    adapter.addAll(resultList);
                                    isLastPage = true;

                                }else {

                                    adapter.addAll(resultList);

                                    adapter.addLoadingFooter();

                                }



                            }else {

                                Log.w("responeReturned","zeroSizeLastresult");
                                isLastPage = true;
                            }

                        }else {

                            Log.w("ErrorSERVER","ErrorSERVER");


                            if (isLoading){ adapter.removeLoadingFooter(); isLoading = false; isLastPage = true; }

                            TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again in at Other Time", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Item_Search>> call, Throwable t) {
                        Log.w("onFailure", t.toString());

                        Log.w("requestfiald","requestfiald");

                        if (isLoading){ adapter.removeLoadingFooter(); isLoading = false; isLastPage = true; }


                        TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Failed to find Data,Please Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);


                    }
                });



            }else {
                Log.w("Internet nernet","Internet not access Please connect to the internet");
                if (isLoading){ adapter.removeLoadingFooter(); isLoading = false; isLastPage = true; }
                TastyToast.makeText(RecycleviewResult_SearchAPI.this, "Internet not access Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }

        }else {
            Log.w("Intt","You're offline.");
            if (isLoading){ adapter.removeLoadingFooter(); isLoading = false; isLastPage = true; }
            TastyToast.makeText(RecycleviewResult_SearchAPI.this, "You're offline. Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }


    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
