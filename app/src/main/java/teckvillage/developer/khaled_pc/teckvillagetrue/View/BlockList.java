package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.Block_Adapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.LogAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.DataItems_Block;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.database.tables.block;

public class BlockList extends AppCompatActivity {

    RecyclerView Blocklist;
    LinearLayoutManager lLayout1;
    Block_Adapter adapter;
    TextView emptyrecyc;
    List<block> BlockInfo;
    Database_Helper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_list);

        getSupportActionBar().setTitle("My block list");
        getSupportActionBar().setDisplayShowHomeEnabled(true);   //back button on App Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //back button on App Bar


        Blocklist=findViewById(R.id.blocklistRecycle);
        emptyrecyc=findViewById(R.id.emtyrecycleblocklist);

        BlockInfo=new ArrayList<block>();
        db=new Database_Helper(this);
        BlockInfo=db.getAllBlocklist();




        lLayout1 = new LinearLayoutManager(BlockList.this);
        Blocklist.setLayoutManager(lLayout1);
        Blocklist.setItemAnimator(new DefaultItemAnimator());
        adapter = new Block_Adapter(BlockList.this,BlockInfo);
        Blocklist.setAdapter(adapter);
        toggleEmptyCases(BlockInfo.size());
    }


    public void toggleEmptyCases(int size) {

        if (size > 0) {

            emptyrecyc.setVisibility(View.GONE);

        } else {
            emptyrecyc.setVisibility(View.VISIBLE);

        }
    }
}
