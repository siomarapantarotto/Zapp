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
import br.com.siomara.zapp.model.Chat;

/**
 * Created by 80114369 on 25/07/2018.
 */

public class ChatAdapter extends ArrayAdapter<Chat> {

    private ArrayList<Chat> chats;
    private Context context;


    public ChatAdapter(@NonNull Context c, @NonNull ArrayList<Chat> objects) {
        super(c, 0, objects);
        this.context = c;
        this.chats = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        // Verifica se a lista está preenchida
        if (chats != null) {

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // monta view a partir do xml
            view = inflater.inflate(R.layout.layout_list_chats, parent, false);

            // recupera elemento para exibição
            TextView name = view.findViewById(R.id.tv_title);
            TextView lastMessage = view.findViewById(R.id.tv_subtitle);

            Chat chat = chats.get(position);
            name.setText( chat.getName());
            lastMessage.setText( chat.getMessage() );

        }

        return view;
    }
}
