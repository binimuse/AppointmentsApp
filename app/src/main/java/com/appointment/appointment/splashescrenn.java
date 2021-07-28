package com.appointment.appointment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import java.util.Locale;

public class splashescrenn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashescrenn);
        loadLocale();

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over

                Intent i = new Intent(splashescrenn.this, MainActivity.class);
                overridePendingTransition(R.anim.nothing, R.anim.slide_out);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
    private void loadLocale() {

        String langPref = "USER_LANGUAGE";
        SharedPreferences prefs = getSharedPreferences("sharedPrefs",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }


    public void changeLang(String lang) {
        Context context = getBaseContext();

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = getApplicationContext().getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
    }
}