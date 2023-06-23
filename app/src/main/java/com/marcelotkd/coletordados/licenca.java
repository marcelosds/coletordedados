package com.marcelotkd.coletordados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class licenca extends AppCompatActivity {

    Button btnSite2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenca);

        setTitle("Coletor de Dados Patrimoniais");

        btnSite2 = findViewById(R.id.btnSite2);


        //ABRE SITE DA VERSÃO PAGA
        btnSite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://play.google.com/store/apps/details?id=com.marcelotkd.coletordados";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }
}