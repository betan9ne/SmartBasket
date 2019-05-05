package apps.betan9ne.smartbasket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import adapters.ReceiptAdapter;
import fragments.invite_listFragment;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import helper.SQLiteHandler;
import helper.SessionManagera;
import objects.ProductItem;
import objects.ReceiptItem;

public class ViewReceiptActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView recyclerView;
    private ReceiptAdapter adapter;
    private ArrayList<ReceiptItem> feedItems;
    private SQLiteHandler db;

    String u_id;
    ImageView addlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_receipt);
        recyclerView =  findViewById(R.id.shopping_list);
        feedItems = new ArrayList<>();


        adapter = new ReceiptAdapter(ViewReceiptActivity.this, feedItems);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        db = new SQLiteHandler(getApplicationContext());


        HashMap<String, String> user = db.getUserDetails(invite_listFragment.class.getSimpleName());

        u_id = user.get("u_id");
        list(u_id);
    }

    @Override
    public void onClick(View view, int position) {
        final ReceiptItem list = feedItems.get(position);
      //  Toast.makeText(this, ""+ list.getName(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(ViewReceiptActivity.this, BasketActivity.class);
        i.putExtra("name", list.getReceipt());
        i.putExtra("id", list.getId()+"");
        startActivity(i);
    }

        public void list(final String id)
    {
        recyclerView.removeAllViews();
        feedItems.clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.get_receipt,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                try {
                                    JSONArray feedArray = jObj.getJSONArray(("receipt"));
                                    if (feedArray.length() == 0) {
                                    } else {
                                        for (int i = 0; i < feedArray.length(); i++) {
                                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                                       //     Toast.makeText(ViewReceiptActivity.this, ""+ feedObj.length() , Toast.LENGTH_SHORT).show();
                                            ReceiptItem item = new ReceiptItem();
                                          item.setId(feedObj.getInt("id"));
                                            item.setReceipt(feedObj.getString("receipt"));
                                          item.setDescr(feedObj.getString("descr"));
                                          item.setTitle(feedObj.getString("title"));
                                              feedItems.add(item);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                //    Toast.makeText(ViewReceiptActivity.this, "hi"+ e.getMessage() , Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                             //   Toast.makeText(ViewReceiptActivity.this, "hi"+ e.getMessage() , Toast.LENGTH_SHORT).show();
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
