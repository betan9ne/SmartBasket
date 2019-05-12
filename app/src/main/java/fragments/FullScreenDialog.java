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
import java.util.UUID;

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
    private TextView darkMode, logout, email;
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
        logout= v.findViewById(R.id.logout);
        email= v.findViewById(R.id.email);
         darkMode= v.findViewById(R.id.darkMode);

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
    logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                logoutUser();
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
