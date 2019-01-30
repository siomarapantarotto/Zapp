package br.com.siomara.zapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.siomara.zapp.R;
import br.com.siomara.zapp.config.FirebaseConfiguration;
import br.com.siomara.zapp.model.User;
import br.com.siomara.zapp.util.Base64Custom;
import br.com.siomara.zapp.util.Preference;


public class LoginEmailActivity extends AppCompatActivity {

    private EditText    edtEmail;
    private EditText    edtPassword;
    private Button      btnLogin;
    private User        user;
    private FirebaseAuth authenticator;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUser;
    private String loggedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("METHOD", "Entering onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        verifyIfUserIsLogged();

        edtEmail    = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin    = findViewById(R.id.btnLogin);

        // Espera o usuario pressionar o botao para fazer verificacao de usuário
        // Caso o usuario já esteja logado isso nao é desejado
        // Precisa criar um metodo para verificar se o usuario já nao está logado.
        // Se já estiver logado, direcionar usuário para tela principal
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = new User();
                user.setEmail( edtEmail.getText().toString() );
                user.setPassword( edtPassword.getText().toString() );
                validateLogin();
            }
        });
        Log.i("METHOD", "Leaving onCreate()");
    }

    // If user is already logged there is no need for new authentication
    // Send user to the main activity that allows conversation.
    private void verifyIfUserIsLogged() {
        Log.i("METHOD", "Entering verifyIfUserIsLogged()");
        authenticator = FirebaseConfiguration.getFirebaseAuth();
        if ( authenticator.getCurrentUser() != null ) {
            Log.i("AUTHENTICATOR", "Usuário já está logado!");
            openMainActivity();
        }
        Log.i("METHOD", "Leaving verifyIfUserIsLogged()");
    }


    // Method for login  - best place to save logged user
    private void validateLogin()
    {
        Log.i("METHOD", "Entering validateLogin()");
        authenticator = FirebaseConfiguration.getFirebaseAuth();
        authenticator.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //Preference preference = new Preference(LoginEmailActivity.this);
                    loggedUserId = Base64Custom.encode(user.getEmail());

                    // Recureparar nome do usuario recem cadastrado
                    firebase = FirebaseConfiguration.getFirebaseIntance()
                            .child("users")
                            .child(loggedUserId);

                    valueEventListenerUser = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            User retrievedUser = dataSnapshot.getValue(User.class);

                            Preference preference = new Preference(LoginEmailActivity.this);
                            preference.saveData(loggedUserId, retrievedUser.getName());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    firebase.addListenerForSingleValueEvent( valueEventListenerUser);

                    //preference.saveData(loggedUserId, "");

                    openMainActivity();
                    Toast.makeText(LoginEmailActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginEmailActivity.this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.i("METHOD", "Leaving validateLogin()");
    }

    // Directs to main activity that allows the conversation
    private void openMainActivity() {
        Log.i("METHOD", "Entering openMainActivity()");
        Intent intent = new Intent(LoginEmailActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // encerra essa login
        Log.i("METHOD", "Leaving openMainActivity()");
    }


    // This method sends user to create an account before he can login
    // The screen message has the property onclick configured for that.
    public void userFirstRegistration(View view)
    {
        Log.i("METHOD", "Entering userFirstRegistration()");
        Intent intent = new Intent(LoginEmailActivity.this, UserRegistrationActivity.class);
        startActivity(intent);
        Log.i("METHOD", "Leaving userFirstRegistration()");
    }

}
