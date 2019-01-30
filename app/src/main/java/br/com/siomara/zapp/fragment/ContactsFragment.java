package br.com.siomara.zapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import br.com.siomara.zapp.adapter.ContactAdapter;
import br.com.siomara.zapp.config.FirebaseConfiguration;
import br.com.siomara.zapp.model.Contact;
import br.com.siomara.zapp.util.Preference;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    //private ArrayList<String> contacts;
    private ArrayList<Contact> contacts;
    private DatabaseReference dbrFirebase;
    private ValueEventListener valueEventListenerContacts;

    public ContactsFragment() {
        // Required empty public constructor
    }

    // O onStart é chamado umpouco antes do fragment ser carregado
    @Override
    public void onStart() {
        super.onStart();
        dbrFirebase.addValueEventListener(valueEventListenerContacts);
        Log.i("ValueEventListener", "**************** Here onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        dbrFirebase.removeEventListener(valueEventListenerContacts);
        Log.i("ValueEventListener", "**************** Here OnStop");
    }

    // Faz a montagem da view que será exibida no fragment, que está
    // posicionado no Slidding tabs, que por sua vez está no main activity
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Instancia objeto
        contacts = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        // Monta listview e adapter
        listView = view.findViewById(R.id.lv_contacts);
//        arrayAdapter = new ArrayAdapter(
//                getActivity(),
//                R.layout.layout_list_contact,
//                contacts
//        );
        // Adapter customizado com nome e email
        arrayAdapter = new ContactAdapter(getActivity(), contacts);

        listView.setAdapter( arrayAdapter );

        // Retrieve contacts from Firebase
        Preference preference = new Preference(getActivity()); // Siomara's class
        String loggedUserID = preference.getIdentification();
        dbrFirebase = FirebaseConfiguration.getFirebaseIntance()
                            .child("contacts")
                            .child(loggedUserID);

        // Listener to retrieve contacts
        valueEventListenerContacts = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Clear contact list
                contacts.clear();

                // List contacts
                for ( DataSnapshot data: dataSnapshot.getChildren() ) {
                    Contact contact = data.getValue( Contact.class);
                    //contacts.add(contact.getName());
                    contacts.add( contact );
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ChatActivity.class);

                // Recupera dados do contato selecionado para serem passados
                Contact contact = contacts.get(position);

                // Envia dados do contato selecionado para chat activity
                intent.putExtra("name", contact.getName());
                intent.putExtra("email", contact.getEmail());

                startActivity(intent);
            }
        });

        return view;
    }

}
