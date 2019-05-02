package apps.betan9ne.smartbasket;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper.AppConfig;
import helper.AppController;

public class CreateList extends AppCompatActivity {
    ImageView imageView2;
    EditText _name, _price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        imageView2 = findViewById(R.id.imageView2);
        _name= findViewById(R.id.name);
        _price= findViewById(R.id.price);

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = _name.getText().toString();
                String price = _price.getText().toString();
                if (name.trim().length() > 0 && price.trim().length() > 0) {
                    addProduct(name, price);
       Snackbar.make(v, "Great, thanks for the update", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                  } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter some details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }


    private void addProduct(final String name, final String price) {

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.add_Item, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                       String errorMsg = jObj.getString("message");
                         _name.setText("");
                        _price.setText("");
                        _name.requestFocus();
                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),										error.getMessage() + " response error", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("price", price);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

}
