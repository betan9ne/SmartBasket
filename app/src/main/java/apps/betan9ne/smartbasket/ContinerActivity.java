package apps.betan9ne.smartbasket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import fragments.InviteFragment;
import fragments.ListFragment;
import fragments.ListsFragment;
import fragments.SearchFragment;
import fragments.ShopFrament;

public class ContinerActivity extends AppCompatActivity {
    private FrameLayout viewPager;
    private Fragment fragment;
    SharedPreferences pref;
    // collections
    private SparseIntArray items;// used for change ViewPager selected item
    private List<Fragment> fragments;// used for ViewPager adapter
    BottomNavigationViewEx bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continer);

        bottomNavigationView= findViewById(R.id.bottom_bar);
        viewPager =findViewById(R.id.view_pager);
        pref = getApplication().getSharedPreferences("avenger", 0); // 0 - for private mode

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragment = new ShopFrament();
        loadFragment(fragment);
        initView();
        initData();

        if(pref.contains("cat_id")) {
            fragment = new ShopFrament();
            loadFragment(fragment);
        }

        }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.shop:
                    fragment = new ShopFrament();
                    loadFragment(fragment);
                    return true;
                case R.id.lists:
                    fragment = new ListsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.search:
                    fragment = new SearchFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.profile:
                    fragment = new InviteFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
        };
    /**
     * change BottomNavigationViewEx style
     */
    private void initView() {
        bottomNavigationView.enableItemShiftingMode(false);
        bottomNavigationView.enableAnimation(true);
    }

    /**
     * create fragments
     */
    private void initData() {
        fragments = new ArrayList<>(4);
        items = new SparseIntArray(4);

        // create music fragment and add it
        ShopFrament shop = new ShopFrament();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Shop");
        shop.setArguments(bundle);

        // create backup fragment and add it
        InviteFragment invite = new InviteFragment();
        bundle = new Bundle();
        bundle.putString("title", "Invites");
        invite.setArguments(bundle);

        // create friends fragment and add it
        ListsFragment list = new ListsFragment();
        bundle = new Bundle();
        bundle.putString("title", "List");
        list.setArguments(bundle);

        SearchFragment search = new SearchFragment();
        bundle = new Bundle();
        bundle.putString("title", "Search");
        search.setArguments(bundle);

        // add to fragments for adapter
        fragments.add(shop);
        fragments.add(list);
        fragments.add(search);
        fragments.add(invite);

        // add to items for change ViewPager item
        items.put(R.id.shop, 0);
        items.put(R.id.lists, 1);
        items.put(R.id.search, 2);
        items.put(R.id.profile, 3);

    }

    @Override
    public void onBackPressed() {
        //Close the application when back button is pressed
        finish();
    }

    public void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.view_pager, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
