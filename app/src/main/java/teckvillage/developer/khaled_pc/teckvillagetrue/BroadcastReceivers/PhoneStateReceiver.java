package teckvillage.developer.khaled_pc.teckvillagetrue.BroadcastReceivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.MainActivity;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.BodyNumberModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.FetchedUserData;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.GroupBodyModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.GroupChatResultModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;
import teckvillage.developer.khaled_pc.teckvillagetrue.Permission;
import teckvillage.developer.khaled_pc.teckvillagetrue.PopupDialogActivity;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.CheckNetworkConnection;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ConnectionDetector;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.SendToChatActivity;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class PhoneStateReceiver extends BroadcastReceiver {

    CardView CallerCardView ;
    TextView CallerName ;
    TextView CallerCountry ;
    RelativeLayout CallerProfileLayout ;
    ImageView CallerImage ;
    ImageView CloseDialog ;

    TextView CallerNumber ;
    TextView CallerNumberType ;

    ImageView CallerImageProgress;
    ProgressBar CallerProgress;

    boolean isBlock;
    private static final String TAG = "State";
    public static WindowManager wm = null;
    private WindowManager.LayoutParams params1;
    public static View view1;
    static boolean viewIsAdded = false;
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

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            String str = intent.getAction();
            if ("android.intent.action.PHONE_STATE".equals(str))
                inComing(context, intent);

            if ("android.intent.action.NEW_OUTGOING_CALL".equals(str))
                trueCallerOutgoing(context, intent);

        } else {
            //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
            if (intent.getAction().equalsIgnoreCase("android.intent.action.NEW_OUTGOING_CALL")) {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            } else {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                int state = 0;
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    state = TelephonyManager.CALL_STATE_IDLE;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    state = TelephonyManager.CALL_STATE_RINGING;
                }


                onCallStateChanged(context, state, number);
            }
        }


        //********************************************************************************************************************************************
        //*************************************************Old Code***************************************************************************************
      /*  try {


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


            }
        }
        else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)&&swithcincomgoing)
        {
            DisplayDialogOverApps(context,number);
            BlockNumberWhenRing(context,number);
        }
        else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {


        }

        }catch (Exception e){
            e.printStackTrace();
        }
*/
    }


    public void DisplayDialogOverApps(Context context, String Number) {

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

        view1 = LayoutInflater.from(context).inflate(R.layout.offhook_ringing_dialog, null, false);
        CallerCardView = view1.findViewById(R.id.Caller_CardView);
        CallerName = view1.findViewById(R.id.Caller_Name);
        CallerCountry = view1.findViewById(R.id.Caller_Country);
        CallerProfileLayout = view1.findViewById(R.id.Caller_Profile_Layout);
        CallerImage = view1.findViewById(R.id.Caller_Image);
        CloseDialog = view1.findViewById(R.id.Close_Dialog);

        CallerNumber = view1.findViewById(R.id.Caller_Number);
        CallerNumberType = view1.findViewById(R.id.Caller_Number_Type);

        CallerImageProgress=view1.findViewById(R.id.progress_contact_img);
        CallerProgress=view1.findViewById(R.id.progress);


        String contactName= (String) get_calls_log.getContactName(Number);
        if (get_calls_log.contactExists(Number))
        {
            CallerName.setText(contactName);
            CallerNumber.setText(Number);
        }
        else
        {
            CallerName.setText(Number);
            CallerNumber.setText(Number);
        }

        //khaled Block
        boolean isBlock=false;
        //Block List Phone numbers
        BlockNumbers = RetreiveAllNumberInBlockList(context);

        if (BlockNumbers.size() > 0) {
            for (int i = 0; i < BlockNumbers.size(); i++) {
                if ((BlockNumbers.get(i) != null) && BlockNumbers.get(i).equalsIgnoreCase(Number
                )) {
                    isBlock=true;
                }
            }
        }

            if (Number.length()<=4||isBlock)
        {
            CallerProfileLayout.setBackgroundColor(context.getResources().getColor(R.color.redColor));
            CallerImage.setImageResource(R.drawable.ic_nurse_red);
        }

        Bitmap contactPhoto =get_calls_log.retrieveContactPhoto(Number);
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
                viewIsAdded = false;
            }
        });

        String dialogPosition = preferences.getString("DialogPosition", "Center");
        params1.height = dpToPx(150, context);
        params1.width = metrics.widthPixels - 40;
        params1.x = 0;
        params1.format = PixelFormat.TRANSLUCENT;

        if (dialogPosition.equals("Top")) {
            params1.y = -metrics.heightPixels / 2;
        } else if (dialogPosition.equals("Center")) {
            params1.y = 0;
        } else if (dialogPosition.equals("Bottom")) {
            params1.y = metrics.heightPixels / 2;
        }
        wm.addView(view1, params1);
        viewIsAdded = true;

        getUserDataApi(context,Number);
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

    boolean blockList(Context context, String phonenumber) {
        //Block List Phone numbers
        BlockNumbers = RetreiveAllNumberInBlockList(context);

        for (int i = 0; i < BlockNumbers.size(); i++) {
            if ((BlockNumbers.get(i) != null) && BlockNumbers.get(i).equalsIgnoreCase(phonenumber)) {

                return true;
            }
        }
        return false;
    }

    boolean BlockNumberWhenRing(Context context, String phonenumber) {

        //Block List Phone numbers
        BlockNumbers = RetreiveAllNumberInBlockList(context);

        if (BlockNumbers.size() > 0) {

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


                for (int i = 0; i < BlockNumbers.size(); i++) {
                    if ((BlockNumbers.get(i) != null) && BlockNumbers.get(i).equalsIgnoreCase(phonenumber)) {
                        // m2.invoke(telephonyService); //silenceRinger
                        m.invoke(telephonyService); // invoke endCall()
                        m.setAccessible(true); // Make it accessible

                        BloackNotification(BlockNumbers.get(i), context);
                        //addFromCallLog(context,phonenumber);//Add to block list history
                        ADDphoneNumberBlockListHistory(context, addFromCallLog(context, phonenumber), BlockNumbers.get(i));
                        return true;
                    }
                }


            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                return false;

            }
            //Turn OFF the mute
            if (audioManager != null) {
                audioManager.setStreamMute(AudioManager.STREAM_RING, false);
            }

        }


        return false;
    }


    public List<String> RetreiveAllNumberInBlockList(Context context) {

        List<String> BlockNumbersH;
        db = new Database_Helper(context);
        BlockNumbersH = db.getAllBlocklistNumbers();

        return BlockNumbersH;

    }

    public String ADDphoneNumberBlockListHistory(Context context, long ID, String num) {


        db = new Database_Helper(context);
        long id = db.insertBlockListHistory(ID, num);

        if (id != -1) {
            return "Success";
        } else {
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
                        Log.w("Loglastcalldetails", "ID= " + String.valueOf(id) + "  $$  " + "Number=  " + _number);
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
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        if (!BlockNumberWhenRing(ctx, number)) {
            preferences = ctx.getSharedPreferences("PopUp_Dialog", MODE_PRIVATE);
            boolean swithcOutgoing = preferences.getBoolean("SwitchOutgoing", true);
            boolean swithcincomgoing = preferences.getBoolean("switchIncoming", true);
            get_calls_log = new Get_Calls_Log(ctx);
            if (!get_calls_log.contactExists(number) && swithcincomgoing) {
                DisplayDialogOverApps(ctx, number);
            }
        }
        Log.w("NewState", "onIncomingCallStarted" + " %%PhoneNumber= " + number);
    }

    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        preferences = ctx.getSharedPreferences("PopUp_Dialog", MODE_PRIVATE);
        boolean swithcOutgoing = preferences.getBoolean("SwitchOutgoing", true);
        boolean swithcincomgoing = preferences.getBoolean("switchIncoming", true);
        get_calls_log = new Get_Calls_Log(ctx);
        if (!get_calls_log.contactExists(number) && swithcOutgoing) {
            DisplayDialogOverApps(ctx, number);
        }
        Log.w("NewState", "onOutgoingCallStarted" + " %%PhoneNumber= " + number);
    }

    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        preferences = ctx.getSharedPreferences("PopUp_Dialog", MODE_PRIVATE);
        boolean swithcOutgoing = preferences.getBoolean("SwitchOutgoing", true);
        boolean swithcincomgoing = preferences.getBoolean("switchIncoming", true);
        get_calls_log = new Get_Calls_Log(ctx);
        if (!get_calls_log.contactExists(number) && swithcincomgoing) {
            try {


                    wm.removeViewImmediate(view1);//error
                    viewIsAdded = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent1 = new Intent(ctx, PopupDialogActivity.class);
            intent1.putExtra("Number", number);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent1);

        }
        Log.w("NewState", "onIncomingCallEnded" + " %%PhoneNumber= " + number);
    }//When user Answer income call and close call

    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        preferences = ctx.getSharedPreferences("PopUp_Dialog", MODE_PRIVATE);
        boolean swithcOutgoing = preferences.getBoolean("SwitchOutgoing", true);
        boolean swithcincomgoing = preferences.getBoolean("switchIncoming", true);
        get_calls_log = new Get_Calls_Log(ctx);
        if (!get_calls_log.contactExists(number) && swithcOutgoing) {
            try {
                wm.removeViewImmediate(view1);//error
                    viewIsAdded = false;

            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent1 = new Intent(ctx, PopupDialogActivity.class);
            intent1.putExtra("Number", number);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent1);

        }
        Log.w("NewState", "onOutgoingCallEnded" + " %%PhoneNumber= " + number);
    }

    protected void onMissedCall(Context ctx, String number, Date start) {
        if (!blockList(ctx, number)) {
            try {
                wm.removeViewImmediate(view1);//error
                viewIsAdded = false;

            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent1 = new Intent(ctx, PopupDialogActivity.class);
            intent1.putExtra("Number", number);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent1);
        }
        Log.w("NewState", "onMissedCall" + " %%PhoneNumber= " + number);
    }//Occur when Income call -> Ring until End (No answer) Or User Cancel(phone number Blocked)


    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
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
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime);
                } else if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                } else {
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                }
                break;
        }
        lastState = state;
    }


    private NotificationManager notifManager;
    private NotificationChannel mChannel;

    public void BloackNotification(String number, Context context) {
        String message = "";
        String ContactName = get_calls_log.getContactName(number);
        if (TextUtils.isEmpty(ContactName)) {
            message = "(" + number + ")";
        } else {
            message = "(" + number + ")" + ContactName;
        }

        Intent intent;
        PendingIntent pendingIntent = null;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }

        intent = new Intent(context, MainActivity.class);
        intent.putExtra("NOTIFICATION", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                mChannel = new NotificationChannel
                        ("0", number, importance);
                mChannel.setDescription(message);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]
                        {100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, "0");

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle("Call Blocked by Whocaller")  // flare_icon_30
                    .setSmallIcon(R.drawable.logo_notification) // required
                    .setContentText(message)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setGroup("Blocked_Numbers")
                    .setAutoCancel(true)
                    .setColor((context.getResources().getColor(R.color.colorPrimary)))
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{100, 200, 300, 400,
                            500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(context);


            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSmallIcon(R.drawable.logo);
            builder.setContentTitle("Call Blocked by Whocaller");
            builder.setContentText(message);
            builder.setGroup("Blocked_Numbers");
            builder.setColor((context.getResources().getColor(R.color.colorPrimary)));
            builder.setSound(sound);
            builder.setVibrate(new long[]{100, 200, 300, 400,
                    500, 400, 300, 200, 400});

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                Intent resultIntent = new Intent(context, MainActivity.class);
                resultIntent.putExtra("NOTIFICATION", true);
                TaskStackBuilder stackBuilder = null;
                stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = null;
                resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);
            }

        }
        NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(context, "0");
        summaryBuilder.setSmallIcon(R.drawable.logo_notification)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setColor((context.getResources().getColor(R.color.colorPrimary)))
                .setGroup("Blocked_Numbers")
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .build();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.addLine(number + " " + message);
            inboxStyle.setSummaryText("Blocked Numbers");
            summaryBuilder.setStyle(inboxStyle);
        }
        Notification summaryNotification = summaryBuilder.build();


        Notification notification = builder.build();
        int id = (int) System.currentTimeMillis();
        notifManager.notify(id, notification);
        notifManager.notify(666, summaryNotification);
    }

    private void inComing(Context context, Intent intent) {
        String callState = intent.getStringExtra("state");
        if ("RINGING".equals(callState)) {
            Log.i(TAG, "RINGING SENDS BUSY");
        } else if ("OFFHOOK".equals(callState)) {
            Log.i(TAG, "OFFHOOK SENDS BUSY");
        } else if ("IDLE".equals(callState)) {
            Log.i(TAG, "IDLE SENDS AVAILABLE");
        }
    }

    private void trueCallerOutgoing(Context context, Intent intent) {
        String callState = intent.getStringExtra("state");
        if ("RINGING".equals(callState)) {
            Log.i(TAG, "RINGING SENDS BUSY");
        } else if ("OFFHOOK".equals(callState)) {
            Log.i(TAG, "OFFHOOK SENDS BUSY");
        } else if ("IDLE".equals(callState)) {
            Log.i(TAG, "IDLE SENDS AVAILABLE");
        }
    }

    public void getUserDataApi(final Context context, String number) {

        if (CheckNetworkConnection.hasInternetConnection(context)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(context)) {

                if(!ApiAccessToken.getAPIaccessToken(context).equals("NoAPIACCESS")){

                    BodyNumberModel bodyNumberModel = new BodyNumberModel(number);
                    Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                    WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                    Call<FetchedUserData> userDataCall = whoCallerApi.fetchUserData(ApiAccessToken.getAPIaccessToken(context), bodyNumberModel);

                    CallerImageProgress.setVisibility(View.VISIBLE);
                    CallerProgress.setVisibility(View.VISIBLE);

                    userDataCall.enqueue(new Callback<FetchedUserData>() {
                        @Override
                        public void onResponse(Call<FetchedUserData> call, Response<FetchedUserData> response) {
                            if (response.isSuccessful())
                            {
                                //Log.d("userNamePaleeez", response.body().getName());
                                FetchedUserData resp =response.body();
                                updateCard(resp);

                            }
                            else
                            {
                                Log.d("onFailure", "other error");
                                CallerImageProgress.setVisibility(View.GONE);
                                CallerProgress.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<FetchedUserData> call, Throwable t) {
                            Log.d("onFailure", t.getMessage());
                            CallerImageProgress.setVisibility(View.GONE);
                            CallerProgress.setVisibility(View.GONE);
                        }
                    });

                }

            }
        }

    }

    public void updateCard(final FetchedUserData userData)
    {
        if (userData.isIs_spam())
        {
            CallerProfileLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.redColor));
        }

        if(userData.getName()!=null){
          CallerName.setText(userData.getName());
        }

        if(userData.getFull_phone()!=null){
            CallerNumber.setText(userData.getFull_phone());
        }

        if (!TextUtils.isEmpty(userData.getCountry()))
        {
            CallerCountry.setText(userData.getCountry());
            CallerCountry.setVisibility(View.VISIBLE);
        }
        else
        {
            CallerCountry.setVisibility(View.GONE);
        }
        Picasso.with(getApplicationContext()).load("http://whocaller.net/whocallerAdmin/uploads/"+userData.getUser_img()).fit().centerInside()
                .into(CallerImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        CallerImageProgress.setVisibility(View.GONE);
                        CallerProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        if (userData.isIs_spam())
                        {
                            CallerImage.setImageResource(R.drawable.ic_nurse_red);
                        }
                        else
                        {
                            CallerImage.setImageResource(R.drawable.ic_nurse);
                        }
                        CallerImageProgress.setVisibility(View.GONE);
                        CallerProgress.setVisibility(View.GONE);
                    }
                });
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // Bitmap is loaded, use image here
                PopupDialogActivity.BlurBuilder blurBuilder=new PopupDialogActivity.BlurBuilder();
                Bitmap resultBmp = blurBuilder.blur(getApplicationContext(), bitmap);
                CallerProfileLayout.setBackgroundDrawable(new BitmapDrawable(resultBmp));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(getApplicationContext()).load("http://whocaller.net/whocallerAdmin/uploads/"+userData.getUser_img()).into(target);
    }
}