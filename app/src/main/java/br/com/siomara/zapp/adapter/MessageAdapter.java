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

import br.com.siomara.zapp.R;
import br.com.siomara.zapp.model.Message;
import br.com.siomara.zapp.util.Preference;

/**
 * Created by 80114369 on 19/07/2018.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    private Context context;
    private ArrayList<Message> messages;

    public MessageAdapter(@NonNull Context c, @NonNull ArrayList<Message> objects) {
        super(c, 0, objects);
        this.context = c;
        this.messages = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (messages != null) {

            // Recupera dados do usuario remetente
            Preference preference =  new Preference(context);
            String idUsuarioRemetente = preference.getIdentification();

            // inicializa objeto para montagem de layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Recupera mensagem
            Message message = messages.get( position );

            // Monta view a partir do xml
            if (idUsuarioRemetente.equals(message.getUserIDSender())) {
                view = inflater.inflate(R.layout.message_going_out_on_right, parent, false);
            } else {
                view = inflater.inflate(R.layout.message_coming_in_on_left, parent, false);
            }

            // Recupera elemento para exibicao
            TextView txtMessage = view.findViewById(R.id.tv_mensagem);
            txtMessage.setText( message.getMessageDetail());
        }

        return  view;
    }
}
