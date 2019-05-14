package fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.CatAdapter;
import adapters.ShopItemAdapter;
import apps.betan9ne.smartbasket.ContinerActivity;
import apps.betan9ne.smartbasket.LoginActivity;
import apps.betan9ne.smartbasket.R;
import apps.betan9ne.smartbasket.ViewReceiptActivity;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import helper.ItemClickListenerB;
import helper.SQLiteHandler;
import helper.SessionManagera;
import objects.BasketItem;
import objects.ProductItem;
import objects.TagItem;

import static apps.betan9ne.smartbasket.R.*;
import static apps.betan9ne.smartbasket.R.id.*;

public class ShopFrament extends Fragment implements ItemClickListener, ItemClickListenerB {
    private RecyclerView recyclerView;
    private ShopItemAdapter adapter;
    private ArrayList<BasketItem> feedItems;
    private ArrayList<ProductItem> listItem;

    private SQLiteHandler db;
    private SessionManagera session;
    String u_id, cat_id, cat_name;
    Dialog dialog;
    Spinner spinner5;
    TextView pick_cat;
    ArrayAdapter<String> _adapter;
    private ArrayList<String> lists = new ArrayList<String>();
    SharedPreferences pref;
    static final String[] Numbers = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12","13","14","15" };
    ContinerActivity asd;
    private ArrayList<TagItem> feedItems_;
    CatAdapter adapter_;
    public ShopFrament(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(layout.activity_shop, container, false);
        recyclerView =  v.findViewById(list);
        RecyclerView cat_list = v.findViewById(id.cat_list);
        pick_cat = v.findViewById(id.pick_cat);
        spinner5= v.findViewById(id.spinner5);
        ImageView view_receipt =  v.findViewById(id.view_receipt);
        pref = getContext().getSharedPreferences("avenger", 0); // 0 - for private mode

        dialog    = new Dialog(getContext());
        feedItems = new ArrayList<>();
        listItem = new ArrayList<ProductItem>();

        asd = new ContinerActivity();
        adapter = new ShopItemAdapter(getContext(), feedItems);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        LinearLayoutManager lasd = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        _adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Numbers);
        feedItems_ = new ArrayList<TagItem>();
        adapter_ = new CatAdapter(getContext(), feedItems_);
        cat_list.setLayoutManager(lasd);
          cat_list.setItemAnimator(new DefaultItemAnimator());
        cat_list.setAdapter(adapter_);
        adapter_.setClickListener(new ItemClickListenerB()
        {
            @Override
            public void onClick(View view, int position) {
                TagItem list = feedItems_.get(position);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("cat_id", list.getId()+""); // Storing string
                editor.putString("cat_name", list.getName()); // Storing string
                editor.apply();
                  getCatItems(list.getId()+"");
                pick_cat.setText(list.getName());
            }
        }
        );

        db = new SQLiteHandler(getContext());

        session = new SessionManagera(getContext());
        HashMap<String, String> user = db.getUserDetails(invite_listFragment.class.getSimpleName());

        u_id = user.get("u_id");

        if(pref.contains("cat_id"))
        {
             cat_name = pref.getString("cat_name", null); // getting String
            pick_cat.setText(cat_name);
                cat_id = pref.getString("cat_id", null);
            getCatItems(cat_id);
        }
        else
        {
            list();
        }

        view_receipt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FullScreenDialog dialog = new FullScreenDialog();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, FullScreenDialog.TAG);       }
                });

        getMyLists(u_id);
         get_category();

        return v;
    }



    String list_id;
    Integer quant;
    @Override
    public void onClick(View view, int position) {
           final BasketItem list = feedItems.get(position);

                dialog.setContentView(layout.update_quantity);
                dialog.setTitle("Bridges");
                final EditText price = dialog.findViewById(id.price);
                Button update = (Button) dialog.findViewById(id.update);
                TextView title = (TextView) dialog.findViewById(textView21);
                final Spinner name = (Spinner) dialog.findViewById(spinner4);
                final Spinner lists = (Spinner) dialog.findViewById(id.spinner5);

                List<String> lables = new ArrayList<String>();
                for (int i = 0; i < listItem.size(); i++) {
                    lables.add(listItem.get(i).getName());
                }
                // Creating adapter for spinner
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lables);
                // Drop down layout style - list view with radio button
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // attaching data adapter to spinner
                lists.setAdapter(spinnerAdapter);

                title.setText(list.getName());
                price.setText(list.getPrice()+"");
                _adapter.setDropDownViewResource(layout.item_spinner);
                name.setAdapter(_adapter);
                name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        quant = Integer.parseInt(parent.getItemAtPosition(position).toString());
                     }
         @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                     }
                });
       lists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        list_id =  listItem.get(position).getId() + "";
                    }
          @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                     }
                });
                        update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        update(list_id, list.getId()+"", price.getText()+"", quant+"", u_id);
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
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        dialog.hide();

                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
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

    public void get_category()
    {
         feedItems_.clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.get_category,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                try {
                                    JSONArray feedArray = jObj.getJSONArray(("cat"));
                                    if (feedArray.length() == 0) {
                                    } else {
                                        for (int i = 0; i < feedArray.length(); i++) {
                                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                                            TagItem item = new TagItem();
                                            item.setId(feedObj.getInt("id"));
                                            item.setName(feedObj.getString("category"));
                                            feedItems_.add(item);
                                        }
                                    }
                                    adapter_.notifyDataSetChanged();
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
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);

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
                                            //     Toast.makeText(ViewReceiptActivity.this, ""+ feedObj.length() , Toast.LENGTH_SHORT).show();
                                            BasketItem item = new BasketItem();
                                            item.setId(feedObj.getInt("id"));
                                            item.setName(feedObj.getString("name"));
                                            item.setPrice(feedObj.getDouble("price"));
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
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

 public void getCatItems(final String cat_id)
    {
        recyclerView.removeAllViews();
        feedItems.clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.getCatItems,
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
                                            //     Toast.makeText(ViewReceiptActivity.this, ""+ feedObj.length() , Toast.LENGTH_SHORT).show();
                                            BasketItem item = new BasketItem();
                                            item.setId(feedObj.getInt("id"));
                                            item.setName(feedObj.getString("name"));
                                            item.setPrice(feedObj.getDouble("price"));
                                            feedItems.add(item);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                  }
     } catch (JSONException e) {
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
                params.put("id", cat_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    public void getMyLists(final String id)
    {
        listItem.clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.getMyLists,
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
                                            ProductItem item = new ProductItem();
                                            item.setId(feedObj.getInt("id"));
                                            item.setName(feedObj.getString("name"));
                                            listItem.add(item);
                                        }
                                    }
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

    public static ShopFrament newInstance(String text) {
        ShopFrament f = new ShopFrament();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }
}

