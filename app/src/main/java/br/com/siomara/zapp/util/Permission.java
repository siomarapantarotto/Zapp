package br.com.siomara.zapp.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siomara.com.br on 21/03/2018.
 */

public class Permission {

    public static boolean validatePermission( int requestCode, Activity activity, String[] permissions ) {

        if (Build.VERSION.SDK_INT > 23) {

            List<String> permissionList = new ArrayList<String>();

            for(String permission : permissions) {
                Boolean acceptedPermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if ( !acceptedPermission ) {
                    permissionList.add(permission);
                }
            }

            if (permissionList.isEmpty()) {
                return true;
            }

            String[] newPermissions = new String[permissionList.size()];
            permissionList.toArray(newPermissions);

            ActivityCompat.requestPermissions(activity, newPermissions, requestCode );
        }
        return true;
    }
}
