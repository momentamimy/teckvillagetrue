package teckvillage.developer.khaled_pc.teckvillagetrue;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.ContactAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.LogAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.ContactInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Fagment extends Fragment {

    RecyclerView contacts,logs;
    LinearLayoutManager lLayout;
    LinearLayoutManager lLayout1;

    public Main_Fagment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main__fagment, container, false);


        contacts=view.findViewById(R.id.contact_recycleview);
        logs=view.findViewById(R.id.Logs_recycleview);


        lLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        lLayout1 = new LinearLayoutManager(getActivity());

        List<ContactInfo> contactInfos=new ArrayList<>();

        contactInfos.add(new ContactInfo("http://i.imgur.com/DvpvklR.png","khaled","Mobile"));
        contactInfos.add(new ContactInfo("https://firebasestorage.googleapis.com/v0/b/chatapp-f5386.appspot.com/o/imgs%2F3C8oCUSQjlZqV0kivIBhpwT5fca2.jpg?alt=media&token=a9e39edd-6d27-4995-969d-a76ddbfb1aab","khaled","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled eltarabily","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));

        contacts.setLayoutManager(lLayout);
        contacts.setItemAnimator(new DefaultItemAnimator());
        ContactAdapter adapter=new ContactAdapter(getActivity(),contactInfos);
        contacts.setAdapter(adapter);



        List<LogInfo> logInfos=new ArrayList<>();
        logInfos.add(new LogInfo("http://i.imgur.com/DvpvklR.png","khaled","income",1235465464,"Mobile"));
        logInfos.add(new LogInfo("https://firebasestorage.googleapis.com/v0/b/chatapp-f5386.appspot.com/o/imgs%2F3C8oCUSQjlZqV0kivIBhpwT5fca2.jpg?alt=media&token=a9e39edd-6d27-4995-969d-a76ddbfb1aab","khaled","income",1235465464,"Mobile"));
        logInfos.add(new LogInfo(null,"khaled","income",1235465464,"Mobile"));
        logInfos.add(new LogInfo(null,"momen","outcome",1235465464,"Mobile"));
        logInfos.add(new LogInfo(null,"khaled","income",1235465464,"Mobile"));
        logInfos.add(new LogInfo(null,"momen","outcome",1235465464,"Mobile"));

        logs.setLayoutManager(lLayout1);
        logs.setItemAnimator(new DefaultItemAnimator());
        LogAdapter adapter1=new LogAdapter(getActivity(),logInfos);
        logs.setAdapter(adapter1);


        return view;
    }

}
