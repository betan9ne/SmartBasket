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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

import apps.betan9ne.smartbasket.AddReceiptActivity;
import apps.betan9ne.smartbasket.CreateList;
import apps.betan9ne.smartbasket.LoginActivity;
import apps.betan9ne.smartbasket.R;
import apps.betan9ne.smartbasket.ViewReceiptActivity;
import helper.AppController;
import helper.SQLiteHandler;
import helper.SessionManagera;

public class FullScreenDialog extends DialogFragment {
    public static String TAG = "FullScreenDialog";
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ImageView iv;
    private TextView add_receipt, view_receipt, logout;
    private SQLiteHandler db;
    String u_id;
    GoogleSignInClient googleSignInClient;
    private SessionManagera session;
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
        session = new SessionManagera(getContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        db = new SQLiteHandler(getContext());
        HashMap<String, String> user = db.getUserDetails(invite_listFragment.class.getSimpleName());
        u_id = user.get("u_id");

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
