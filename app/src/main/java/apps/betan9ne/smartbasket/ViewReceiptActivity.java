package apps.betan9ne.smartbasket;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.github.chrisbanes.photoview.PhotoView;

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
    Dialog dialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Bundle b;
    String u_id;
    ImageView addlist;
    String list_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_receipt);
        recyclerView =  findViewById(R.id.shopping_list);
        feedItems = new ArrayList<>();

        dialog    = new Dialog(getApplicationContext());
        adapter = new ReceiptAdapter(ViewReceiptActivity.this, feedItems);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        db = new SQLiteHandler(getApplicationContext());
        if(getIntent().getExtras() != null)
        {
            b = getIntent().getExtras();
            list_id = b.getString("list_id");
        }

        HashMap<String, String> user = db.getUserDetails(invite_listFragment.class.getSimpleName());

        u_id = user.get("u_id");
        list(list_id);
    }

    @Override
    public void onClick(View view, int position) {
        final ReceiptItem list = feedItems.get(position);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewReceiptActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.view_custom_layout, null);
      final  PhotoView photoView = mView.findViewById(R.id.imageView);
      final TextView title = mView.findViewById(R.id.title);
      final TextView close= mView.findViewById(R.id.close);
        title.setText(list.getTitle()+" - " + list.getDescr());

        if (imageLoader == null)
        {
            imageLoader = AppController.getInstance().getImageLoader();
        }
        imageLoader.get(list.getReceipt(),  new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("image", "Image Load Error: " + error.getMessage());
            }
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    photoView.setImageBitmap(response.getBitmap());
                  }
            }
        });
        mBuilder.setView(mView);
     final  AlertDialog mDialog = mBuilder.create();
        mDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

        public void list(final String id)
    {
        recyclerView.removeAllViews();
        feedItems.clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.get_list_receipt,
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
                params.put("id", id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq);
   }
}
