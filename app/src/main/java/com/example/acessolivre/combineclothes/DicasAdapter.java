package com.example.acessolivre.combineclothes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Projeto on 04/10/2017.
 */

public class DicasAdapter extends ArrayAdapter<Dicas> {

    private final Context context;
    private final ArrayList<Dicas> elementos;

    public DicasAdapter(Context context, ArrayList<Dicas> elementos){
        super (context,R.layout.list_dicas,elementos);
        this.context = context;
        this.elementos = elementos;
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE  );

        View rowView = inflater.inflate( R.layout.list_dicas,parent,false);

        TextView estilo = ( TextView )rowView.findViewById(R.id.estilo);
        ImageView imagem = ( ImageView) rowView.findViewById(R.id.imagem);

        estilo.setText ( elementos.get(position).getEstilo());
        imagem.setImageResource( elementos.get(position).getImagem());

        return rowView;

    }
}
