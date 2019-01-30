package br.com.siomara.zapp.util;

import android.util.Base64;

/**
 * Created by 80114369 on 18/04/2018.
 */

public class Base64Custom {

    public static String encode(String s) {
        return Base64.encodeToString(s.getBytes(),
               Base64.DEFAULT).replaceAll("\\n|\\r", "");
    }

    public static String decode(String s) {
        return new String( Base64.decode(s, Base64.DEFAULT) );
    }
}
