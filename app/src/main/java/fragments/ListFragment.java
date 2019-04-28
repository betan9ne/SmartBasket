package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import apps.betan9ne.smartbasket.BasketActivity;
import apps.betan9ne.smartbasket.CreateList;
import apps.betan9ne.smartbasket.R;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import helper.SQLiteHandler;
import helper.SessionManagera;
import objects.ProductItem;

public class ListFragment extends Fragment implements ItemClickListener {
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private ArrayList<ProductItem> feedItems;
    ImageView addlist;
    private SQLiteHandler db;
    private SessionManagera session;
    String u_id;
    public ListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_main, container, false);

        addlist = v.findViewById(R.id.imageView3);
        recyclerView =  v.findViewById(R.id.shopping_list);
        feedItems = new ArrayList<>();


        adapter = new ListAdapter(getContext(), feedItems);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        addlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateList.class);
                startActivity(intent);
            }
        });
        db = new SQLiteHandler(getContext());

        session = new SessionManagera(getContext());
        HashMap<String, String> user = db.getUserDetails(ListFragment.class.getSimpleName());

        u_id = user.get("u_id");

        list(u_id);

        return v;
    }


    @Override
    public void onClick(View view, int position) {
        final ProductItem list = feedItems.get(position);
        //  Toast.makeText(this, ""+ list.getName(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(), BasketActivity.class);
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

    public static ListFragment newInstance(String text) {
        ListFragment f = new ListFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }
}

