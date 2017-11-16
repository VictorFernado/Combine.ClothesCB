package com.example.acessolivre.combineclothes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TelaDicas extends AppCompatActivity {
/*
    Teste
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_dicas);

        ListView listaDicas = (ListView)findViewById(R.id.ListViewDicas);
        ArrayAdapter arrayAdapter = new DicasAdapter(this, preencherArrayDicas());
        listaDicas.setAdapter(arrayAdapter);
        listaDicas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),"Estilo:"+arrayDicas.get(position).toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<Dicas> preencherArrayDicas(){
        ArrayList<Dicas> dados = new ArrayList<Dicas>();
        Dicas d = new Dicas("Casual",R.drawable.casual);
        dados.add(d);
        d = new Dicas("Formal",R.drawable.formal);
        dados.add(d);
        d = new Dicas("Clássico",R.drawable.classico);
        dados.add(d);
        d = new Dicas("Rocker",R.drawable.allstar_rock);
        dados.add(d);
        d = new Dicas("Activewear",R.drawable.esportivo);
        dados.add(d);

        return dados;
    }
}
