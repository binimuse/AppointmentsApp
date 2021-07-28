package com.appointment.appointment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.PopupMenu;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public class setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loadLocale();
        super.onCreate(savedInstanceState);
       // overridePendingTransition(R.anim.nothing, R.anim.slide_in);
        setContentView(R.layout.activity_setting);



        SharedPreferences sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);























        findViewById(R.id.lang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context contextWrapper = new ContextThemeWrapper(setting.this, R.style.PopupStyle);
                PopupMenu popup = new PopupMenu(contextWrapper,v);


                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.getMenuInflater().inflate(R.menu.pop_up, popup.getMenu());


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.English:

                                LanguageManager.setNewLocale(setting.this, LanguageManager.LANGUAGE_KEY_ENGLISH);
                                reLaunchAppEn();
                                reLaunchApp();
                                finish();
                                return true;

                            case R.id.Amharic:

                                LanguageManager.setNewLocale(setting.this, LanguageManager.LANGUAGE_KEY_AMHARIC);
                                reLaunchAppAm();
                                reLaunchApp();
                                finish();
                                return true;
                            case R.id.Oromiffa:
                                //  setlocal("om");
//                                //Toast.makeText(getApplicationContext(),"English",Toast.LENGTH_SHORT).show();

                                return true;
                            case R.id.Tigregha:
                                //  setlocal("ti");
                                //   recreate();
                                //Toast.makeText(getApplicationContext(),"English",Toast.LENGTH_SHORT).show();

                                return true;
                            default:
                                break;


                        }
                        popup.dismiss();
                        return true;
                    }
                });

                popup.show();
            }


        });
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
    private void reLaunchAppAm() {
        SharedPreferences shp = getSharedPreferences(
                "sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putString("USER_LANGUAGE", LanguageManager.LANGUAGE_KEY_AMHARIC);
        editor.commit();
        editor.apply();



    }

    private void reLaunchAppEn() {
        SharedPreferences shp = getSharedPreferences(
                "sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putString("USER_LANGUAGE", LanguageManager.LANGUAGE_KEY_ENGLISH);
        editor.commit();
        editor.apply();
    }

    private void reLaunchApp() {





        Intent mStartActivity = new Intent(setting.this, MainActivity.class);
        recreate();
        // finish();

        startActivity(mStartActivity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }
}