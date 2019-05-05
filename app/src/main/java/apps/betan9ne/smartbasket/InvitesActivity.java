package apps.betan9ne.smartbasket;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapters.PartnersAdapter;
import adapters.ShopItemAdapter;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import objects.BasketItem;
import objects.PartnersItem;

public class InvitesActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView recyclerView;
    private PartnersAdapter adapter;
    private ArrayList<PartnersItem> feedItems;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);

        recyclerView =  findViewById(R.id.list);
        dialog    = new Dialog(InvitesActivity.this);
        feedItems = new ArrayList<>();

        adapter = new PartnersAdapter(InvitesActivity.this, feedItems);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        getInvites(7+"");
    }

    @Override
    public void onClick(View view, int position) {
        final PartnersItem list = feedItems.get(position);

        dialog.setContentView(R.layout.dialog_invite_accept);
        dialog.setTitle("Bridges");
        Button accept = dialog.findViewById(R.id.accept);
        Button decline =  dialog.findViewById(R.id.decline);
        TextView title =  dialog.findViewById(R.id.textView21);

        title.setText(list.getF_name() + " has invited you to join their " + list.getName());

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               update(list.getId()+"", "1" );
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               update(list.getId()+"", "2" );
            }
        });
        dialog.show();
    }
    public void update(final String id, final String status) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.update_invite, new Response.Listener<String>() {
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
                        getInvites(7+"");
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
                params.put("status", status);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void getInvites(final String id)
    {
        recyclerView.removeAllViews();
        feedItems.clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.getInvites,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                try {
                                    JSONArray feedArray = jObj.getJSONArray(("invites"));
                                    if (feedArray.length() == 0) {
                                    } else {
                                        for (int i = 0; i < feedArray.length(); i++) {
                                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                                            //     Toast.makeText(ViewReceiptActivity.this, ""+ feedObj.length() , Toast.LENGTH_SHORT).show();
                                            PartnersItem item = new PartnersItem();
                                            item.setId(feedObj.getInt("id"));
                                            item.setName(feedObj.getString("name"));
                                            item.setF_name(feedObj.getString("f_name"));
                                            item.setStatus(feedObj.getString("status"));
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
