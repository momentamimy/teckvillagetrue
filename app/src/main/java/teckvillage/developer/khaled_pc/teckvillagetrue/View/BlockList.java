package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.Block_Adapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.tables.block;

public class BlockList extends AppCompatActivity {

    RecyclerView Blocklist;
    LinearLayoutManager lLayout1;
    Block_Adapter adapter;
    TextView emptyrecyc;
    List<block> BlockInfo=new ArrayList<block>();
    Database_Helper db;
    boolean shouldExecuteOnResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_list);

        getSupportActionBar().setTitle("My block list");
        getSupportActionBar().setDisplayShowHomeEnabled(true);   //back button on App Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //back button on App Bar

        shouldExecuteOnResume = false;

        Blocklist=findViewById(R.id.blocklistRecycle);
        emptyrecyc=findViewById(R.id.emtyrecycleblocklist);

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

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (shouldExecuteOnResume) {

            BlockInfo.clear();
            BlockInfo.addAll(db.getAllBlocklist());
            adapter.notifyDataSetChanged();
            toggleEmptyCases(BlockInfo.size());
        }else{
            shouldExecuteOnResume = true;
        }

    }
}
