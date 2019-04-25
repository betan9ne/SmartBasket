package apps.betan9ne.smartbasket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapters.ListAdapter;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import objects.ProductItem;

public class MainActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private ArrayList<ProductItem> feedItems;
    ImageView addlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addlist = findViewById(R.id.imageView3);
        recyclerView =  findViewById(R.id.shopping_list);
        feedItems = new ArrayList<>();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        adapter = new ListAdapter(MainActivity.this, feedItems);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        addlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateList.class);
                startActivity(intent);
            }
            });
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.shop:
                                Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.lists:

                            case R.id.profile:

                        }
                        return true;
                    }
                });
        list(1+"");
    }

    @Override
    public void onClick(View view, int position) {
        final ProductItem list = feedItems.get(position);
      //  Toast.makeText(this, ""+ list.getName(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this, BasketActivity.class);
        i.putExtra("name", list.getName());
        i.putExtra("id", list.getId()+"");
        startActivity(i);
    }

        public void list(final String id)
    {
        recyclerView.removeAllViews();
        feedItems.clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.getLists,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                try {
                                    JSONArray feedArray = jObj.getJSONArray(("list"));
                                    if (feedArray.length() == 0) {
                                    } else {
                                        for (int i = 0; i < feedArray.length(); i++) {
                                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                                       //     Toast.makeText(MainActivity.this, ""+ feedObj.length() , Toast.LENGTH_SHORT).show();
                                            ProductItem item = new ProductItem();
                                          item.setId(feedObj.getInt("id"));
                                          item.setName(feedObj.getString("name"));
                                          item.setUser_id(feedObj.getInt("user_id"));
                                          item.setItem_count(feedObj.getInt("item_count"));
                                            feedItems.add(item);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                //    Toast.makeText(MainActivity.this, "hi"+ e.getMessage() , Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                             //   Toast.makeText(MainActivity.this, "hi"+ e.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                        //	pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //	pDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


}
