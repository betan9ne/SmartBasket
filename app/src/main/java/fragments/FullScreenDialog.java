package fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.betan9ne.smartbasket.AddReceiptActivity;
import apps.betan9ne.smartbasket.BasketActivity;
import apps.betan9ne.smartbasket.CreateList;
import apps.betan9ne.smartbasket.LoginActivity;
import apps.betan9ne.smartbasket.R;
import apps.betan9ne.smartbasket.ShopModeActivity;
import apps.betan9ne.smartbasket.ViewReceiptActivity;
import helper.AppConfig;
import helper.AppController;
import helper.SQLiteHandler;
import helper.SessionManagera;
import objects.BasketItem;
import objects.ProductItem;

public class FullScreenDialog extends DialogFragment {
    public static String TAG = "FullScreenDialog";
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ImageView iv;
    private TextView add_receipt, view_receipt, logout, email, shopping;
    private SQLiteHandler db;
    private ArrayList<ProductItem> listItem;
    String u_id;
    Dialog dialog;
    GoogleSignInClient googleSignInClient;
    private SessionManagera session;
    String list_id, list_name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.layout_full_screen_dialog, container, false);
        iv = v.findViewById(R.id.profile);
        add_receipt= v.findViewById(R.id.add_receipt);
        view_receipt= v.findViewById(R.id.view_receipt);
        logout= v.findViewById(R.id.logout);
        email= v.findViewById(R.id.email);
        shopping= v.findViewById(R.id.shopping_mode);

        listItem = new ArrayList<ProductItem>();
        dialog    = new Dialog(getContext());
        session = new SessionManagera(getContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        db = new SQLiteHandler(getContext());
        HashMap<String, String> user = db.getUserDetails(invite_listFragment.class.getSimpleName());
        u_id = user.get("u_id");
        email.setText(user.get("email"));
        getMyLists(u_id);
        if (imageLoader == null)
        {
            imageLoader = AppController.getInstance().getImageLoader();
        }
        imageLoader.get(user.get("photo"), new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("image", "Image Load Error: " + error.getMessage());
            }
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    iv.setImageBitmap(response.getBitmap());
                }
            }
        });

        shopping.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                dialog.setContentView(R.layout.pick_list_dialog);
                dialog.setTitle("Bridges");
                Button update = (Button) dialog.findViewById(R.id.update);
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

               // title.setText(list.getName());

                lists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        list_id =  listItem.get(position).getId() + "";
                        list_name = listItem.get(position).getName();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //  Toast.makeText(AddItem.this, "ID  " , Toast.LENGTH_SHORT).show();
                    }
                });
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), ShopModeActivity.class);
                        i.putExtra("name", list_name);
                        i.putExtra("id", list_id);
                        startActivity(i);
                      }
                });
                dialog.show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                logoutUser();
            }
        });

        add_receipt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(getContext(), AddReceiptActivity.class);
                startActivity(intent);
            }
        });

        view_receipt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(getContext(), ViewReceiptActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    public void logoutUser() {
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                session.setLogin(false);
                Intent intent=new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                db.deleteUsers();
            }
        });

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
