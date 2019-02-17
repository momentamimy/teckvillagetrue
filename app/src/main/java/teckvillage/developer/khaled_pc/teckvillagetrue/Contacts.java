package teckvillage.developer.khaled_pc.teckvillagetrue;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.AlphabetItem;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.ContactAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.LetterComparator;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.UserContactsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.DataHelper;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.UserContactData;


/**
 * A simple {@link Fragment} subclass.
 */
public class Contacts extends Fragment {

    RecyclerView RecyclerView;

    ContactAdapter adapter;
    IndexFastScrollRecyclerView mRecyclerView;

    private List<String> mDataArray;
    private List<AlphabetItem> mAlphabetItems;
    ArrayList<UserContactData> contactInfos;

    public Contacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        //Initial recycleview
        mRecyclerView =  view.findViewById(R.id.fast_scroller_recycler);

        contactInfos = new ArrayList<>();

        contactInfos.add(new UserContactData("","AD","ASD"));
        contactInfos.add(new UserContactData(null, "Boda", "Mobile"));
        contactInfos.add(new UserContactData(null, "Mahmoud", "Mobile"));
        contactInfos.add(new UserContactData(null, "cea eltarabily", "Mobile"));
        contactInfos.add(new UserContactData(null, "Dude", "Mobile"));
        contactInfos.add(new UserContactData(null, "Rolla", "Mobile"));
        contactInfos.add(new UserContactData(null, "khaled", "Mobile"));
        contactInfos.add(new UserContactData(null, "Yuda", "Mobile"));
        contactInfos.add(new UserContactData(null, "zead", "Mobile"));
        contactInfos.add(new UserContactData(null, "Mosab", "Mobile"));
        contactInfos.add(new UserContactData(null, "Mumen", "Mobile"));
        contactInfos.add(new UserContactData(null, "ola", "Mobile"));
        contactInfos.add(new UserContactData(null, "illa", "Mobile"));
        contactInfos.add(new UserContactData(null, "Pola", "Mobile"));
        contactInfos.add(new UserContactData(null, "lhaled", "Mobile"));
        contactInfos.add(new UserContactData(null, "qula", "Mobile"));
        contactInfos.add(new UserContactData(null, "wael", "Mobile"));
        contactInfos.add(new UserContactData(null, "sia", "Mobile"));
        contactInfos.add(new UserContactData(null, "gola", "Mobile"));
        contactInfos.add(new UserContactData(null, "eman", "Mobile"));
        contactInfos.add(new UserContactData(null, "noha", "Mobile"));
        contactInfos.add(new UserContactData(null, "fatma", "Mobile"));
        contactInfos.add(new UserContactData(null, "via", "Mobile"));
        contactInfos.add(new UserContactData(null, "joha", "Mobile"));
        contactInfos.add(new UserContactData(null, "hoda", "Mobile"));


        initialiseData();
        initialiseUI();


        return view;
    }


    protected void initialiseData() {
        //Recycler view data
        mDataArray = DataHelper.getAlphabetData();

        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < mDataArray.size(); i++) {
            String name = mDataArray.get(i);
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
    }

    protected void initialiseUI() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new UserContactsAdapters(getActivity(),addAlphabets(new LetterComparator().sortList(contactInfos))));

        mRecyclerView.setIndexTextSize(12);
        mRecyclerView.setIndexBarColor("#33334c");
        mRecyclerView.setIndexBarCornerRadius(3);
        mRecyclerView.setIndexBarTransparentValue((float) 0.0);
        mRecyclerView.setIndexbarMargin(10);
        mRecyclerView.setIndexbarWidth(50);
        mRecyclerView.setPreviewPadding(0);
        mRecyclerView.setIndexBarTextColor("#FF024B68");


        mRecyclerView.setIndexBarVisibility(true);
        mRecyclerView.setIndexbarHighLateTextColor("#013c53");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);
    }





    /**
     * Receive sorted list and divide it into Letters and contacts
     * @param list
     * @return
     */
    ArrayList<UserContactData> addAlphabets(ArrayList<UserContactData> list) {
        int i = 0;
        ArrayList<UserContactData> customList = new ArrayList<UserContactData>();
        UserContactData userContactData = new UserContactData();
        userContactData.usercontacName=String.valueOf(list.get(0).usercontacName.charAt(0));
        userContactData.setType(1);
        customList.add(userContactData);
        for (i = 0; i < list.size() - 1; i++) {
            UserContactData userContactData2 = new UserContactData();
            char name1 = list.get(i).usercontacName.toUpperCase().charAt(0);
            char name2 = list.get(i + 1).usercontacName.toUpperCase().charAt(0);
            if (name1 == name2) {
                list.get(i).setType(2);
                customList.add(list.get(i));
            } else {
                list.get(i).setType(2);
                customList.add(list.get(i));
                userContactData2.usercontacName=String.valueOf(name2);
                userContactData2.setType(1);
                customList.add(userContactData2);
            }
        }
        list.get(i).setType(2);
        customList.add(list.get(i));
        return customList;
    }

}
