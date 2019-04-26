package apps.betan9ne.smartbasket;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapters.ShopItemAdapter;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import objects.BasketItem;

public class ShopActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView recyclerView;
    private ShopItemAdapter adapter;
    private ArrayList<BasketItem> feedItems;
    Dialog dialog;
    ArrayAdapter<String> _adapter;
    static final String[] Numbers = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12","13","14","15" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        recyclerView =  findViewById(R.id.list);
        dialog    = new Dialog(ShopActivity.this);
        feedItems = new ArrayList<>();
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom_bar);
        adapter = new ShopItemAdapter(ShopActivity.this, feedItems);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        _adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Numbers);

        list();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.shop:
                                break;
                            case R.id.lists:
                                Intent intenta = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intenta);
                                break;
                            case R.id.profile:
                                Intent intent = new Intent(getApplicationContext(), InvitesActivity.class);
                                startActivity(intent);
                                break;

                        }
                        return true;
                    }
                });
    }
    Integer quant;
    @Override
    public void onClick(View view, int position) {
        final BasketItem list = feedItems.get(position);

        dialog.setContentView(R.layout.update_quantity);
        dialog.setTitle("Bridges");
        final EditText price = dialog.findViewById(R.id.price);
        Button update = (Button) dialog.findViewById(R.id.update);
        TextView title = (TextView) dialog.findViewById(R.id.textView21);
        final Spinner name = (Spinner) dialog.findViewById(R.id.spinner4);

        title.setText(list.getName());
        price.setText(list.getPrice()+"");
        _adapter.setDropDownViewResource(R.layout.item_spinner);
        name.setAdapter(_adapter);
        name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quant = Integer.parseInt(parent.getItemAtPosition(position).toString());
                //    Toast.makeText(ViewItem.this, ""+ quant, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //  Toast.makeText(AddItem.this, "ID  " , Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update("2", list.getId()+"", price.getText()+"", quant+"", "4");
            }
        });
        dialog.show();
    }

    public void update(final String list_id, final String item_id, final String price, final String quantity, final String added_by) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_item_basket, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        dialog.hide();

                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),										error.getMessage() + " response error", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("list_id", list_id);
                params.put("item_id", item_id);
                params.put("quantity", quantity);
                params.put("price", price);
                params.put("added_by", added_by);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void list()
    {
        recyclerView.removeAllViews();
        feedItems.clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.getItems,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                try {
                                    JSONArray feedArray = jObj.getJSONArray(("items"));
                                    if (feedArray.length() == 0) {
                                    } else {
                                        for (int i = 0; i < feedArray.length(); i++) {
                                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                                            //     Toast.makeText(MainActivity.this, ""+ feedObj.length() , Toast.LENGTH_SHORT).show();
                                            BasketItem item = new BasketItem();
                                            item.setId(feedObj.getInt("id"));
                                            item.setName(feedObj.getString("name"));
                                             item.setPrice(feedObj.getDouble("price"));
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
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}