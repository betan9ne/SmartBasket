package fragments;

import android.app.Dialog;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.CatAdapter;
import adapters.ShopItemAdapter;
import apps.betan9ne.smartbasket.R;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import objects.BasketItem;
import objects.TagItem;

public class CategoryFragment extends DialogFragment  implements ItemClickListener {
    private RecyclerView recyclerView;
    private ArrayList<TagItem> feedItems;
    private CatAdapter adapter;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.cat_list, container, false);

        recyclerView =  v.findViewById(R.id.list);
        feedItems = new ArrayList<>();
        adapter = new CatAdapter(getContext(), feedItems);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        list();
        return  v;
    }

    @Override
    public void onClick(View view, int position) {
        final TagItem list = feedItems.get(position);
    }

    public void list()
    {
        recyclerView.removeAllViews();
        feedItems.clear();
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
                                            //     Toast.makeText(ViewReceiptActivity.this, ""+ feedObj.length() , Toast.LENGTH_SHORT).show();
                                            TagItem item = new TagItem();
                                            item.setId(feedObj.getInt("id"));
                                            item.setName(feedObj.getString("category"));
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



    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    }
