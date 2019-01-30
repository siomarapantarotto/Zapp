package br.com.siomara.zapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.widget.Toolbar; // Dá erro com esse import
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.siomara.zapp.R;
import br.com.siomara.zapp.adapter.MessageAdapter;
import br.com.siomara.zapp.config.FirebaseConfiguration;
import br.com.siomara.zapp.model.Chat;
import br.com.siomara.zapp.model.Message;
import br.com.siomara.zapp.util.Base64Custom;
import br.com.siomara.zapp.util.Preference;

public class ChatActivity extends AppCompatActivity {

    private Toolbar     toolbarChats;
    private EditText    edtMessage;
    private ImageButton btnSend;
    private ListView    listChats;
    //private ArrayList<String> messages;
    private ArrayList<Message> messages;
    //private ArrayAdapter adapter;
    private ArrayAdapter<Message> adapter;
    private ValueEventListener valueEventListenerChats;

    private DatabaseReference firebase;

    // Dados do destinatário
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;

    // Dados do remetente
    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbarChats    = findViewById(R.id.toolbarChatsID);
        edtMessage      = findViewById(R.id.edtMessageID);
        btnSend         = findViewById(R.id.btnSendID);
        listChats       = findViewById(R.id.listChatsID);

        // Recupera dados do remetente que é o usuário logado
        Preference preference = new Preference(ChatActivity.this);
        idUsuarioRemetente = preference.getIdentification();
        nomeUsuarioRemetente = preference.getName();

        // Usado para passar dados de um ponto ao outro
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            nomeUsuarioDestinatario     = extra.getString("name");
            String emailDestinatario    = extra.getString("email");
            idUsuarioDestinatario       = Base64Custom.encode(emailDestinatario);
        }

        // Configure toolbar
        toolbarChats.setTitle(nomeUsuarioDestinatario);
        toolbarChats.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbarChats);


        // Monta lista de chats
        messages = new ArrayList<>();

        //adapter = new ArrayAdapter(
        //        ChatActivity.this,
        //        android.R.layout.simple_list_item_1,
        //        messages
        //);
        adapter = new MessageAdapter(ChatActivity.this, messages);
        listChats.setAdapter(adapter);


        // Recuperar messages from firebase
        firebase = FirebaseConfiguration.getFirebaseIntance()
                    .child( "messages" )
                    .child( idUsuarioRemetente )
                    .child( idUsuarioDestinatario );

        valueEventListenerChats = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                messages.clear();

                // recupera mensagens;
                for(DataSnapshot dados: dataSnapshot.getChildren()) {
                    Message message = dados.getValue(Message.class);
                    //messages.add(message.getMessageDetail());
                    messages.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListenerChats);


        // Enviar mensagem
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtMessage = edtMessage.getText().toString();
                if (txtMessage.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Digite uma mensagem para enviar", Toast.LENGTH_SHORT).show();
                } else {
                    Message message = new Message();
                    message.setUserIDSender(idUsuarioRemetente);
                    message.setMessageDetail(txtMessage);

                    // Save message for Remetente
                    Boolean retornoMensagemRemetente = saveMessage(idUsuarioRemetente, idUsuarioDestinatario, message);
                    if (!retornoMensagemRemetente) {
                        Toast.makeText(ChatActivity.this,
                                "Sorry, error while saving the message, try it again!!!",
                                 Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        // Save message for Destinatario
                        Boolean retornoMensagemDestinatario =saveMessage(idUsuarioDestinatario, idUsuarioRemetente, message);
                        if (!retornoMensagemDestinatario) {
                            Toast.makeText(ChatActivity.this,
                                    "Sorry, error while sendin message to Destinatario, try it again!!!",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    // Save chat for remetente
                    Chat chat = new Chat();
                    chat.setUserId(idUsuarioDestinatario);
                    chat.setName(nomeUsuarioDestinatario);
                    chat.setMessage(txtMessage);
                    Boolean retornoChatRemetente = saveChat(idUsuarioRemetente, idUsuarioDestinatario, chat);
                    if (!retornoChatRemetente) {
                        Toast.makeText(
                                ChatActivity.this,
                                "Error saving Chat, Try again!",
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        // Save chat for destinatario
                        chat = new Chat();
                        chat.setUserId(idUsuarioRemetente);
                        chat.setName(nomeUsuarioRemetente);
                        chat.setMessage(txtMessage);

                        Boolean retornoChatDestinatario = saveChat(idUsuarioDestinatario, idUsuarioRemetente, chat);
                        if (!retornoChatDestinatario) {
                            Toast.makeText(
                                    ChatActivity.this,
                                    "Error saving chat for destinatário, try later!",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    }

                    // Clear message field
                    edtMessage.setText("");
                }
            }
        });

    }

    private boolean saveMessage(String remetenteID, String destinatarioID, Message message) {
        try {
            // Cria novo nó "messages", além de users and contacts
            firebase = FirebaseConfiguration.getFirebaseIntance().child("messages");

            firebase.child(remetenteID)
                    .child(destinatarioID)
                    .push()
                    .setValue( message );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveChat( String remetenteID, String destinatarioID, Chat chat) {
        try {
            firebase = FirebaseConfiguration.getFirebaseIntance().child("chats");
            firebase.child(remetenteID)
                    .child(destinatarioID)
                    .setValue(chat);
            return true;
        } catch ( Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerChats);
    }

}
