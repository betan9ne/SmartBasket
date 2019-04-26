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
import android.widget.Button;
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
import apps.betan9ne.smartbasket.InvitesActivity;
import apps.betan9ne.smartbasket.R;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import objects.PartnersItem;

public class InviteFragment extends Fragment implements ItemClickListener {
    private RecyclerView recyclerView;
    private PartnersAdapter adapter;
    private ArrayList<PartnersItem> feedItems;
    Dialog dialog;
    public InviteFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_invites, container, false);

        recyclerView =  v.findViewById(R.id.list);
        dialog    = new Dialog(getContext());
        feedItems = new ArrayList<>();

        adapter = new PartnersAdapter(getContext(), feedItems);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        getInvites(7+"");

        return v;
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
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        dialog.hide();
                        getInvites(7+"");
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
                                            //     Toast.makeText(MainActivity.this, ""+ feedObj.length() , Toast.LENGTH_SHORT).show();
                                            PartnersItem item = new PartnersItem();
                                            item.setId(feedObj.getInt("id"));
                                            item.setName(feedObj.getString("name"));
                                            item.setF_name(feedObj.getString("f_name"));
                                            item.setStatus(feedObj.getInt("status"));
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

    public static InviteFragment newInstance(String text) {
        InviteFragment f = new InviteFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }
}

