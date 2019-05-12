package fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import apps.betan9ne.smartbasket.AddReceiptActivity;
import apps.betan9ne.smartbasket.BasketActivity;
import apps.betan9ne.smartbasket.ContinerActivity;
import apps.betan9ne.smartbasket.R;
import apps.betan9ne.smartbasket.ShopModeActivity;
import apps.betan9ne.smartbasket.ViewReceiptActivity;
import helper.AppConfig;
import helper.AppController;
import helper.SQLiteHandler;
import objects.ProductItem;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    public BottomSheetFragment() {
        // Required empty public constructor
    }
    Dialog dialog, dialog2;
    TextView edit_list_name, add_partner, delete_list, shopping_mode, add_receipt, view_receipt;
    private SQLiteHandler db;
    private ArrayList<ProductItem> listItem;
    String u_id, b_id, list_name;
    String list_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bottom_sheet, container, false);
        if (getArguments() != null) {
           b_id= getArguments().getString("b_id");
            list_name= getArguments().getString("name");
        }
        edit_list_name = v.findViewById(R.id.edit_list_name);
        add_partner = v.findViewById(R.id.add_partner);
        delete_list = v.findViewById(R.id.delete_list);
        shopping_mode = v.findViewById(R.id.shopping_mode);
        add_receipt = v.findViewById(R.id.add_receipt);
        view_receipt= v.findViewById(R.id.view_receipt);

        db = new SQLiteHandler(getContext());
        HashMap<String, String> user = db.getUserDetails(invite_listFragment.class.getSimpleName());
        u_id = user.get("u_id");

        dialog    = new Dialog(getContext());
        dialog2    = new Dialog(getContext());

        edit_list_name.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.setContentView(R.layout.update_list_name);
                dialog.setTitle("Bridges");
                final EditText name = dialog.findViewById(R.id.name);
                Button update = (Button) dialog.findViewById(R.id.update);
                TextView title = (TextView) dialog.findViewById(R.id.textView21);

                title.setText(list_name);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        update_basket_name(b_id +"", name.getText().toString());

                    }
                });
                dialog.show();
            }
        });

        add_partner.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.invite_partner);
                dialog.setTitle("Bridges");
                final EditText email = dialog.findViewById(R.id.email);
                Button update = (Button) dialog.findViewById(R.id.update);
                TextView title = (TextView) dialog.findViewById(R.id.textView21);

               title.setText(list_name);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        send_invite(u_id, email.getText().toString(), b_id, email.getText().toString());
                    }
                });
                dialog.show();
            }
            });

        delete_list.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                delete_list((b_id));
            }
        });

        shopping_mode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                        String session = UUID.randomUUID().toString().replace("-", "");
                        Intent i = new Intent(getContext(), ShopModeActivity.class);
                        i.putExtra("name", list_name);
                        i.putExtra("id", b_id);
                        i.putExtra("session", session);
                        startActivity(i);
                    }
        });

        add_receipt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddReceiptActivity.class);
                i.putExtra("list_id", b_id);
                startActivity(i);
            }
        });

        view_receipt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ViewReceiptActivity.class);
                i.putExtra("list_id", b_id);
                startActivity(i);

            }
        });
        return v;
    }


    public void delete_list(final String id)
    {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.delete_list,
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
                                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getContext(), ContinerActivity.class);
                                         startActivity(i);
                                       } else {

                                        Toast.makeText(getContext(), "failed", Toast.LENGTH_LONG).show();
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
                params.put("id", id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void send_invite(final String list_owner, final String list_invite, final String list_id, final String email){

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.send_invite, new Response.Listener<String>() {
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

                    } else if(error) {
                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        share.putExtra(Intent.EXTRA_SUBJECT, "Smart Basket");
                        share.putExtra(Intent.EXTRA_TEXT, "I want to add you to my shopping list. download SmartBasket and send me your email address");
                        startActivity(Intent.createChooser(share, "Share link!"));
                        dialog.hide();
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
                params.put("list_owner", list_owner);
                params.put("list_invite", list_invite);
                params.put("list_id", list_id);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void update_basket_name(final String id, final String name){

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.update_basket_name, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        String errorMsg = jObj.getString("message");
                        Intent intent = new Intent(getContext(), ContinerActivity.class);
                        startActivity(intent);
                        Toast.makeText(getContext(), "List name updated", Toast.LENGTH_SHORT).show();
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
                params.put("id", id);
                params.put("name", name);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

}