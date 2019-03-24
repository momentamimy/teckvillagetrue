package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_Calls_Log;

public class PopupDialogActivity extends Activity {



    TextView CallerName;
    TextView CallerCountry;
    RelativeLayout ProfileBlurLayOut;
    ImageView CallerImage;
    ImageView CloseDialog;

    TextView CallerNumber;
    TextView CallerNumberType;

    Get_Calls_Log get_calls_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_dialog);
        String number=getIntent().getStringExtra("Number");
        MyCustomAssignNumDialog(number);
        WhoCallerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

    }

    Dialog WhoCallerDialog;
    public void MyCustomAssignNumDialog(final String number) {
        WhoCallerDialog= new Dialog(this, R.style.DialogTheme);
        WhoCallerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WhoCallerDialog.setContentView(R.layout.who_caller_dialog);
        WhoCallerDialog.show();
        Window window = WhoCallerDialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        WhoCallerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CallerName=WhoCallerDialog.findViewById(R.id.Caller_Name);
        CallerCountry=WhoCallerDialog.findViewById(R.id.Caller_Country);
        ProfileBlurLayOut=WhoCallerDialog.findViewById(R.id.ProfileBlurLayOut);
        CallerImage=WhoCallerDialog.findViewById(R.id.Caller_Image);
        CloseDialog=WhoCallerDialog.findViewById(R.id.Close_Dialog);

        CallerNumber=WhoCallerDialog.findViewById(R.id.Caller_Number);
        CallerNumberType=WhoCallerDialog.findViewById(R.id.Caller_Number_Type);

        get_calls_log=new Get_Calls_Log(this);
        String contactName= (String) get_calls_log.getContactName(number);
        Bitmap contactPhoto=  get_calls_log.retrieveContactPhoto(number);
        if (TextUtils.isEmpty(contactName))
        {
            contactName=number;
        }

        CallerName.setText(contactName);
        CallerNumber.setText(number);
        if (contactPhoto!=null)
        {
            BlurBuilder blurBuilder=new BlurBuilder();
            CallerImage.setImageBitmap(contactPhoto);
            Bitmap resultBmp = blurBuilder.blur(this, contactPhoto);
            ProfileBlurLayOut.setBackgroundDrawable(new BitmapDrawable(resultBmp));

        }

        CloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhoCallerDialog.dismiss();
            }
        });


    }


    public int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    public class BlurBuilder {

        private static final float BITMAP_SCALE = 0.15f;
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
