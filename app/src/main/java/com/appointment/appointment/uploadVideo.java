package com.appointment.appointment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class uploadVideo extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle3;
    int SELECT_VIDEO_REQUEST = 1;
    String selectedVideoPath;
    Button submit;
    VideoView videoView;
    MediaController mc;
    EditText fullname,phone,bank,w_ph,w_acc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setTitle(Html.fromHtml("<font color=\"#00695C\">" + getString(R.string.app_name) + "</font>"));
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#EAEAEA"));
        actionBar.setBackgroundDrawable(colorDrawable);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout3);
        toggle3 = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle3);
        toggle3.syncState();
















        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation3);


        bottomNavigationView.setSelectedItemId(R.id.Video);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intentityt = new Intent(getApplicationContext(), MainActivity.class);
                        overridePendingTransition(0, 0);
                        intentityt.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentityt);
                        break;


                    case R.id.Search:
                        Intent intentityit = new Intent(getApplicationContext(), search.class);
                        overridePendingTransition(0, 0);
                        intentityit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentityit);
                        break;

                }
                return true;
            }
        });


        FloatingActionButton upload = (FloatingActionButton) findViewById(R.id.upload);

         fullname = (EditText) findViewById(R.id.fullname);
        fullname = (EditText) findViewById(R.id.phone);
         bank = (EditText) findViewById(R.id.bank);
         w_ph = (EditText) findViewById(R.id.w_phone);
         w_acc = (EditText) findViewById(R.id.w_acc);




        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean fieldsOK = validate(new EditText[] { fullname, fullname, bank,w_ph,w_acc });
                if (fieldsOK==true)
                {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("video/*");

                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Video"), SELECT_VIDEO_REQUEST);
                }
                else {
                    SweetAlertDialog pDialogg = new SweetAlertDialog(uploadVideo.this, SweetAlertDialog.ERROR_TYPE);

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


//    private void postDataUsingVolley(String name, String job) {
//        // url to post our data
//        String url = "https://reqres.in/api/users";
//
//
//        // creating a new variable for our request queue
//        RequestQueue queue = Volley.newRequestQueue(uploadVideo.this);
//
//        // on below line we are calling a string
//        // request method to post the data to our API
//        // in this we are calling a post method.
//        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                // inside on response method we are
//                // hiding our progress bar
//                // and setting data to edit text as empty
//
//                // on below line we are displaying a success toast message.
//                Toast.makeText(uploadVideo.this, "Data added to API", Toast.LENGTH_SHORT).show();
//                try {
//                    // on below line we are passing our response
//                    // to json object to extract data from it.
//                    JSONObject respObj = new JSONObject(response);
//
//                    // below are the strings which we
//                    // extract from our json object.
//                    String name = respObj.getString("name");
//                    String job = respObj.getString("job");
//
//                    // on below line we are setting this string s to our text view.
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // method to handle errors.
//                Toast.makeText(uploadVideo.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // below line we are creating a map for
//                // storing our values in key and value pair.
//                Map<String, String> params = new HashMap<String, String>();
//
//                // on below line we are passing our key
//                // and value pair to our parameters.
//                params.put("name", name);
//                params.put("job", job);
//
//                // at last we are
//                // returning our params.
//                return params;
//            }
//        };
//        // below line is to make
//        // a json object request.
//        queue.add(request);
//    }

    private boolean validate(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                return false;
            }
        }
        return true;
    }

    private static Context context;
    private Uri mVideoURI;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Uri selectedVideoUri = data.getData();

            mVideoURI = Uri.parse(String.valueOf(selectedVideoUri));

            String selectedVideoPath = getPath(selectedVideoUri, uploadVideo.this);
            if (selectedVideoPath != null) {

                VideoView videoView =(VideoView)findViewById(R.id.videoView1);
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoURI(selectedVideoUri);
                videoView.start();
                MediaController mediaController= new MediaController(this);
                mediaController.setAnchorView(videoView);

            }
            else {
                Toast.makeText(this, "invalid Path", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public String getPath(Uri uri, Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if(isKitKat){
            filePath = generateFromKitkat(uri,context);
        }

        if(filePath != null){
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }

    @TargetApi(19)
    private String generateFromKitkat(Uri uri,Context context){
        String filePath = null;
        if(DocumentsContract.isDocumentUri(context, uri)){
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = { MediaStore.Audio.Media.DATA };
            String sel = MediaStore.Audio.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{ id }, null);



            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }

}