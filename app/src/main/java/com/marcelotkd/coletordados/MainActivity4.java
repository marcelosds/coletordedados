package com.marcelotkd.coletordados;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.marcelotkd.coletordados.DAO.DAO_BENS;
import com.marcelotkd.coletordados.OBJETOS.Bem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Base64;

public class MainActivity4 extends AppCompatActivity {


    Button btnImportar, btnExporta;
    CheckBox chk_main4_1, chk_main4_2;
    ArrayList<String> listItem;
    ArrayAdapter adapter;
    ListView lvItens;
    DAO_BENS db;
    ActivityResultLauncher<Intent> activityResultLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        setTitle("Configurações");

        db = new DAO_BENS(this);

        listItem = new ArrayList<>();



        btnImportar = findViewById(R.id.btnImportar);
        btnExporta = findViewById(R.id.btnExporta);
        chk_main4_1 = findViewById(R.id.chk_main4_1);
        chk_main4_2 = findViewById(R.id.chk_main4_2);


        chk_main4_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    if (chk_main4_1.isChecked()) {
                        btnImportar.setEnabled(true);
                    } else {
                        btnImportar.setEnabled(false);
                    }
                } else {
                    requestPermissions();
                    btnImportar.setEnabled(true);

                }
                }
        });

        chk_main4_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if (checkPermission()) {
                if (chk_main4_2.isChecked()) {
                    btnExporta.setEnabled(true);
                } else {
                    btnExporta.setEnabled(false);
                }
            } else {
                requestPermissions();
                btnExporta.setEnabled(true);

            }
            }
        });

        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
              if(result.getResultCode()== Activity.RESULT_OK) {
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                      if (Environment.isExternalStorageManager()) {
                          Toast.makeText(getApplicationContext(), "Permissão Liberada!", Toast.LENGTH_SHORT).show();
                      } else {
                          Toast.makeText(getApplicationContext(), "Permissão Negada!", Toast.LENGTH_SHORT).show();
                      }
                  }
              }
            }
        });



        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importDB();

                //EXCLUIR O ARQUIVO BENS.DB APÓS IMPORTAÇÃO
                String dir=Environment.getExternalStorageDirectory() + "/Download";
                File sd = new File(dir);
                String currentDBPath = "bens.db";
                File localexcl = new File(sd, currentDBPath);

                boolean p = new File(String.valueOf(localexcl)).delete();

            }
        });


        btnExporta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDB();


            }
        });



    }


    boolean checkPermission() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            return Environment.isExternalStorageManager();
        }
        else
        {
            int readcheck=ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE);
            int writecheck=ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
            return  readcheck == PackageManager.PERMISSION_GRANTED && writecheck == PackageManager.PERMISSION_GRANTED;
        }
    }

    void requestPermissions() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R)
        {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",new Object[]{getApplicationContext().getPackageName()})));
                activityResultLauncher.launch(intent);

            } catch (Exception e)
            {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activityResultLauncher.launch(intent);
            }

        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity4.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},0);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 0:
                if(grantResults.length>0)
                {
                    boolean readper=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean writeper=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(readper && writeper)
                    {
                        Toast.makeText(getApplicationContext(), "Permissão Liberada!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Permissão Negada!", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(), "Permissão Negada!", Toast.LENGTH_SHORT).show();
                }
        }

    }

    private void importDB(){

        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Toast.makeText(this, "Não foi possível acessar a unidade do dispositivo.", Toast.LENGTH_LONG).show();
        }
        String dir=Environment.getExternalStorageDirectory() + "/Download";
        File sd = new File(dir);
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String backupDBPath = "/data/com.marcelotkd.coletordados/databases/bens.db";
        String currentDBPath = "bens.db";
        File currentDB = new File(sd, currentDBPath);
        File backupDB = new File(data, backupDBPath);

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Dados importados com Sucesso!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Arquivo inexistente ou corrompido!", Toast.LENGTH_LONG).show();
        }
    }


    private void exportDB(){


        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Toast.makeText(this, "Não foi possível acessar a unidade do dispositivo.", Toast.LENGTH_LONG).show();
        }
        String DatabaseName = "bens.db";
        String dir=Environment.getExternalStorageDirectory() + "/Download";
        File sd = new File(dir);
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "com.marcelotkd.coletordados" +"/databases/"+DatabaseName ;
        String backupDBPath = "bens.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Resultado Exportado com Sucesso!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Resultado não foi Exportado!", Toast.LENGTH_LONG).show();
        }
    }





}