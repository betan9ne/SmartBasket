package apps.betan9ne.smartbasket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import helper.SessionManagera;

public class SplashActivity extends AppCompatActivity {

    private SessionManagera session;
    private static final int RC_SIGN_IN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManagera(getApplicationContext());

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }
                catch(InterruptedException ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    if (session.isLoggedIn()) {
                        Intent asd = new Intent(SplashActivity.this, ContinerActivity.class);
                        startActivity(asd);
                        finish();
                    }
                    else
                    {
                        finishAndRemoveTask ();
                        Intent ads = new Intent(SplashActivity.this, LoginActivity.class);
                        ads.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ads);
                        finish();
                    }

                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
