package br.com.siomara.zapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by siomara.com.br on 19/03/2018.
 */

public class PreferenceForToken {

    private Context context;
    private SharedPreferences sharedPreferences;
    private String FILE_NAME = "zapp_preferences";
    private int MODE = 0; // não compartilhado
    private SharedPreferences.Editor editor;
    private String KEY_USERNAME = "username";
    private String KEY_TELEPHONE = "telephone";
    private String KEY_VALIDATION_CODE = "validation_code";


    public PreferenceForToken(Context contextParameters) {
        this.context = contextParameters;
        this.sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE);
        editor = sharedPreferences.edit();
    }

    public void saveUserPreferences (String username, String telephone, String validationCode) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_TELEPHONE, telephone);
        editor.putString(KEY_VALIDATION_CODE, validationCode);
        editor.commit();
    }

    public HashMap<String, String> getUserPreferences () {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_USERNAME, sharedPreferences.getString(KEY_USERNAME, null));
        userData.put(KEY_TELEPHONE, sharedPreferences.getString(KEY_TELEPHONE, null));
        userData.put(KEY_VALIDATION_CODE, sharedPreferences.getString(KEY_VALIDATION_CODE, null));
        return userData;
    }
}
