package com.zhaw.catiejo.whatsforlunch.MenuDisplay;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.content.Intent;
import com.zhaw.catiejo.whatsforlunch.MensaContainer;
import com.zhaw.catiejo.whatsforlunch.MensaPicker.MensaPickerActivity;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch.WhatsForLunchApplication;
import com.zhaw.catiejo.whatsforlunch._campusinfo.ICateringController;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MenuDisplayActivity extends AppCompatActivity implements View.OnClickListener {
    @Inject
    ICateringController cateringController;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mFab;
    private MensaContainer mMensa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Boilerplate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((WhatsForLunchApplication) getApplication()).inject(this);

        mMensa = (MensaContainer) getIntent().getSerializableExtra(Constants.MENU_SELECTOR);


        // In activity_main.xml, there's a Recycler View with the id recyclerView.
        // This gets a reference to that.
        mRecyclerView = (RecyclerView) findViewById(R.id.menuRecycler);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        // Horizontally scrolling layout manager
        // Influenced from https://goo.gl/B9CsiA and https://goo.gl/fQbj5E
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set up the adapter...this connects the views (layouts?) to the data (my custom class, MenuCard)
        mAdapter = new MenuCardAdapter(getMenuCardList(), null);
        mRecyclerView.setAdapter(mAdapter);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

    }
    private List<MenuCard> getMenuCardList() {
        List<MenuCard> cards = new ArrayList<>();
        cards.add(new MenuCard(R.string.foodCounter_1, R.string.menuItem_1, R.string.filler, R.string.studentPrice, R.string.employeePrice, R.string.externalPrice));
        cards.add(new MenuCard(R.string.foodCounter_2, R.string.menuItem_2, R.string.filler, R.string.studentPrice, R.string.employeePrice, R.string.externalPrice));
        cards.add(new MenuCard(R.string.foodCounter_3, R.string.menuItem_3, R.string.filler, R.string.studentPrice, R.string.employeePrice, R.string.externalPrice));
        cards.add(new MenuCard(R.string.foodCounter_4, R.string.menuItem_4, R.string.filler, R.string.studentPrice, R.string.employeePrice, R.string.externalPrice));
        cards.add(new MenuCard(R.string.foodCounter_5, R.string.menuItem_5, R.string.filler, R.string.studentPrice, R.string.employeePrice, R.string.externalPrice));
        return cards;
    }

    // implements View.OnClickListener so that this can be overridden
    // idea from ViewPagerCards-master and developer.android.com/training/basics/firstapp/starting-activity
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MensaPickerActivity.class);
        startActivity(intent);
    }

    private void setUpToolbar() {
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(mMensa.getName());
    }

}
