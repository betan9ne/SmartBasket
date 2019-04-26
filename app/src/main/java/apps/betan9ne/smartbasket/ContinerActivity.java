package apps.betan9ne.smartbasket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import fragments.InviteFragment;
import fragments.ListFragment;
import fragments.SearchFragment;
import fragments.ShopFrament;

public class ContinerActivity extends AppCompatActivity {
    private ViewPager viewPager;
     private VpAdapter adapter;

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

        initView();
        initData();
        initEvent();

    }

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
        ListFragment list = new ListFragment();
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

        // set adapter
        adapter = new VpAdapter(getSupportFragmentManager(), fragments);
       viewPager.setAdapter(adapter);
    }

    /**
     * set listeners
     */
    private void initEvent() {
        // set listener to change the current item of view pager when click bottom nav item
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = 0;
                switch (item.getItemId()) {
                    case R.id.shop:
                        id = 0;
                        break;
                    case R.id.lists:
                        id = 1;
                        break;
                    case R.id.search:
                        id = 2;
                        case R.id.profile:
                        id = 3;
                        break;
                }
//                if(previousPosition != id) {
//                  bind.vp.setCurrentItem(id, false);
//                  previousPosition = id;
//                }

                // you can write as above.
                // I recommend this method. You can change the item order or counts without update code here.
             /*   int position = items.get(item.getItemId());
                if (previousPosition != position) {
                    // only set item when item changed
                    previousPosition = position;
                    Log.i("pager", "-----bnve-------- previous item:" + bottomNavigationView.getCurrentItem() + " current item:" + position + " ------------------");
                    bottomNavigationView.setCurrentItem(position);
                }*/
                return true;
            }
        });

        // set listener to change the current checked item of bottom nav when scroll view pager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("pager", "-----ViewPager-------- previous item:" + bottomNavigationView.getCurrentItem() + " current item:" + position + " ------------------");
                bottomNavigationView.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }

}
