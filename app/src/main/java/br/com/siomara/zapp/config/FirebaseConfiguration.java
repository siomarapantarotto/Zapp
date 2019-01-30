package br.com.siomara.zapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by siomara.com.br on 05/04/2018.
 */

public final class FirebaseConfiguration {

    private static DatabaseReference firebaseReference;
    private static FirebaseAuth firebaseAuth;

    public static DatabaseReference getFirebaseIntance() {
        {
            if (firebaseReference == null) {
                firebaseReference = FirebaseDatabase.getInstance().getReference();
            }
            return firebaseReference;
        }
    }

    public static FirebaseAuth getFirebaseAuth() {
        if ( firebaseAuth == null) {
           firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

}
