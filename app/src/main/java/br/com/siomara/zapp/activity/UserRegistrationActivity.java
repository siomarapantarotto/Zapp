package br.com.siomara.zapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import br.com.siomara.zapp.R;
import br.com.siomara.zapp.config.FirebaseConfiguration;
import br.com.siomara.zapp.model.User;
import br.com.siomara.zapp.util.Base64Custom;
import br.com.siomara.zapp.util.Preference;

public class UserRegistrationActivity extends AppCompatActivity {

    private EditText    edtName;
    private EditText    edtEmail;
    private EditText    edtPassword;
    private Button      btnRegister;
    private User        user;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        edtName     = findViewById(R.id.edtName);
        edtEmail    = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User();
                user.setName(edtName.getText().toString());
                user.setEmail(edtEmail.getText().toString());
                user.setPassword(edtPassword.getText().toString());
                registerNewUser(user);
            }
        });
    }

    // Method to register a new user into Firebase database;
    private void registerNewUser(final User user)
    {
        firebaseAuth = FirebaseConfiguration.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(UserRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(UserRegistrationActivity.this, "Sucesso ao cadstrar o usuário!", Toast.LENGTH_LONG).show();
                    // Forma 1
                    // FirebaseUser firebaseUser = task.getResult().getUser().getUid();
                    // Forma 2
                    // FirebaseUser firebaseUser = task.getResult().getUser();
                    // user.setId(firebaseUser.getUid());

                    String idOnBase64 = Base64Custom.encode(user.getEmail());
                    user.setId(idOnBase64);
                    user.save();

                    // Salva dados do novo usuario em preferencia local do aparelho
                    Preference preference = new Preference(UserRegistrationActivity.this);
                    preference.saveData(idOnBase64, user.getName());

                    // Para salvar os dados não podemos estar deslogados
                    //firebaseAuth.signOut();
                    //finish(); // Fecha janela de cadastramento e deixa usuario na de login
                    openUserLogin();

                } else {

                    String exceptionError = "";

                    try {

                        throw task.getException();

                    } catch ( FirebaseAuthWeakPasswordException e ) {
                        exceptionError = "Digite uma senha mais forte, contendo mais caracteres, com letras e números.";
                    } catch ( FirebaseAuthInvalidCredentialsException e ) {
                        exceptionError = "O email informado é inválido, digite-o corretamente.";
                    } catch ( FirebaseAuthUserCollisionException e ) {
                        exceptionError = "O email informado já pertence a outro usuário. Digite um outro para se registrar.";
                    } catch ( FirebaseAuthInvalidUserException e ) {
                        exceptionError = "Account is disabled or email address does not correspond to an existing user";
                    } catch ( Exception e ) {
                        exceptionError = "Houve um erro genérico ou desconhecido ao cadastra usuário.";
                        e.printStackTrace();
                    }

                    Toast.makeText(UserRegistrationActivity.this, "ERRO: " + exceptionError, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void openUserLogin() {
        Intent intent = new Intent(UserRegistrationActivity.this, LoginEmailActivity.class);
        startActivity(intent);
        finish();
    }
}
