package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ContactHolder;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.ContactInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {

    private List<ContactInfo> itemList;
    Context context;

    public ContactAdapter(Context context, List<ContactInfo> itemList)
    {
        this.context=context;
        this.itemList=itemList;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_of_most_important_contact_recycleview, null);
        ContactHolder rbv = new ContactHolder(layoutView);
        return rbv;
    }

    @Override
    public void onBindViewHolder(final ContactHolder holder, int position) {
        ContactInfo contactInfo=itemList.get(position);
        holder.contactName.setText(contactInfo.contacName);
        holder.numberType.setText(contactInfo.numberType);
        if (contactInfo.imageUrl!=null)
        {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.progressCircleImageView.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(contactInfo.imageUrl)
                    .into(holder.contactCircleImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.progressCircleImageView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.contactCircleImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nurse));
                            holder.progressBar.setVisibility(View.GONE);
                            holder.progressCircleImageView.setVisibility(View.GONE);
                        }
                    });
        }
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

}
