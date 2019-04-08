package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telecom.TelecomManager;
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



import java.lang.reflect.Method;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.database.tables.block;

import static android.content.Context.MODE_PRIVATE;

public class PhoneStateReceiver extends BroadcastReceiver {

    public static WindowManager wm=null;
    private WindowManager.LayoutParams params1;
    public static View view1;
    static boolean viewIsAdded=false;
    Get_Calls_Log get_calls_log;
    Database_Helper db;
    List<String> BlockNumbers;
    private static final Uri URI_CONTENT_CALLS = Uri.parse("content://call_log/calls");
    // For the sake of performance we don't use comprehensive phone number pattern.
    // We just want to detect whether a phone number is digital but not symbolic.
    private static final Pattern digitalPhoneNumberPattern = Pattern.compile("[+]?[0-9-() ]+");
    // Is used for normalizing a phone number, removing from it brackets, dashes and spaces.
    private static final Pattern normalizePhoneNumberPattern = Pattern.compile("[-() ]");

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;  //because the passed incoming is only valid in ringing


    SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
        }
        else{
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                state = TelephonyManager.CALL_STATE_IDLE;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                state = TelephonyManager.CALL_STATE_RINGING;
            }


            onCallStateChanged(context, state, number);
        }

     //********************************************************************************************************************************************
     //*************************************************Old Code***************************************************************************************


        String state= intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        preferences=context.getSharedPreferences("PopUp_Dialog",MODE_PRIVATE);
        boolean swithcOutgoing=preferences.getBoolean("SwitchOutgoing",true);
        boolean swithcincomgoing=preferences.getBoolean("switchIncoming",true);
        String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        // Adds a view on top of the dialer app when it launches.
        //Toast.makeText(context,"Num : "+number+"State : "+state,Toast.LENGTH_LONG).show();
        Toast.makeText(context,"Num : "+number+"State : ",Toast.LENGTH_LONG).show();
        Log.d("hlaclaclclclclclclc","Num : "+number+"State : "+state);

        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)&&swithcOutgoing)
        {
            if (!viewIsAdded)
            {
                DisplayDialogOverApps(context,number);
            }
        }
        else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)&&swithcincomgoing)
        {
            DisplayDialogOverApps(context,number);
            BlockNumberWhenRing(context,number);
        }
        else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {


            if (wm!=null&&viewIsAdded==true)
            {
                wm.removeViewImmediate(view1);//error
            try {
                if (wm!=null&&viewIsAdded==true)
                {
                    wm.removeView(view1);
                    viewIsAdded=false;
                }
            }catch (Exception e){

            }


                Intent intent1=new Intent(context,PopupDialogActivity.class);
                intent1.putExtra("Number",number);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);



            }
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
                wm.removeView(view1);
                viewIsAdded=false;
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
        viewIsAdded=true;
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


    void BlockNumberWhenRing(Context context,String phonenumber){

        //Block List Phone numbers
        BlockNumbers=RetreiveAllNumberInBlockList(context);

        if(BlockNumbers.size()>0){

            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            //Turn ON the mute
            if (audioManager != null) {
                audioManager.setStreamMute(AudioManager.STREAM_RING, true);
            }

            try {


                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                Class c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                Object telephonyService = m.invoke(tm); // Get the internal ITelephony object
                c = Class.forName(telephonyService.getClass().getName()); // Get its class
                //Method m2 = c.getDeclaredMethod("silenceRinger");
                m = c.getDeclaredMethod("endCall"); // Get the "endCall()" method
                m.setAccessible(true); // Make it accessible


                for(int i=0;i<BlockNumbers.size();i++){
                    if ((BlockNumbers.get(i) != null) &&BlockNumbers.get(i).equalsIgnoreCase(phonenumber)) {
                       // m2.invoke(telephonyService); //silenceRinger
                        m.invoke(telephonyService); // invoke endCall()
                        m.setAccessible(true); // Make it accessible

                        //addFromCallLog(context,phonenumber);//Add to block list history
                        ADDphoneNumberBlockListHistory(context,addFromCallLog(context,phonenumber),BlockNumbers.get(i));
                    }
                }


            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();

            }
            //Turn OFF the mute
            if (audioManager != null) {
                audioManager.setStreamMute(AudioManager.STREAM_RING, false);
            }

        }



    }


   public List<String> RetreiveAllNumberInBlockList(Context context){

       List<String> BlockNumbersH;
       db=new Database_Helper(context);
       BlockNumbersH=db.getAllBlocklistNumbers();

       return BlockNumbersH;

   }

    public String ADDphoneNumberBlockListHistory(Context context,long ID,String num){


        db=new Database_Helper(context);
        long id=db.insertBlockListHistory(ID,num);

        if(id!=-1){
            return "Success";
        }else {
            return "Failed";

        }

    }

    // ADD passed number from the Call log to Blocklist History
    private long addFromCallLog(Context context, String number) {
        // wait for the call be written to the Call log
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        // and then ADD it
       return getLastRecordIdFromCallLog(context, number, 10000);
    }

    // Returns last call record from the Call log that was written since "duration" time
    private long getLastRecordIdFromCallLog(Context context, String number, long duration) {
        if (!Permission.isGranted(context, Permission.READ_CALL_LOG)) {
            return -1;
        }

        // We should not search call just by number because it can be not normalized.
        // Therefore select all records have been written since passed time duration,
        // normalize every number and then compare.
        long time = System.currentTimeMillis() - duration;

        Cursor cursor = context.getContentResolver().query(
                URI_CONTENT_CALLS,
                new String[]{CallLog.Calls._ID, CallLog.Calls.NUMBER},
                CallLog.Calls.DATE + " > ? ",
                new String[]{String.valueOf(time)},
                CallLog.Calls.DATE + " DESC");

        long id = -1;
        if (validate(cursor)) {
            cursor.moveToFirst();
            final int ID = cursor.getColumnIndex(CallLog.Calls._ID);
            final int NUMBER = cursor.getColumnIndex(CallLog.Calls.NUMBER);

            // get the first equal
            do {
                String _number = cursor.getString(NUMBER);

                // searching for normal number
                if (_number != null) {
                        _number = normalizePhoneNumber(_number);
                        if (_number.equals(number)) {
                            id = cursor.getLong(ID);
                            Log.w("Loglastcalldetails","ID= " +String.valueOf(id)+"  $$  " +"Number=  "+_number);
                            break;
                        }
                    }

            } while (cursor.moveToNext());
            cursor.close();
        }

        return id;
    }


    private boolean validate(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) return false;
        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        }
        return true;
    }

    /**
     * If passed phone number is digital and not symbolic then normalizes
     * it, removing brackets, dashes and spaces.
     */
    public static String normalizePhoneNumber(@NonNull String number) {
        number = number.trim();
        if (digitalPhoneNumberPattern.matcher(number).matches()) {
            number = normalizePhoneNumberPattern.matcher(number).replaceAll("");
        }
        return number;
    }

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context ctx, String number, Date start){Log.w("NewState","onIncomingCallStarted"+" %%PhoneNumber= "+number);}
    protected void onOutgoingCallStarted(Context ctx, String number, Date start){Log.w("NewState","onOutgoingCallStarted"+" %%PhoneNumber= "+number);}
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end){Log.w("NewState","onIncomingCallEnded"+" %%PhoneNumber= "+number);}//When user Answer income call and close call
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end){Log.w("NewState","onOutgoingCallEnded"+" %%PhoneNumber= "+number);}
    protected void onMissedCall(Context ctx, String number, Date start){Log.w("NewState","onMissedCall"+" %%PhoneNumber= "+number);}//Occur when Income call -> Ring until End (No answer) Or User Cancel(phone number Blocked)



    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if(lastState == state){
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, number, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime);
                }
                else if(isIncoming){
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                }
                else{
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                }
                break;
        }
        lastState = state;
    }

}
