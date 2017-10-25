package com.example.android.aa.tabshop;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.aa.R;
import com.example.android.aa.tabshop.TabFragments.FoodFragment;
import com.example.android.aa.tabshop.TabFragments.StuffFragment;
import com.example.android.aa.tabshop.TabFragments.MedicineFragment;

public class ShopTab extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.food,
            R.drawable.stuff,
            R.drawable.medicine
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_tab);
        toolbar = (Toolbar) findViewById(R.id.shoptoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Animal Aid Shop"); // not displaying
        // toolbar.setDisplayHomeAsUpEnabled(true);  no use
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true); // not working but displaying
        toolbar.inflateMenu(R.menu.menu_main); //not workin here

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FoodFragment(), "Food");
        adapter.addFragment(new StuffFragment(), "Stuff");
        adapter.addFragment(new MedicineFragment(), "Medicine");
        viewPager.setAdapter(adapter);

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
}
