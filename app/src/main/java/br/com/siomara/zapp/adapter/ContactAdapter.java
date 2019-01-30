package br.com.siomara.zapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.siomara.zapp.R;
import br.com.siomara.zapp.model.Contact;

/**
 * Created by 80114369 on 26/06/2018.
 */

public class ContactAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contacts;
    private Context context;

    public ContactAdapter(@NonNull Context c, @NonNull ArrayList<Contact> objects) {
        super(c, 0, objects);

        this.contacts = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = null;

        // Check if exist contacts
        if (contacts != null) {
            // inicializar objeto para montagem da view
            // recuperar inflater , que tranforma xml em view
            // SystemService é uma interface global para acessar serviços do sistema
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.layout_list_contact, parent, false);

            // recupera elemento para exibição
            TextView contactName = view.findViewById(R.id.textView_name);
            TextView contactEmail = view.findViewById(R.id.textView_email);
            Contact contact = contacts.get(position);
            contactName.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }

        return view;
    }

}
