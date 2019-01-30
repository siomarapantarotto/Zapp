package br.com.siomara.zapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.siomara.zapp.R;
import br.com.siomara.zapp.adapter.TabAdapter;
import br.com.siomara.zapp.config.FirebaseConfiguration;
import br.com.siomara.zapp.model.Contact;
import br.com.siomara.zapp.model.User;
import br.com.siomara.zapp.util.Base64Custom;
import br.com.siomara.zapp.util.Preference;
import br.com.siomara.zapp.util.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth        authenticator;
    private DatabaseReference   dbFirebase;
    private Toolbar             toolbar;
    private SlidingTabLayout    slidingTabLayout;
    private ViewPager           viewPager;
    private String              contactIdentification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticator = FirebaseConfiguration.getFirebaseAuth();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Zapp");
        setSupportActionBar(toolbar);

        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.vp_page);

        // Distribui tabs pela tela
        // Bug - indicador da tab selecionada fica pouco acima da margem
        slidingTabLayout.setDistributeEvenly(true); // tirando esse comando o indicador volta para posição correta

        // Troca a cor da barrinha indicadora de tab selecionada
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        // O getSupportFragmentManager() retorna objeto que gerencia os fragmentos
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // Define actions for menu and toolbar items
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.menuItemLogout:
                logoutUser();
                return true;
            case R.id.menuItemSearch:
                return true;
            case R.id.menuItemAddContact:
                addNewContact();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }


    private void addNewContact() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Configuirações do dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("Informe e-mail do contato");
        alertDialog.setCancelable(false);

        final EditText edtContactEmail = new EditText(MainActivity.this);
        alertDialog.setView(edtContactEmail);


        // configura botões

        alertDialog.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String contactEmail = edtContactEmail.getText().toString();

                if (contactEmail.isEmpty()) {

                    Toast.makeText(MainActivity.this, "Preencha o email do contato.", Toast.LENGTH_SHORT).show();

                } else {

                    // Verificar se o novo contato já está previamente cadastrado no Zapp
                    contactIdentification = Base64Custom.encode(contactEmail);
                    // Primeira referência para verificar se contato existe no db
                    dbFirebase = FirebaseConfiguration.getFirebaseIntance().child("users").child(contactIdentification);

                    // Consulta se novo contato tem cadastro no app uma unica vez
                    dbFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // caso tenha o usuario no db ele vem no dataSnapshot
                            if  (dataSnapshot.getValue() != null) {

                                // Recuperar dados do conmtato a ser adicionado
                                User userContact = dataSnapshot.getValue(User.class);

                                // Recuperar identificador usuario logado (base64)
                                Preference preference = new Preference(MainActivity.this);
                                String loggedUserIdentification = preference.getIdentification();

                                //authenticator.getCurrentUser().getEmail();

                                // Segunda referência para salvar os dados
                                dbFirebase = FirebaseConfiguration.getFirebaseIntance();
                                dbFirebase = dbFirebase.child("contacts")
                                                       .child(loggedUserIdentification)
                                                       .child(contactIdentification);

                                Contact contact = new Contact();
                                contact.setId(contactIdentification);
                                contact.setEmail(userContact.getEmail());
                                contact.setName(userContact.getName());

                                dbFirebase.setValue(contact);

/*
                                + contatos
                                    + jamilton@gmail.com (que está logado)
                                        + joserenato@gmail.com
                                            + email
                                            + nome
                                        + leticia@gmail.com

*/


                            } else {
                                Toast.makeText(MainActivity.this, "Esse contato ainda não se cadastrou como usuário do Zapp", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void logoutUser() {
        authenticator.signOut();
        Intent intent = new Intent (MainActivity.this, LoginEmailActivity.class);
        startActivity(intent);
        finish(); // Close the current window
    }

}
