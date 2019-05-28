package com.developer.whocaller.net.View;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.developer.whocaller.net.Controller.Block_Adapter;
import com.developer.whocaller.net.Controller.LocaleHelper;
import com.developer.whocaller.net.Model.database.Database_Helper;
import com.developer.whocaller.net.Model.database.tables.block;
import com.developer.whocaller.net.R;

import java.util.ArrayList;
import java.util.List;



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

        getSupportActionBar().setTitle(getResources().getString(R.string.block_list));
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
