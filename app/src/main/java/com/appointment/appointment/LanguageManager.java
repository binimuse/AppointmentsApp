package com.appointment.appointment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageManager {

    /**
     * For english locale
     */
    public static final String LANGUAGE_KEY_ENGLISH = "en";

    public static final String LANGUAGE_KEY_AMHARIC = "am";
    public static final String LANGUAGE_KEY_Oromiffa = "om";
    public static final String LANGUAGE_KEY_Tigirighna = "ti";
    public static final String LANGUAGE_KEY_Afar = "aa";
    public static final String LANGUAGE_KEY_Somalia = "so";


    /**
     *  SharedPreferences Key
     */
    private static final String LANGUAGE_KEY = "language_key";

    /**
     * set current pref locale
     * @param mContext
     * @return
     */
    public static Context setLocale(Context mContext) {
        return updateResources(mContext, getLanguagePref(mContext));
    }

    /**
     * Set new Locale with context
     * @param mContext
     * @param mLocaleKey
     * @return
     */
    public static Context setNewLocale(Context mContext, String mLocaleKey) {
        setLanguagePref(mContext, mLocaleKey);
        return updateResources(mContext, mLocaleKey);
    }

    /**
     * Get saved Locale from SharedPreferences
     * @param mContext current context
     * @return current locale key by default return english locale
     */
    public static String getLanguagePref(Context mContext) {
        SharedPreferences mPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(mContext);


        return mPreferences.getString(LANGUAGE_KEY, "");
    }

    /**
     *  set pref key
      * @param mContext
     * @param localeKey
     */
    private static void setLanguagePref(Context mContext, String localeKey) {
        SharedPreferences mPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
        mPreferences.edit().putString(LANGUAGE_KEY, localeKey).commit();
    }

    /**
     * update resource
     * @param context
     * @param language
     * @return
     */
    public static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = new Configuration(res.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context = context.createConfigurationContext(config);
        }

        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
        return context;


    }

    /**
     * get current locale
     * @param res
     * @return
     */
    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
    }
}
