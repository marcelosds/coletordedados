package com.marcelotkd.coletordados;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button btnInventario, btnSite, btnConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

           setTitle("Coletor de Dados Patrimoniais");


            btnInventario = findViewById(R.id.btnInventario);
            btnSite = findViewById(R.id.btnSite);
            btnConfig = findViewById(R.id.btnConfig);

            //ABRE A TELA DE INVENTÁRIO
            btnInventario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(it);
                }
            });

            //ABRE A TELA DA CONFIGURAÇÃO
            btnConfig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it1 = new Intent(MainActivity.this, MainActivity4.class);
                    startActivity(it1);
                }
            });


            //ABRE SITE DA EMPRESA
            btnSite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://www.linkedin.com/in/marcelo-souza-dos-santos-0a158733/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

    }


        // Código botão Voltar
        public boolean onKeyDown ( int keyCode, KeyEvent event){

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                checkExit();
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }

        private void checkExit () {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Deseja realmente sair?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                        }
                    })
                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

}
