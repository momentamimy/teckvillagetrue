package teckvillage.developer.khaled_pc.teckvillagetrue.View.Fragments.Search_API_Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;
import com.sdsmdg.tastytoast.TastyToast;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.RecycleviewResult_SearchAPI;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.CheckNetworkConnection;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ConnectionDetector;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearchByNumber extends Fragment {

    ImageView flagIcon;
    CountryPicker countryPicker;
    Country myCountry=null;
    EditText text;
    Button search_btn;
    String searchvalue;
    String countrycode;

    public FragmentSearchByNumber() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_search_by_number_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flagIcon=view.findViewById(R.id.flag_icon);
        flagIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getActivity().getSupportFragmentManager());
            }
        });
        countryPicker = new CountryPicker.Builder().with(getActivity())
                .listener(new OnCountryPickerListener() {
                    @Override
                    public void onSelectCountry(Country country) {
                        myCountry=country;
                        flagIcon.setImageResource(country.getFlag());
                    }
                }).build();


        text=view.findViewById(R.id.editsearch);
        search_btn=view.findViewById(R.id.btn_searchfor);

        text.setHint("Enter Phone Number");
        text.setInputType(InputType.TYPE_CLASS_NUMBER ); //for decimal numbers

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchvalue= text.getText().toString().trim();
                if(searchvalue.isEmpty() || searchvalue == null ){

                    //TastyToast.makeText(getActivity(), "Please Enter Phone Number", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    Toast.makeText(getActivity(),"Please Enter Phone Number",Toast.LENGTH_LONG).show();

                }else if(searchvalue.length()<4){

                    //TastyToast.makeText(getActivity(), "Phone Number should be more than three numbers", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    Toast.makeText(getActivity(),"Phone Number should be more than three numbers",Toast.LENGTH_LONG).show();
                }else  {

                    //Check that text is numbers
                    if(searchvalue.matches("[0-9]+") ){

                        //For Country code
                        if(myCountry==null){
                            countrycode="";
                            Log.w("myCountry",countrycode);
                        }else {
                            countrycode=myCountry.getDialCode();
                            Log.w("myCountry",countrycode);
                        }

                        //searchvalue
                        if (CheckNetworkConnection.hasInternetConnection(getActivity())) {

                            //Check internet Access
                            if (ConnectionDetector.hasInternetConnection(getActivity())) {

                                Intent intent=new Intent(getActivity(),RecycleviewResult_SearchAPI.class);
                                intent.putExtra("countrycodesearch",countrycode);
                                intent.putExtra("searchvalue",searchvalue);
                                intent.putExtra("SearchMethod","searchMethodPhoneNumber");
                                startActivity(intent);


                            }else {
                                TastyToast.makeText(getActivity(), "Internet not access Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }

                        }else {
                            TastyToast.makeText(getActivity(), "You're offline. Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }


                    }else {
                        TastyToast.makeText(getActivity(), "Please Enter Phone Number Not Text", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }

                }

            }
        });


    }

}
