package apps.betan9ne.smartbasket;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapters.BasketAdapter;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import helper.RecyclerItemTouchHelper;
import objects.BasketItem;

public class ShopModeActivity extends AppCompatActivity implements ItemClickListener {
     DatabaseReference mDatabase;
    TextView l_name;
    RecyclerView recyclerView;
    Dialog dialog;
    String key;
    private BasketAdapter adapter;
    private ArrayList<BasketItem> feedItems;
    ArrayAdapter<String> _adapter;
    static final String[] Numbers = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12","13","14","15" };
String list_name;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_mode);
        l_name= findViewById(R.id.textView3);
        Intent intent = getIntent();
        recyclerView = findViewById(R.id.list);


        feedItems = new ArrayList<>();
        adapter = new BasketAdapter(ShopModeActivity.this, feedItems);
        _adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Numbers);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        dialog    = new Dialog(ShopModeActivity.this);
        if(getIntent().getExtras() != null) {
            b = getIntent().getExtras();
            l_name.setText(b.getString("name"));
            list_name = b.getString("name");
            mDatabase = FirebaseDatabase.getInstance().getReference().child(list_name);

            getStarted(b.getString("id"));
        }
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                feedItems.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BasketItem track = postSnapshot.getValue(BasketItem.class);
                    feedItems.add(track);
                }
                BasketAdapter trackListAdapter = new BasketAdapter(ShopModeActivity.this, feedItems);
                trackListAdapter.setClickListener(ShopModeActivity.this);
                recyclerView.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                feedItems.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    key = postSnapshot.getKey();
                    BasketItem track = postSnapshot.getValue(BasketItem.class);
                    feedItems.add(track);
                }
                BasketAdapter trackListAdapter = new BasketAdapter(ShopModeActivity.this, feedItems);
                trackListAdapter.setClickListener(ShopModeActivity.this);
                recyclerView.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private boolean updateItem(String key, Integer id, String name, String addedby, Double price, Integer quan, String list_id) {
        //getting the specified artist reference
        mDatabase = FirebaseDatabase.getInstance().getReference(list_name).child(id+"");

        //updating artist
        BasketItem basket = new BasketItem(id, name, list_id, addedby, price, quan);
        mDatabase.setValue(basket);
        Toast.makeText(getApplicationContext(), "Item Updated", Toast.LENGTH_LONG).show();
        return true;
    }
    Integer quant;
    @Override
    public void onClick(View view, int position) {
        final BasketItem city = feedItems.get(position);
        dialog.setContentView(R.layout.update_quantity2);
        dialog.setTitle("Bridges");
        final EditText price = dialog.findViewById(R.id.price);
        Button update = (Button) dialog.findViewById(R.id.update);
        final TextView title = (TextView) dialog.findViewById(R.id.textView21);
        final Spinner name = (Spinner) dialog.findViewById(R.id.spinner4);

        title.setText(city.getName());
        price.setText(city.getPrice()+"");
        _adapter.setDropDownViewResource(R.layout.item_spinner);
        name.setAdapter(_adapter);
        name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quant = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //  Toast.makeText(AddItem.this, "ID  " , Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String key = mDatabase.child(list_name).push().getKey();

        updateItem(key, city.getId(), title.getText().toString(), city.getAddedBy(), Double.parseDouble(price.getText().toString()),
                        quant, city.getList_id());
               dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addItems(Integer id, String name, String addedby, Double price, Integer quan, String list_id) {
           if (!TextUtils.isEmpty(name)) {
       String _id = mDatabase.push().getKey();
     BasketItem basket = new BasketItem(id, name, list_id, addedby, price, quan);
            mDatabase.child(id+"").setValue(basket);
              Toast.makeText(this, "item added", Toast.LENGTH_LONG).show();
        } else {
              Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
        }
    }



    public void getStarted(final String id)
    {
        final ProgressDialog diag  = new ProgressDialog(ShopModeActivity.this);
        diag.setMessage("Collecting data. Just a moment.");
        diag.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,  AppConfig.getBasketItems,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                try {
                                    JSONArray feedArray = jObj.getJSONArray(("basket"));
                                    if (feedArray.length() == 0) {
                                    } else {
                                        for (int i = 0; i < feedArray.length(); i++) {
                                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                                            addItems(feedObj.getInt("id"), feedObj.getString("name"),
                                                    feedObj.getString("f_name"), feedObj.getDouble("price"),
                                                    feedObj.getInt("q"), id);
                                        }
                                    }
                                    } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                diag.hide();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id+"");
                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


}
