package com.marcelotkd.coletordados;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.marcelotkd.coletordados.DAO.DAO_BENS;


import java.util.ArrayList;



public class MainActivity3 extends AppCompatActivity {

    ListView lvTudo;
    ArrayList listTudo;
    ArrayAdapter adapter;
    TextView txtnrInventario, txtTotal, txtInvent;


    DAO_BENS db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        setTitle("Relação de Bens");

        lvTudo = findViewById(R.id.lvTudo);

        db = new DAO_BENS(this);

        listTudo = new ArrayList<>();

        txtnrInventario = findViewById(R.id.txtnrInventario);

        txtTotal = findViewById(R.id.txtTotal);

        txtInvent = findViewById(R.id.txtInvent);

        viewTudo();

        /* Buscar Total de Bens */
        long total = DatabaseUtils.queryNumEntries(db.getReadableDatabase(), "bens_table");

        String t = String.valueOf(total);

        txtTotal.setText("Total de Bens: "+ t);


        long inventariados = DatabaseUtils.longForQuery(db.getReadableDatabase(),
                "SELECT COUNT(*) FROM bens_table WHERE DATA <> '00/00/0000'", null);

        String i = String.valueOf(inventariados);

        txtInvent.setText("Inventariados: "+ i);
    }


    //MOSTRA OS DADOS DA TABELA BENS A LISTVIEW
    private void viewTudo() {

        Cursor cursor = db.viewTudo();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Bens Patrimoniais não localizados!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                listTudo.add("Placa: " + cursor.getString(1));
                listTudo.add("Descrição: " + cursor.getString(2));
                listTudo.add("Localização: " + cursor.getString(3));
                listTudo.add("Estado: " + cursor.getString(4));
                listTudo.add("Situação: " + cursor.getString(5));
                listTudo.add("Inventariado em: " + cursor.getString(6));
                listTudo.add("");
                listTudo.add("");


                String nr = cursor.getString(cursor.getColumnIndex("CDINVENTARIO"));
                txtnrInventario.setText("Inventário Nº: " + nr);


            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_gallery_item,  listTudo);
            lvTudo.setAdapter(adapter);


        }
    }
}

