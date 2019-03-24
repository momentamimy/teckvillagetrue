package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.intrusoft.squint.DiagonalView;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {


    DiagonalView diagonalView;
    CircleImageView userPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        diagonalView=findViewById(R.id.diagonal);
        userPhoto=findViewById(R.id.UserPhoto);

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
}
