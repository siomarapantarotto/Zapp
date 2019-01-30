package br.com.siomara.zapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.siomara.zapp.R;
import br.com.siomara.zapp.activity.ChatActivity;
import br.com.siomara.zapp.adapter.ChatAdapter;
import br.com.siomara.zapp.config.FirebaseConfiguration;
import br.com.siomara.zapp.model.Chat;
import br.com.siomara.zapp.util.Base64Custom;
import br.com.siomara.zapp.util.Preference;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Chat> adapter;
    private ArrayList<Chat> chats;

    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerChats;


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        // Monta listview e adapter
        chats = new ArrayList<>();
        listView = view.findViewById(R.id.lv_chats);
        adapter = new ChatAdapter(getActivity(), chats);
        listView.setAdapter(adapter);

        // recuperar dados do usuario
        Preference preference = new Preference(getActivity());
        String loggedUserId = preference.getIdentification(); // remetente

        // recuperar chats do firebase
        firebase = FirebaseConfiguration.getFirebaseIntance()
                .child("chats")
                .child(loggedUserId); // Remetente

        valueEventListenerChats = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                    Chat chat = dados.getValue( Chat.class) ;
                    chats.add(chat);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // adicionar evento de clique na lista de chats
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chat chat = chats.get(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                intent.putExtra("name", chat.getName());
                String email= Base64Custom.decode(chat.getUserId());
                intent.putExtra("email", email);

                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerChats);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerChats);
    }
}
