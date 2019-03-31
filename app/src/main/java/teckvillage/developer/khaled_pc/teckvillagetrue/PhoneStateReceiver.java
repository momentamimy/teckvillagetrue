package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_Calls_Log;

import static android.content.Context.MODE_PRIVATE;

public class PhoneStateReceiver extends BroadcastReceiver {

    public static WindowManager wm=null;
    private WindowManager.LayoutParams params1;
    public static View view1;
    Get_Calls_Log get_calls_log;

    SharedPreferences preferences;
    @Override
    public void onReceive(Context context, Intent intent) {

        String state= intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        preferences=context.getSharedPreferences("PopUp_Dialog",MODE_PRIVATE);
        boolean swithcOutgoing=preferences.getBoolean("SwitchOutgoing",true);
        boolean swithcincomgoing=preferences.getBoolean("switchIncoming",true);
        String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        // Adds a view on top of the dialer app when it launches.
        //Toast.makeText(context,"Num : "+number+"State : "+state,Toast.LENGTH_LONG).show();
        Toast.makeText(context,"Num : "+number+"State : ",Toast.LENGTH_LONG).show();
        Log.d("hlaclaclclclclclclc","Num : "+number+"State : "+state);

        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)|| state.equals(TelephonyManager.EXTRA_STATE_RINGING))
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)&&swithcOutgoing)
        {
            DisplayDialogOverApps(context,number);
        }
        else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)&&swithcincomgoing)
        {
            DisplayDialogOverApps(context,number);
        }

        else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            if (wm!=null)
            {
                wm.removeViewImmediate(view1);
            }

            Intent intent1=new Intent(context,PopupDialogActivity.class);
            intent1.putExtra("Number",number);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }

    public void DisplayDialogOverApps(Context context,String Number) {

        get_calls_log=new Get_Calls_Log(context);
        String contactName= (String) get_calls_log.getContactName(Number);
        Bitmap contactPhoto=  get_calls_log.retrieveContactPhoto(Number);
        if (TextUtils.isEmpty(contactName))
        {
            contactName=Number;
        }
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params1 = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        view1 = LayoutInflater.from(context).inflate(R.layout.offhook_ringing_dialog,null, false);
        CardView CallerCardView=view1.findViewById(R.id.Caller_CardView);
        TextView CallerName=view1.findViewById(R.id.Caller_Name);
        TextView CallerCountry=view1.findViewById(R.id.Caller_Country);
        RelativeLayout CallerProfileLayout=view1.findViewById(R.id.Caller_Profile_Layout);
        ImageView CallerImage=view1.findViewById(R.id.Caller_Image);
        ImageView CloseDialog=view1.findViewById(R.id.Close_Dialog);

        TextView CallerNumber=view1.findViewById(R.id.Caller_Number);
        TextView CallerNumberType=view1.findViewById(R.id.Caller_Number_Type);

        CallerName.setText(contactName);
        CallerNumber.setText(Number);
        if (contactPhoto!=null)
        {
            BlurBuilder blurBuilder=new BlurBuilder();
            CallerImage.setImageBitmap(contactPhoto);
            Bitmap resultBmp = blurBuilder.blur(context, contactPhoto);
            CallerProfileLayout.setBackgroundDrawable(new BitmapDrawable(resultBmp));

        }

        CloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeViewImmediate(view1);
            }
        });

        String dialogPosition=preferences.getString("DialogPosition","Center");
        params1.height = dpToPx(150,context);
        params1.width = metrics.widthPixels-40;
        params1.x = 0;
        params1.format = PixelFormat.TRANSLUCENT;

        if (dialogPosition.equals("Top"))
        {
            params1.y = -metrics.heightPixels/2;
        }
        else if (dialogPosition.equals("Center"))
        {
            params1.y = 0;
        }
        else if (dialogPosition.equals("Bottom"))
        {
            params1.y = metrics.heightPixels/2;
        }
        wm.addView(view1, params1);
    }

    public int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    public class BlurBuilder {

        private static final float BITMAP_SCALE = 0.1f;
        private static final float BLUR_RADIUS = 15f;


        public Bitmap blur(Context context, Bitmap image) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                int width = Math.round(image.getWidth() * BITMAP_SCALE);
                int height = Math.round(image.getHeight() * BITMAP_SCALE);

                Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
                Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

                RenderScript rs = RenderScript.create(context);

                ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
                Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

                intrinsicBlur.setRadius(BLUR_RADIUS);
                intrinsicBlur.setInput(tmpIn);
                intrinsicBlur.forEach(tmpOut);
                tmpOut.copyTo(outputBitmap);

                return outputBitmap;
            }
            return null;
        }

    }
}
