package com.appointment.appointment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appointment.appointment.adapter.CustomListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private WebView myWebView;
    // ProgressBar progressBar;
    private static MainActivity mInstance;
    ProgressDialog progressDialog;
    AppUpdateManager appUpdateManager;
    int RequestUpdate = 1;
    ImageView img_five;
    public  static TextView data;
    public  static TextView data2;
    public  static TextView data3;
    public  static CardView card;

    private RequestQueue mQueue;
    String dataParsed = "";
    String singleParsed = "";
    String singleParsed2 = "";
    String singleParsed3 = "";
    public static final String TAG = AppController.class.getSimpleName();

    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;

    private static final String target_url = "http://192.168.1.114:8000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

      //  setContentView(R.layout.activity_main);



        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        mQueue = Volley.newRequestQueue(this);
        jsonParse();

        SharedPreferences sharedPreferencess
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
       // final Vibrator vibe = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

       // final SharedPreferences.Editor editorr= sharedPreferencess.edit();


            getWindow().requestFeature(Window.FEATURE_PROGRESS);
            setContentView(R.layout.activity_main);
            mInstance = this;

        listView = (ListView) findViewById(R.id.list);

        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);
            appUpdateManager = AppUpdateManagerFactory.create(this);
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo result) {
                    if ((result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                            && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    result,
                                    AppUpdateType.IMMEDIATE,
                                    MainActivity.this,
                                    RequestUpdate);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });



        nav = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setTitle(Html.fromHtml("<font color=\"#00695C\">" + getString(R.string.app_name) + "</font>"));
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#EAEAEA"));
        actionBar.setBackgroundDrawable(colorDrawable);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cm.getActiveNetworkInfo();
        if (nf != null && nf.isConnected()) {


        } else {

            ///   checkConnection();
            SweetAlertDialog pDialogg = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);

            pDialogg.setTitleText(R.string.Nocon);

            pDialogg.setConfirmText(new String(getString(R.string.ok)));
            pDialogg.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);

                    intent.setComponent(new ComponentName("com.android.settings",
                            "com.android.settings.Settings$DataUsageSummaryActivity"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    pDialogg.dismiss();
                    //checkConnection();
                }
            })
                    .setCancelButton((new String(getString(R.string.can))), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }
                    })
                    .show();

        }


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {


                    case R.id.setting:



                        Intent intent = new Intent(getApplicationContext(), setting.class);
                        overridePendingTransition(0, 0);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);

                        return true;

                    case R.id.nav_about:

                        Intent intenti = new Intent(getApplicationContext(), about.class);
                        overridePendingTransition(0, 0);
                        intenti.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intenti);

                        return true;


                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Search:

                        Intent intentit = new Intent(getApplicationContext(), search.class);
                        overridePendingTransition(0, 0);
                        intentit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentit);


                        break;


                    case R.id.Video:


                        Intent intentitt = new Intent(getApplicationContext(), uploadVideo.class);
                        overridePendingTransition(0, 0);
                        intentitt.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentitt);

                        break;

                }
                return true;
            }
        });



        // 3 birr

        ImageView img_three = (ImageView) findViewById(R.id.img_three);
        img_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","3" );
                startActivity(c);
            }
        });
        ImageView img_three2 = (ImageView) findViewById(R.id.img_three2);
        img_three2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","3" );
                startActivity(c);
            }
        });
        TextView text_three = (TextView) findViewById(R.id.text_three);
        text_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","3" );
                startActivity(c);
            }
        });
        TextView text_three2 = (TextView) findViewById(R.id.text_three2);
        text_three2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","3" );
                startActivity(c);
            }
        });

        // 5 birr


        ImageView img_five = (ImageView) findViewById(R.id.img_five1);
        img_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","5" );
                startActivity(c);
            }
        });
        ImageView img_five2 = (ImageView) findViewById(R.id.img_five2);
        img_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","5" );
                startActivity(c);
            }
        });
        TextView text_five = (TextView) findViewById(R.id.text_five);
        text_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","5" );
                startActivity(c);
            }
        });
        TextView text_five2 = (TextView) findViewById(R.id.text_five2);
        text_five2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","5" );
                startActivity(c);
            }
        });


        // 10  birr
        ImageView img_ten = (ImageView) findViewById(R.id.img_ten);
        img_ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","10" );
                startActivity(c);
            }
        });
        ImageView img_ten2 = (ImageView) findViewById(R.id.img_ten2);
        img_ten2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","10" );
                startActivity(c);
            }
        });
        TextView text_tex = (TextView) findViewById(R.id.text_ten);
        text_tex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","10" );
                startActivity(c);
            }
        });
        TextView text_tex2 = (TextView) findViewById(R.id.text_ten2);
        text_tex2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","10" );
                startActivity(c);
            }
        });



        // 20  birr
        ImageView img_twenty = (ImageView) findViewById(R.id.img_twenty);
        img_twenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","20" );
                startActivity(c);
            }
        });
        ImageView img_twenty2 = (ImageView) findViewById(R.id.img_twenty2);
        img_twenty2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","20" );
                startActivity(c);
            }
        });
        TextView text_twenty = (TextView) findViewById(R.id.text_twenty);
        text_twenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","20" );
                startActivity(c);
            }
        });
        TextView text_twenty2 = (TextView) findViewById(R.id.text_twenty2);
        text_twenty2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tekst=810;
                Uri tlf = Uri.parse("smsto:"+tekst);
                Intent c = new Intent(Intent.ACTION_VIEW, tlf);
                c.setData(tlf);
                c.putExtra("sms_body","20" );
                startActivity(c);
            }
        });








        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonParse();


                ObjectAnimator.ofFloat(v, "rotation", 6f, 360f).start();




            }
        });





    }


    private void jsonParse() {

        String url = "http://192.168.8.100:90/api/get-winners";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                hidePDialog();
                Log.d(TAG, response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int n = 0; n < jsonArray.length(); n++)
                    {


                        JSONObject object = jsonArray.getJSONObject(n);
                                JSONObject winner = (JSONObject) object.get("winner");






                        Movie movie = new Movie();

                        movie.setTitle(object.getString("full_name"));

                       movie.setRating((winner.getString("lottery_number")));
                      movie.setYear((winner.getString("phone_number")));




                        movieList.add(movie);


                    }

                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
        movieList.clear();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    public static synchronized MainActivity getInstance() {
        return mInstance;
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:

                        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);

                        pDialog.setTitleText(R.string.sure);
                        pDialog.setContentText(new String(getString(R.string.con)));
                        pDialog.setConfirmText(new String(getString(R.string.ok)));
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finishAffinity();
                                finish();
                            }
                        })
                                .setCancelButton((new String(getString(R.string.can))), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();

                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //   startVoiceRecognitionActivity();
        MainActivity.getInstance().setConnectivityListener(this);

    }

}

