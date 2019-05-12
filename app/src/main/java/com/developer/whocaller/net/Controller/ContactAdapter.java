package com.developer.whocaller.net.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.whocaller.net.Model.ContactInfo;
import com.developer.whocaller.net.Model.retrofit.ApiAccessToken;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.BodyNumbersListModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.FetchedUserData;
import com.developer.whocaller.net.Model.retrofit.WhoCallerApi;
import com.developer.whocaller.net.Model.retrofit.retrofitHead;
import com.developer.whocaller.net.View.ContactHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {

    private static final int TYPE_ITEM_NoNeedToFetch = 0;
    private static final int TYPE_ITEM_Feched = 1;
    private static final int TYPE_ITEM_NotFeched = 2;

    private List<ContactInfo> itemList;
    Context context;

    String[] Numbers;
    public ContactAdapter(Context context, List<ContactInfo> itemList)
    {
        this.context=context;
        this.itemList=itemList;

        Numbers=new String[itemList.size()];
        Log.d("dadqfqfcsdv", ""+itemList.size());
        for (int i=0;i<itemList.size();i++)
        {
            if (!TextUtils.isEmpty(itemList.get(i).contacName))
            {
                if (itemList.get(i).contacName.equals(itemList.get(i).getPhoneNum()))
                {
                    Numbers[i]=itemList.get(i).getPhoneNum();
                    itemList.get(i).setNeedToFech(true);
                }
            }
        }
        if (Numbers.length!=0)
        {
            getUserDataApi(context,Numbers);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (itemList.get(position).isNeedToFech())
        {
            if (itemList.get(position).isUserFeched()){
                viewType = TYPE_ITEM_Feched;
            }else{
                viewType = TYPE_ITEM_NotFeched;
            }
        }
        else
        {
            viewType =TYPE_ITEM_NoNeedToFetch;
        }
        return viewType;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_of_most_important_contact_recycleview, null);
        ContactHolder rbv = new ContactHolder(layoutView);
        return rbv;
    }

    @Override
    public void onBindViewHolder(final ContactHolder holder, int position) {
        final ContactInfo contactInfo=itemList.get(position);
        holder.contactName.setText(contactInfo.contacName);
        holder.numberType.setText(contactInfo.numberType);


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + contactInfo.phoneNum));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });
        updatePhoto(contactInfo,holder);
        }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public int getLetterPosition(String letter){
        Log.w("size", String.valueOf(itemList.size()));
        for (int i = 0 ; i < itemList.size(); i++){
            Log.w("let", String.valueOf(itemList.get(i).contacName.substring(0,1)));
            if(itemList.get(i).contacName.substring(0,1).equals(letter)){
                Log.w("pos", String.valueOf(i));
                return i;
            }
        }
        return -1;
    }


    List<FetchedUserData> fetchedUserDataList=new ArrayList<>();
    public void getUserDataApi(final Context context, String[] numbers) {

        BodyNumbersListModel bodyNumbersListModel = new BodyNumbersListModel(numbers);
        Retrofit retrofit = retrofitHead.retrofitRequestCash(context);
        WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
        Call<List<FetchedUserData>> usersDataCall = whoCallerApi.fetchUsersData(ApiAccessToken.getAPIaccessToken(context), bodyNumbersListModel);

        usersDataCall.enqueue(new Callback<List<FetchedUserData>>() {
            @Override
            public void onResponse(Call<List<FetchedUserData>> call, Response<List<FetchedUserData>> response) {
                if (response.raw().cacheResponse() != null) {
                    Log.e("MyNetwork", "response came from cache");
                }

                if (response.raw().networkResponse() != null) {
                    Log.e("MyNetwork", "response came from server");
                }

                if (response.isSuccessful())
                {
                    for (int i=0;i<response.body().size();i++)
                        Log.d("userNamePaleeez", response.body().get(i).getName()+"  "+response.body().get(i).getPhone());
                    List<FetchedUserData> resp =response.body();
                    fetchedUserDataList=resp;

                    //Collections.reverse(fetchedUserDataList);
                    if (fetchedUserDataList.size()!=0) {
                        for (int i=0;i<itemList.size();i++) {
                            if (itemList.get(i).isNeedToFech()) {
                                for (int j=0;j<fetchedUserDataList.size();j++) {
                                    if (itemList.get(i).getPhoneNum().equals(fetchedUserDataList.get(j).getFull_phone())
                                            || itemList.get(i).getPhoneNum().equals(fetchedUserDataList.get(j).getPhone())) {

                                        if (!TextUtils.isEmpty(fetchedUserDataList.get(j).getName()))
                                        {
                                            itemList.get(i).setContacName(fetchedUserDataList.get(j).getName());
                                        }
                                        if (!TextUtils.isEmpty(fetchedUserDataList.get(j).getUser_img()))
                                        {
                                            itemList.get(i).setImageUrl(fetchedUserDataList.get(j).getUser_img());
                                            itemList.get(i).setUserFeched(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    notifyItemRangeChanged(0,itemList.size()-1);
                }
                else
                {
                    Log.d("onFailure", "other error");
                }
            }

            @Override
            public void onFailure(Call<List<FetchedUserData>> call, Throwable t) {
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    public void updatePhoto(ContactInfo data, final ContactHolder holder)
    {
        if (!TextUtils.isEmpty(data.imageUrl))
        {
            if (!data.imageUrl.equals(""))
            {
                if (holder.getItemViewType()==TYPE_ITEM_Feched)
                {
                    Picasso.with(context).load("http://whocaller.net/whocallerAdmin/uploads/"+data.imageUrl)
                            .fit().centerInside()
                            .into(holder.contactCircleImageView, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {

                                }
                                @Override
                                public void onError() {
                                    holder.contactCircleImageView.setImageResource(R.drawable.ic_nurse);
                                }
                            });
                }
            }
        }
    }
}
