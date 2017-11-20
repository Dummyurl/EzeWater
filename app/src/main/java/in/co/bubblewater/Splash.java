package in.co.bubblewater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import io.codetail.animation.SupportAnimator;

public class Splash extends AppCompatActivity {
    public static String device_id;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    int LoginId;
    boolean hidden = true;
    RelativeLayout mRevealView;
    SupportAnimator mAnimator;
    boolean mPressed = false;
    float pixelDensity;
    ImageView imgicon;
    TextView txtname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Internet Connection Error");
            alertDialog.setMessage("Please connect to working Internet connection");
            alertDialog.setIcon(R.mipmap.launchicon);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Splash.this.finish();
                }
            });
            alertDialog.show();
            return;
        }
//        imgicon = (ImageView) findViewById(R.id.imgicon);
//        txtname = (TextView) findViewById(R.id.txtname);
//        pixelDensity = getResources().getDisplayMetrics().density;
//        mRevealView = (RelativeLayout) findViewById(R.id.reveal_items);
//        mRevealView.setVisibility(View.INVISIBLE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                LoginId = sharedpreferences.getInt("LoginId", 0);
//                if (!mPressed) {
//                    mPressed = true;
//                    int x = mRevealView.getRight();
//                    int y = mRevealView.getBottom();
//                    x -= ((28 * pixelDensity) + (16 * pixelDensity));
//
//                    int hypotenuse = (int) Math.hypot(mRevealView.getWidth(), mRevealView.getHeight());
//
//
//                    mAnimator = ViewAnimationUtils.createCircularReveal(mRevealView, x, y, 0, hypotenuse);
//                    mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//                    mAnimator.setDuration(800);
//
//
//                    if (hidden) {
//                        mRevealView.setVisibility(View.VISIBLE);
//                        mAnimator.start();
//                        hidden = false;
//
//                    }
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            ImageView imageView = (ImageView) findViewById(R.id.imgicon);
//
//                            Animation animZoomin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
//
//                            imageView.startAnimation(animZoomin);
//                            imageView.setVisibility(View.VISIBLE);
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    txtname.setVisibility(View.VISIBLE);
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (LoginId == 0) {
//                                                Intent in = new Intent(Splash.this, LoginActivity.class);
//                                                startActivity(in);
//                                                overridePendingTransition(R.anim.enter, R.anim.exit);
//                                            } else {
//                                                Intent in = new Intent(Splash.this, MainActivity.class);
//                                                startActivity(in);
//                                                overridePendingTransition(R.anim.enter, R.anim.exit);
//                                            }
//                                            Splash.this.finish();
//                                        }
//                                    }, 4000);
//                                }
//                            }, 4000);
//
//                        }
//                    }, 3000);
//                }
//            }
//        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                LoginId = sharedpreferences.getInt("LoginId", 0);
                if (LoginId == 0) {
                    Intent in = new Intent(Splash.this, LoginActivity.class);
                    startActivity(in);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else {
                    Intent in = new Intent(Splash.this, MainActivity.class);
                    startActivity(in);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
                Splash.this.finish();
            }
        }, 2000);

    }

}

