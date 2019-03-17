package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.myinnos.alphabetsindexfastscrollrecycler.utilities_fs.StringMatcher;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_User_Contacts;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.UserContactData;


/**
 * Created by khaled-pc on 2/13/2019.
 */

public class SendToContactsAdapters extends RecyclerView.Adapter<SendToContactsAdapters.ViewHolder> implements SectionIndexer {

    private static final int TYPE_LETTER =1 ;
    private static final int TYPE_USER =2 ;
    Context context;
    private List<UserContactData> mDataArray;
    private ArrayList<Integer> mSectionPositions;
    private String mSections =Character.toString((char)0x2605)+ "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Get_User_Contacts get_user_contacts;
    public SendToContactsAdapters(Context context, List<UserContactData> mDataArray) {
        this.mDataArray=mDataArray;
        this.context=context;
        get_user_contacts=new Get_User_Contacts(context);
    }

    public SendToContactsAdapters() {

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
            final String name=mDataArray.get(position).usercontacName;
            final String num=get_user_contacts.getPhoneNumber(mDataArray.get(position).usercontacName,context).replace(" ","");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, SMS_MessagesChat.class);
                    intent.putExtra("LogSMSName",name);
                    intent.putExtra("LogSMSAddress",num);
                    context.startActivity(intent);
                }
            });
           holder.contactName2.setText(mDataArray.get(position).usercontacName);
           holder.sms.setVisibility(View.INVISIBLE);
           holder.contactCircleImageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    mDataArray.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                    */
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
        ImageView sms;
          ViewHolder(View itemView) {
            super(itemView);
            contactCircleImageView2=itemView.findViewById(R.id.usercontactimg);
            contactName2=itemView.findViewById(R.id.nameofcontacts);
            country2=itemView.findViewById(R.id.country);
            letter=itemView.findViewById(R.id.letter);
            sms=itemView.findViewById(R.id.chat_user_contact);
        }
    }
}