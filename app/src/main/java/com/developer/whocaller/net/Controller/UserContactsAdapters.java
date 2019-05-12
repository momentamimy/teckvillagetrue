package com.developer.whocaller.net.Controller;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import in.myinnos.alphabetsindexfastscrollrecycler.utilities_fs.StringMatcher;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import com.developer.whocaller.net.SMS_MessagesChat;
import com.developer.whocaller.net.View.User_Contact_Profile;
import com.developer.whocaller.net.Model.UserContactData;


/**
 * Created by khaled-pc on 2/13/2019.
 */

public class UserContactsAdapters extends RecyclerView.Adapter<UserContactsAdapters.ViewHolder> implements SectionIndexer {

    private static final int TYPE_LETTER =1 ;
    private static final int TYPE_USER =2 ;
    Context context;
    private List<UserContactData> mDataArray;
    private ArrayList<Integer> mSectionPositions;
    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";//Character.toString((char)0x2605)+
    Intent intent;

    public UserContactsAdapters(Context context,List<UserContactData> mDataArray) {
        this.mDataArray=mDataArray;
        this.context=context;
    }

    public UserContactsAdapters() {

    }


    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    //Character Array
    @Override
    public Object[] getSections() {

        String[] sections = new String[mSections.length()];

        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;

    }


    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (mDataArray.get(position).getType() == TYPE_LETTER) {
            viewType = TYPE_LETTER;
        } else if (mDataArray.get(position).getType() == TYPE_USER) {
            viewType = TYPE_USER;
        }

        return viewType;
    }


    @Override
    public int getPositionForSection(int sectionIndex) {
        // If there is no item for current section, previous section will be selected
        for (int i = sectionIndex; i >= 0; i--) {
            for (int j = 0; j < getItemCount(); j++) {
                if (i == 0) {
                    // For numeric section
                    for (int k = 0; k <= 9; k++) {
                        if (StringMatcher.match(String.valueOf(mDataArray.get(j).usercontacName.charAt(0)), String.valueOf(k)))
                            return j;
                    }
                } else {
                    if (StringMatcher.match(String.valueOf(mDataArray.get(j).usercontacName.charAt(0)), String.valueOf(mSections.charAt(i))))
                        return j;
                }
            }
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       switch (viewType) {
            case TYPE_LETTER:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.letter_layout, parent, false);
                return new ViewHolder(v);
            case TYPE_USER:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_contacts_row, parent, false);
                return new ViewHolder(v2);
            default:
                View v3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_contacts_row, parent, false);
                return new ViewHolder(v3);
        }
        }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(holder.contactName2!=null){


            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)holder.contactName2.getLayoutParams();
           //Assume contactName2 is null //when get code retrieve country remove from here
           //--------------------------------------------------------------------------------
            holder.country2.setVisibility(View.GONE);
            holder.contactName2.setText(mDataArray.get(position).usercontacName);//Display name
            //set contactName Center in parent if country visiable
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            holder.contactName2.setLayoutParams(layoutParams);
           //---------------------------------------------------------------------------------

            /*
            if(mDataArray.get(position).getCountry()!=null&&mDataArray.get(position).getCountry().length()>0){
                holder.country2.setVisibility(View.VISIBLE);
                holder.country2.setText(mDataArray.get(position).getCountry());//Display phone or country
                holder.contactName2.setText(mDataArray.get(position).usercontacName);//Display name
                // if false remove center:
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
                holder.contactName2.setLayoutParams(layoutParams);

            }else {
                holder.country2.setVisibility(View.GONE);
                holder.contactName2.setText(mDataArray.get(position).usercontacName);//Display name
                //set contactName Center in parent if country visiable
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                holder.contactName2.setLayoutParams(layoutParams);
            }*/



            holder.contactCircleImageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

           holder.container_chaticon.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   intent = new Intent(context,SMS_MessagesChat.class);
                   intent.putExtra("LogSMSName",mDataArray.get(position).usercontacName);
                   Log.w("gda",mDataArray.get(position).usercontacName);
                   ArrayList<String> phones = new ArrayList<String>();
                   phones= getphonenumberfromcontactID(mDataArray.get(position).getId());
                   if(phones.size()==1){
                       if(phones.get(0) != null&& !phones.get(0).isEmpty()){
                           intent.putExtra("LogSMSAddress",phones.get(0));
                           context.startActivity(intent);
                       }else {
                           Toast.makeText(context, "Can't send message to this number", Toast.LENGTH_LONG).show();
                       }
                   }else if(phones.size()>1){

                       DisplayArraylistOfPhonenumberDialog(phones);

                   }
               }
           });

            holder.openProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(context,User_Contact_Profile.class);
                    intent2.putExtra("ContactID",mDataArray.get(position).getId());
                    context.startActivity(intent2);
                }
            });

        }

        if(holder.letter!=null){
            //Log.w("letter",mDataArray.get(position).usercontacName);
            holder.letter.setText(mDataArray.get(position).usercontacName);

        }

    }

    @Override
    public int getItemCount() {
        if (mDataArray == null)
            return 0;
        return mDataArray.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView contactCircleImageView2;
        TextView contactName2;
        TextView country2;
        TextView letter;
        ImageView chaticon;
        RelativeLayout container_chaticon,openProfile;

          ViewHolder(View itemView) {
            super(itemView);
            contactCircleImageView2=itemView.findViewById(R.id.usercontactimg);
            contactName2=itemView.findViewById(R.id.nameofcontacts);
            country2=itemView.findViewById(R.id.country);
            letter=itemView.findViewById(R.id.letter);
            chaticon=itemView.findViewById(R.id.chat_user_contact);
            container_chaticon=itemView.findViewById(R.id.container_lay_chat_user_contact);
              openProfile=itemView.findViewById(R.id.fir_layCon);

        }
    }

    public ArrayList<String> getphonenumberfromcontactID(long id){
        ArrayList<String> phones = new ArrayList<String>();

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {  ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                new String[]{String.valueOf(id)}, null);

        while (cursor.moveToNext())
        {
            phones.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        }

        cursor.close();
        return(phones);
    }


    void DisplayArraylistOfPhonenumberDialog(ArrayList<String> phonenumbers){

        //Create sequence of items
        final CharSequence[] phonenums = phonenumbers.toArray(new String[phonenumbers.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Select Phone Number");
        dialogBuilder.setItems(phonenums, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //String selectedText = phonenums[item].toString();
                //Selected item in listview
                //Open Dialog With Phone Number That is Selected From This Dialog
                intent.putExtra("LogSMSAddress",phonenums[item].toString());
                context.startActivity(intent);

            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();

    }
}