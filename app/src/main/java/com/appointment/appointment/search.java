package com.appointment.appointment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class search  extends AppCompatActivity  {

    private ProgressBar progress_bar;
    private FloatingActionButton fab;
    private EditText et_search;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initComponent();
        SweetAlertDialog pDialogg = new SweetAlertDialog(search.this, SweetAlertDialog.ERROR_TYPE);

        pDialogg.setTitleText(R.string.Nocon);

      //  nav = (NavigationView) findViewById(R.id.nav_view2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setTitle(Html.fromHtml("<font color=\"#00695C\">" + getString(R.string.app_name) + "</font>"));
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#EAEAEA"));
        actionBar.setBackgroundDrawable(colorDrawable);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);

        toggle2 = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle2);
        toggle2.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation2);

//        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                switch (item.getItemId()) {
//
//
//                    case R.id.setting:
//                        Intent i = new Intent(getApplicationContext(), setting.class);
//                        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
//                        startActivity(i);
//
//                        return true;
//
//                    case R.id.nav_about:
//                        Intent iii = new Intent(getApplicationContext(), about.class);
//                        startActivity(iii);
//
//                        return true;
//
//
//                }
//                return true;
//            }
//        });
        bottomNavigationView.setSelectedItemId(R.id.Search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:

                        Intent intentitt = new Intent(getApplicationContext(), MainActivity.class);
                        overridePendingTransition(0, 0);
                        intentitt.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentitt);


                        break;


                    case R.id.Video:


                        Intent intentityt = new Intent(getApplicationContext(), uploadVideo.class);
                        overridePendingTransition(0, 0);
                        intentityt.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentityt);
                        break;

                }
                return true;
            }
        });





    }



    private void initComponent() {
        et_search = (EditText) findViewById(R.id.et_search);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progress_bar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN );
        fab = (FloatingActionButton) findViewById(R.id.fab);

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    searchAction();
                    return true;
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                boolean fieldsOK = validate(new EditText[] { et_search });
                if(fieldsOK==true)
                {
                    searchAction();
                }
                else {
                    SweetAlertDialog pDialogg = new SweetAlertDialog(search.this, SweetAlertDialog.ERROR_TYPE);

                    pDialogg.setTitleText("please complete the form");

                    pDialogg.setConfirmText(new String(getString(R.string.ok)));
                    pDialogg.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

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



            }
        });

    }
    private boolean validate(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                return false;
            }
        }
        return true;
    }

    private void searchAction() {
        progress_bar.setVisibility(View.VISIBLE);
        fab.setAlpha(0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String content = et_search.getText().toString();
                progress_bar.setVisibility(View.GONE);
                fab.setAlpha(1f);

                    searchdata(content);

            }
        }, 1000);
    }


    private void searchdata(String content) {

         String postUrl = "http://192.168.8.100:90/api/check-result/"+content;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String mes;
        StringRequest request = new StringRequest(Request.Method.GET, postUrl, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonobj = new JSONObject(response);
                    if (jsonobj.getString("message").equals("congrats you have won"))
                    {

                        SweetAlertDialog pDialogg = new SweetAlertDialog(search.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);

                        pDialogg.setTitleText(jsonobj.getString("message"));
                        pDialogg.setCustomImage(R.drawable.lotto);
                        pDialogg.setConfirmText(new String(getString(R.string.ok)));
                        pDialogg.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.dismissWithAnimation();
                                //checkConnection();
                            }
                        })

                                .show();

                    }
                    else {

                        SweetAlertDialog pDialogg2 = new SweetAlertDialog(search.this, SweetAlertDialog.WARNING_TYPE);

                        pDialogg2.setTitleText(jsonobj.getString("message"));
                        pDialogg2.setCustomImage(R.drawable.lotto);
                        pDialogg2.setCancelButton((new String(getString(R.string.can))), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                            }
                        })
                                .show();

                    }




                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jh", "Error: " + error
                        + "\nStatus Code " + error.networkResponse.statusCode
                        + "\nResponse Data " + error.networkResponse.data
                        + "\nCause " + error.getCause()
                        + "\nmessage" + error.getMessage());
                if (error.networkResponse.statusCode==400)
                {
                    SweetAlertDialog pDialogg2 = new SweetAlertDialog(search.this, SweetAlertDialog.WARNING_TYPE);

                    pDialogg2.setTitleText("you didn't won");
                    pDialogg2.setCustomImage(R.drawable.lotto);
                    pDialogg2.setCancelButton((new String(getString(R.string.can))), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }
                    })
                            .show();

                }
                else if(error.networkResponse.statusCode==500)
                {

                    SweetAlertDialog pDialogg2 = new SweetAlertDialog(search.this, SweetAlertDialog.WARNING_TYPE);

                    pDialogg2.setTitleText("Player with this number doesn't exist");
                    pDialogg2.setCustomImage(R.drawable.lotto);
                    pDialogg2.setCancelButton((new String(getString(R.string.can))), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }
                    })
                            .show();



            }
                else{
                    SweetAlertDialog pDialogg2 = new SweetAlertDialog(search.this, SweetAlertDialog.WARNING_TYPE);

                    pDialogg2.setTitleText("Server error");
                    pDialogg2.setCustomImage(R.drawable.lotto);
                    pDialogg2.setCancelButton((new String(getString(R.string.can))), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }
                    })
                            .show();

                }
            }
        }) ;
        requestQueue.add(request);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}