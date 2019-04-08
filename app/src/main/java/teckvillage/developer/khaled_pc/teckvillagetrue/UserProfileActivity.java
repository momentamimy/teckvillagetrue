package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intrusoft.squint.DiagonalView;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    //CONTACT
    RelativeLayout Phone_Edit_Layout,home_Edit_Layout,Email_Edit_Layout,Websie_Edit_Layout;

    //ABOUT
    RelativeLayout info_Edit_Layout;
    LinearLayout info_Gender_Layout;

    DiagonalView diagonalView;
    CircleImageView userPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        diagonalView=findViewById(R.id.diagonal);
        userPhoto=findViewById(R.id.UserPhoto);

        //CONTACT
        Phone_Edit_Layout=findViewById(R.id.Phone_Edit_Layout);
        home_Edit_Layout=findViewById(R.id.home_Edit_Layout);
        Email_Edit_Layout=findViewById(R.id.Email_Edit_Layout);
        Websie_Edit_Layout=findViewById(R.id.Websie_Edit_Layout);

        //ABOUT
        info_Edit_Layout=findViewById(R.id.info_Edit_Layout);
        info_Gender_Layout=findViewById(R.id.info_Gender_Layout);


        //CONTACT
        Phone_Edit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        home_Edit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomHomeEditDialog();
            }
        });
        Email_Edit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomEmailEditDialog();
            }
        });
        Websie_Edit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomWebsiteEditDialog();
            }
        });


        //ABOUT
        info_Edit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomShortTextEditDialog();
            }
        });
        info_Gender_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomGenderDialog();
            }
        });


            BlurBuilder blurBuilder=new BlurBuilder();
            Bitmap resultBmp = blurBuilder.blur(this, BitmapFactory.decodeResource(getResources(), R.drawable.tamimy));
            diagonalView.setImageBitmap(resultBmp);


    }

    public class BlurBuilder {

        private static final float BITMAP_SCALE = 0.19f;
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
    private Bitmap darkenBitMap(Bitmap bm) {

        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Color.RED);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0x327F7F7F, 0x00333333);    // darken
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);

        return bm;
    }

    Dialog MyDialogHomeEdit=null;
    public void MyCustomHomeEditDialog() {
        MyDialogHomeEdit = new Dialog(this);
        MyDialogHomeEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogHomeEdit.setContentView(R.layout.home_edit_dialog);
        Window window = MyDialogHomeEdit.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogHomeEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText Street,ZibCode,City;
        TextView Cancel,Ok;

        Street=MyDialogHomeEdit.findViewById(R.id.edit_Street);
        ZibCode=MyDialogHomeEdit.findViewById(R.id.edit_ZIB);
        City=MyDialogHomeEdit.findViewById(R.id.edit_City);

        Cancel=MyDialogHomeEdit.findViewById(R.id.btn_close);
        Ok=MyDialogHomeEdit.findViewById(R.id.btn_submit);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogHomeEdit.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogHomeEdit.dismiss();
            }
        });
        MyDialogHomeEdit.show();
    }


    Dialog MyDialogEmailEdit=null;
    public void MyCustomEmailEditDialog() {
        MyDialogEmailEdit = new Dialog(this);
        MyDialogEmailEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogEmailEdit.setContentView(R.layout.email_edit_dialog);
        Window window = MyDialogEmailEdit.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogEmailEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText Email;
        TextView Cancel,Ok;

        Email=MyDialogEmailEdit.findViewById(R.id.edit_Email);

        Cancel=MyDialogEmailEdit.findViewById(R.id.btn_close);
        Ok=MyDialogEmailEdit.findViewById(R.id.btn_submit);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogEmailEdit.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogEmailEdit.dismiss();
            }
        });
        MyDialogEmailEdit.show();
    }


    Dialog MyDialogWebsiteEdit=null;
    public void MyCustomWebsiteEditDialog() {
        MyDialogWebsiteEdit= new Dialog(this);
        MyDialogWebsiteEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogWebsiteEdit.setContentView(R.layout.website_edit_dialog);
        Window window = MyDialogWebsiteEdit.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogWebsiteEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText Website;
        TextView Cancel,Ok;

        Website=MyDialogWebsiteEdit.findViewById(R.id.edit_Websie);

        Cancel=MyDialogWebsiteEdit.findViewById(R.id.btn_close);
        Ok=MyDialogWebsiteEdit.findViewById(R.id.btn_submit);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogWebsiteEdit.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogWebsiteEdit.dismiss();
            }
        });
        MyDialogWebsiteEdit.show();
    }


    Dialog MyDialogShortTextEdit=null;
    public void MyCustomShortTextEditDialog() {
        MyDialogShortTextEdit= new Dialog(this);
        MyDialogShortTextEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogShortTextEdit.setContentView(R.layout.short_text_edit_dialog);
        Window window = MyDialogShortTextEdit.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogShortTextEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText ShortText;
        final TextView NumChar;
        TextView Cancel,Ok;

        ShortText=MyDialogShortTextEdit.findViewById(R.id.edit_short_text);
        NumChar=MyDialogShortTextEdit.findViewById(R.id.Num_Char);

        ShortText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i=160-ShortText.getText().length();
                NumChar.setText(i+" characters remaining");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Cancel=MyDialogShortTextEdit.findViewById(R.id.btn_close);
        Ok=MyDialogShortTextEdit.findViewById(R.id.btn_submit);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogShortTextEdit.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogShortTextEdit.dismiss();
            }
        });
        MyDialogShortTextEdit.show();
    }


    Dialog MyDialogGender=null;
    public void MyCustomGenderDialog() {
        MyDialogGender= new Dialog(this);
        MyDialogGender.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogGender.setContentView(R.layout.gender_dialog);
        Window window = MyDialogGender.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogGender.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView Prefer_not_to_say,Male,Female;

        Prefer_not_to_say=MyDialogGender.findViewById(R.id.Prefer_not_to_say);
        Male=MyDialogGender.findViewById(R.id.Male);
        Female=MyDialogGender.findViewById(R.id.Female);

        Prefer_not_to_say.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogGender.dismiss();
            }
        });
        Male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogGender.dismiss();
            }
        });
        Female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogGender.dismiss();
            }
        });

        MyDialogGender.show();
    }
}
