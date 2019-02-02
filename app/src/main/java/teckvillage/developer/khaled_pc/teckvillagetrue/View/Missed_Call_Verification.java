package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;

public class Missed_Call_Verification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed__call__verification);

        ImageView dd=findViewById(R.id.centerparent);
        dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
