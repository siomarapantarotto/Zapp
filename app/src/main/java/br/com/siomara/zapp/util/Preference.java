package br.com.siomara.zapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by siomara.com.br on 19/03/2018.
 */

public class Preference {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final int       MODE = 0; // não compartilhado
    private final String    FILE_NAME = "zapp_preferences";
    private final String    KEY_IDENTIFICATION = "loggedUserId";
    private final String    KEY_NAME = "loggedUserName";


    public Preference(Context contextParameters) {
        this.context = contextParameters;
        this.sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE);
        editor = sharedPreferences.edit();
    }


    public void saveData (String userIdentification, String userName) {
        editor.putString(KEY_IDENTIFICATION, userIdentification);
        editor.putString(KEY_NAME, userName);
        editor.commit();
    }

    public String getIdentification () {
        return sharedPreferences.getString(KEY_IDENTIFICATION, null);
    }

    public String getName () {
        return sharedPreferences.getString(KEY_NAME, null);
    }

}
