package fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.betan9ne.smartbasket.R;
import helper.AppConfig;
import helper.AppController;
import helper.SQLiteHandler;
import helper.SessionManagera;

public class ListsFragment extends Fragment{
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SQLiteHandler db;

    String u_id;
    Dialog dialog;
    public ListsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lists_fragment, container, false);

        mViewPager = v.findViewById(R.id.container);
        setupViewPager(mViewPager);
        dialog    = new Dialog(getContext());
        mTabLayout =  v.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        db = new SQLiteHandler(getContext());
         HashMap<String, String> user = db.getUserDetails(invite_listFragment.class.getSimpleName());

        u_id = user.get("u_id");

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.add_basket);
                dialog.setTitle("Bridges");
                final EditText name = dialog.findViewById(R.id.name);
                Button update =  dialog.findViewById(R.id.update);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add_basket(name.getText()+"", u_id);
                    }
                });
                dialog.show();
            }
        });


        return v;
    }

    public void refresh_frag()
    {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
    private void add_basket(final String name, final String u_id) {

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.add_basket, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        dialog.hide();
                        refresh_frag();

                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),										error.getMessage() + " response error", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("u_id", u_id);
                params.put("name", name);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ListFragment(), "My Lists");
        adapter.addFragment(new invite_listFragment(), "Invited Lists");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
