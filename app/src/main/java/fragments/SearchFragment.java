package fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.ShopItemAdapter;
import apps.betan9ne.smartbasket.R;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import helper.SQLiteHandler;
import helper.SessionManagera;
import objects.BasketItem;
import objects.ProductItem;
import objects.TagItem;

public class SearchFragment extends Fragment implements ItemClickListener {
    private RecyclerView recyclerView;
    private ShopItemAdapter adapter;
    private ArrayList<BasketItem> feedItems;
    private ArrayList<ProductItem> listItem;
    SearchView search_;
    TextView coun;
    LinearLayout line0;
    Bundle b;
    private ArrayList<TagItem> tagItem;
    Dialog dialog;
    ImageView empty;
    private SQLiteHandler db;
    private SessionManagera session;
    String u_id;
    ChipCloud chipCloud;
    ArrayAdapter<String> _adapter;
    static final String[] Numbers = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12","13","14","15" };

    public SearchFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_search, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.list);
        search_ = (SearchView) v.findViewById(R.id.search);
        coun = (TextView) v.findViewById(R.id.ago);
        empty = v.findViewById(R.id.imageView6);
        dialog    = new Dialog(getContext());
        chipCloud = v.findViewById(R.id.chip_cloud);
        feedItems = new ArrayList<>();
        listItem = new ArrayList<ProductItem>();
        adapter = new ShopItemAdapter(getContext(), feedItems);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        _adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Numbers);
        tagItem = new ArrayList<>();
        chipCloud.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                TagItem item = tagItem.get(index);
                recyclerView.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                search(item.getName());
            }
            @Override
            public void chipDeselected(int index) {
                //...
            }
        });

        search_.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //   search(s);
                search_.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().length() == 0)
                {
                    recyclerView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                    search(s);
                }

                return true;
            }
        });

        search_.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    empty.setVisibility(View.GONE);
                  } else {
                 }
            }
        });
        db = new SQLiteHandler(getContext());

        session = new SessionManagera(getContext());
        HashMap<String, String> user = db.getUserDetails(invite_listFragment.class.getSimpleName());
        get_Tags();
        u_id = user.get("u_id");
        getMyLists(u_id);
        return v;
    }
    String list_id;
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
        final Spinner lists = (Spinner) dialog.findViewById(R.id.spinner5);

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
        lists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list_id =  listItem.get(position).getId() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //  Toast.makeText(AddItem.this, "ID  " , Toast.LENGTH_SHORT).show();
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

    public void get_Tags()
    {
        tagItem.clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.getTags,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                try {
                                    JSONArray feedArray = jObj.getJSONArray(("search"));
                                    if (feedArray.length() == 0) {
                                    } else {
                                        for (int i = 0; i < feedArray.length(); i++) {
                                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                                            TagItem item = new TagItem();
                                            item.setName(feedObj.getString("term"));
                                            tagItem.add(item);
                                            chipCloud.addChip(feedObj.getString("term"));
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
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void search(final String search)
    {
        recyclerView.removeAllViews();
        feedItems.clear();

        //showDialog();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.search,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                try {
                                    JSONArray feedArray = jObj.getJSONArray(("items"));
                                    if (feedArray.length() == 0) {
                                        coun.setText("No Items Found");
                                        recyclerView.setVisibility(View.GONE);
                                        empty.setVisibility(View.VISIBLE);
                                    } else {
                                        coun.setText(feedArray.length()+" Items Found");
                                        for (int i = 0; i < feedArray.length(); i++) {
                                            JSONObject feedObj = (JSONObject) feedArray.get(i);

                                            BasketItem item = new BasketItem();
                                            item.setId(feedObj.getInt("id"));
                                            item.setName(feedObj.getString("name"));
                                            item.setPrice(feedObj.getDouble("price"));
                                            feedItems.add(item);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
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
                params.put("search", search);
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
    public static SearchFragment newInstance(String text) {
        SearchFragment f = new SearchFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }
}

