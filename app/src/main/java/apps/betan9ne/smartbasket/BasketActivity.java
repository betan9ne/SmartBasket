package apps.betan9ne.smartbasket;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.BasketAdapter;
import butterknife.OnClick;
import fragments.BottomSheetFragment;
import fragments.invite_listFragment;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import helper.RecyclerItemTouchHelper;
import helper.SQLiteHandler;
import helper.SessionManagera;
import objects.BasketItem;
import objects.PartnersItem;
import butterknife.BindView;
public class BasketActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, ItemClickListener {
    Bundle b;
    ImageView imageView4, imageView5;
    TextView list_name, pat, partners, total;
     RecyclerView recyclerView;
    private BasketAdapter adapter;
    private ArrayList<BasketItem> feedItems;
    private ArrayList<PartnersItem> partnersItems;
    String b_id;
    ArrayAdapter<String> _adapter;
    static final String[] Numbers = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12","13","14","15" };
    Dialog dialog, dialog2;
    private SQLiteHandler db;
    private SessionManagera session;
    String u_id, user_id;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        list_name = findViewById(R.id.textView3);
        total = findViewById(R.id.textView13);
        pat = findViewById(R.id.textView7);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView4.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        dialog    = new Dialog(BasketActivity.this);
        dialog2    = new Dialog(BasketActivity.this);
        feedItems = new ArrayList<>();
        partnersItems = new ArrayList<>();
        adapter = new BasketAdapter(BasketActivity.this, feedItems);
        _adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Numbers);
         RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        db = new SQLiteHandler(getApplicationContext());

        session = new SessionManagera(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails(BasketActivity.class.getSimpleName());

        u_id = user.get("u_id");

        if(getIntent().getExtras() != null)
        {
            b = getIntent().getExtras();
            b_id = b.getString("id");
            user_id = b.getString("user_id");
            getPartners(b.getString("id"));
            getProducts(b.getString("id"));
            get_basket_total(b_id);
            list_name.setText(b.getString("name"));
        }
        if(u_id.equals(user_id))
        {
            imageView4.setVisibility(View.VISIBLE);
        }


        pat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.remove_partner);
                dialog.setTitle("Bridges");
                final EditText price = dialog.findViewById(R.id.price);
                Button update = (Button) dialog.findViewById(R.id.update);
                 final Spinner name =   dialog.findViewById(R.id.spinner4);

                List<String> lables = new ArrayList<String>();
                for (int i = 0; i < partnersItems.size(); i++) {
                    lables.add(partnersItems.get(i).getF_name());
                }
                // Creating adapter for spinner
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lables);
                // Drop down layout style - list view with radio button
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // attaching data adapter to spinner
                name.setAdapter(spinnerAdapter);

                name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        quant = partnersItems.get(position).getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //  Toast.makeText(AddItem.this, "ID  " , Toast.LENGTH_SHORT).show();
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete_partners(quant);
                        dialog.hide();
                    }
                });
                dialog.show();
            }
            });

        imageView4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                      bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                        Bundle bundle = new Bundle();
                        bundle.putString("b_id",b_id );
                        bundle.putString("name",list_name.getText().toString() );
                       bottomSheetFragment.setArguments(bundle);
                    }
                });


        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    public void getProducts(final String id)
    {
        final ProgressDialog diag  = new ProgressDialog(BasketActivity.this);
        diag.setMessage("Collecting data. Just a moment.");
        diag.show();
        feedItems.clear();
        //showDialog();
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
                                        //   Toast.makeText(ViewReceiptActivity.this, " " + feedArray.length() , Toast.LENGTH_LONG).show();
                                    } else {
                                        for (int i = 0; i < feedArray.length(); i++) {
                                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                                            BasketItem item = new BasketItem();
                                            item.setId(feedObj.getInt("id"));
                                            item.setName(feedObj.getString("name"));
                                            item.setPrice(feedObj.getDouble("price"));
                                            item.setQ(feedObj.getInt("q"));
                                            item.setAddedBy(feedObj.getString("f_name"));
                                            feedItems.add(item);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
//
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
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id+"");
                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof BasketAdapter.MyViewHolder) {
            Integer id = feedItems.get(viewHolder.getAdapterPosition()).getId();

            adapter.removeItem(viewHolder.getAdapterPosition());
            delete_item(id);
        }
    }

    public void delete_item(final Integer id)
    {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.delete_item,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d(response.toString());
                        if (response != null) {
                            try
                            {
                                JSONObject jObj = new JSONObject(response);
                                try {
                                    boolean error = jObj.getBoolean("error");
                                    // Check for error node in json
                                    if (!error) {
                                        getProducts(b_id);
                                        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                                    } else {

                                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id+"");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    public void delete_partners(final Integer id)
    {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.delete_partner,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d(response.toString());
                        if (response != null) {
                            try
                            {
                                JSONObject jObj = new JSONObject(response);
                                try {
                                    boolean error = jObj.getBoolean("error");
                                    // Check for error node in json
                                    if (!error) {
                                        getPartners(b_id);
                                        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                                    } else {

                                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("list_id", b_id+"");
                params.put("invite_id", id+"");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    Integer quant;
    @Override
    public void onClick(View view, int position) {
        final BasketItem city = feedItems.get(position);

        dialog.setContentView(R.layout.update_quantity2);
        dialog.setTitle("Bridges");
        final EditText price = dialog.findViewById(R.id.price);
        Button update = (Button) dialog.findViewById(R.id.update);
        TextView title = (TextView) dialog.findViewById(R.id.textView21);
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
                update(city.getId()+"", quant+"", price.getText()+"");

            }
        });
        dialog.show();
    }

    public void update(final String id, final String q, final String price) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.updateBasketItem, new Response.Listener<String>() {
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
                        getProducts(b.getString("id"));
                        get_basket_total(b_id);
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
                params.put("id", id);
                params.put("q", q);
                params.put("price", price);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void getPartners(final String id)
    {
        pat.setText("");
        partnersItems.clear();
           StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.getPartners,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                try {
                                    JSONArray feedArray = jObj.getJSONArray(("partner"));
                                    if (feedArray.length() == 0) {
                                    } else {
                                        for (int i = 0; i < feedArray.length(); i++) {
                                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                                            PartnersItem item =  new PartnersItem();
                                            item.setId(feedObj.getInt("liat_invite"));
                                            item.setF_name(feedObj.getString("name"));
                                            partnersItems.add(item);
                                             pat.append(feedObj.getString("name")+ ", ");
                                           }
                                    }
    } catch (JSONException e) {
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

    public void get_basket_total(final String id)
    {
        total.setText("");
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.get_basket_total,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String count = jObj.getString("count");
                                String _total = jObj.getString("total");
                                total.setText("Items: "+ count+", Total Cost: K"+_total);

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
