package com.marcelotkd.coletordados;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.marcelotkd.coletordados.DAO.DAO_BENS;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {


    Button btnScan, btnLista, btnLimpa, btnConsulta, btnGrava;
    EditText txtResultado, txtPlaca, txtDescricao, txtData;
    TextView txtLocalizacao, txtEstado, txtSituacao, txtCDlocal, txtCDestado, txtCDsituacao;
    Spinner spConservacao, spSituacao, spLocalizacao;
    CheckBox chkAlterar, chkLocal, chkEstado, chkSituacao;
    ArrayList<String> listItem;
    ArrayAdapter adapter;
    ListView lvItens;
    DAO_BENS db;

    String[] appPermissoes = {Manifest.permission.CAMERA};

    public static final int CODIGO_PERMISSOES_REQUERIDAS = 1;

    private String local;
    private String[] id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setTitle("Coleta");


        db = new DAO_BENS(this);

        listItem = new ArrayList<>();

        btnScan = findViewById(R.id.btnScan);
        btnConsulta = findViewById(R.id.btnConsulta);
        btnLimpa = findViewById(R.id.btnLimpa);
        btnGrava = findViewById(R.id.btnGrava);
        btnLista = findViewById(R.id.btnLista);
        txtResultado = findViewById(R.id.txtResultado);
        txtPlaca = findViewById(R.id.txtPlaca);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtLocalizacao = findViewById(R.id.txtLocalizacao);
        txtEstado = findViewById(R.id.txtEstado);
        txtSituacao = findViewById(R.id.txtSituacao);
        txtData = findViewById(R.id.txtData);
        txtCDlocal = findViewById(R.id.txtCDlocal);
        txtCDestado = findViewById(R.id.txtCDestado);
        txtCDsituacao = findViewById(R.id.txtCDsituacao);
        final Activity activity = this;
        spLocalizacao = findViewById(R.id.spLocalizacao);
        spConservacao = findViewById(R.id.spConservacao);
        spSituacao = findViewById(R.id.spSituacao);
        chkAlterar = findViewById(R.id.chkAlterar);
        lvItens = findViewById(R.id.lvItens);
        chkLocal = findViewById(R.id.chkLocal);
        chkEstado = findViewById(R.id.chkEstado);
        chkSituacao = findViewById(R.id.chkSituacao);



        //CARREGA ARRAYS
        loadSpinnerLocais();
        loadSpinnerEstado();
        loadSpinnerSituacao();


        //EXIBE A LISTA COM TODOS OS BENS DO INVENTÁRIO
        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(it);

            }
        });



        //LIMPA TODOS OS DADOS DA TELA
        btnLimpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGrava.setEnabled(false);
                txtResultado.setText("");
                txtPlaca.setText("");
                txtDescricao.setText("");
                txtLocalizacao.setText("");
                txtEstado.setText("");
                txtSituacao.setText("");
                txtData.setText("");
                listItem = new ArrayList<>();
                listItem.clear();
                lvItens.setAdapter(null);
                chkAlterar.setChecked(false);
                chkAlterar.setVisibility(View.INVISIBLE);
                chkLocal.setVisibility(View.INVISIBLE);
                chkEstado.setVisibility(View.INVISIBLE);
                chkSituacao.setVisibility(View.INVISIBLE);
                spLocalizacao.setVisibility(View.INVISIBLE);
                spConservacao.setVisibility(View.INVISIBLE);
                spSituacao.setVisibility(View.INVISIBLE);
                spLocalizacao.setSelection(0);
                spConservacao.setSelection(0);
                spSituacao.setSelection(0);



            }
        });



        //BUSCA NÚMERO DA PLACA DIGITADO
        btnConsulta.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                if (txtResultado.getText().toString().equals("")) {
                    Toast.makeText(MainActivity2.this, "Informe o número da Placa!", Toast.LENGTH_SHORT).show();


                } else {

                    try {

                        viewDados();

                        String placa = (lvItens.getItemAtPosition(0).toString());
                        String descricao = lvItens.getItemAtPosition(1).toString();
                        String localizacao = lvItens.getItemAtPosition(2).toString();
                        String estado = lvItens.getItemAtPosition(3).toString();
                        String situacao = lvItens.getItemAtPosition(4).toString();
                        String data = lvItens.getItemAtPosition(5).toString();
                        String cd_local = lvItens.getItemAtPosition(6).toString();
                        String cd_estado = lvItens.getItemAtPosition(7).toString();
                        String cd_situcao = lvItens.getItemAtPosition(8).toString();

                        if (txtResultado.getText().toString().equals(placa)) {
                            txtPlaca.setText(placa);
                            txtDescricao.setText(descricao);
                            txtLocalizacao.setText(localizacao);
                            txtEstado.setText(estado);
                            txtSituacao.setText(situacao);
                            txtData.setText(data);
                            txtCDlocal.setText(cd_local);
                            txtCDestado.setText(cd_estado);
                            txtCDsituacao.setText(cd_situcao);

                            btnGrava.setEnabled(true);

                            InputMethodManager imm = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(txtResultado.getWindowToken(), 0);

                            listItem.clear();
                            lvItens.setAdapter(null);
                            txtResultado.setText("");
                            chkAlterar.setVisibility(view.VISIBLE);




                        } else {

                            Toast.makeText(MainActivity2.this, "Bem Patrimonial não localizado!", Toast.LENGTH_SHORT).show();

                        }
                    }
                    catch (Exception e){

                    }

                }
            }

        });


        txtResultado.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (txtResultado.getText().toString().equals("")) {
                        Toast.makeText(MainActivity2.this, "Informe o número da Placa!", Toast.LENGTH_SHORT).show();


                    } else {

                        try {

                            viewDados();

                            String placa = (lvItens.getItemAtPosition(0).toString());
                            String descricao = lvItens.getItemAtPosition(1).toString();
                            String localizacao = lvItens.getItemAtPosition(2).toString();
                            String estado = lvItens.getItemAtPosition(3).toString();
                            String situacao = lvItens.getItemAtPosition(4).toString();
                            String data = lvItens.getItemAtPosition(5).toString();
                            String cd_local = lvItens.getItemAtPosition(6).toString();
                            String cd_estado = lvItens.getItemAtPosition(7).toString();
                            String cd_situcao = lvItens.getItemAtPosition(8).toString();

                            if (txtResultado.getText().toString().equals(placa)) {
                                txtPlaca.setText(placa);
                                txtDescricao.setText(descricao);
                                txtLocalizacao.setText(localizacao);
                                txtEstado.setText(estado);
                                txtSituacao.setText(situacao);
                                txtData.setText(data);
                                txtCDlocal.setText(cd_local);
                                txtCDestado.setText(cd_estado);
                                txtCDsituacao.setText(cd_situcao);

                                btnGrava.setEnabled(true);

                                InputMethodManager imm = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(txtResultado.getWindowToken(), 0);

                                listItem.clear();
                                lvItens.setAdapter(null);
                                txtResultado.setText("");
                                chkAlterar.setVisibility(view.VISIBLE);




                            } else {

                                Toast.makeText(MainActivity2.this, "Bem Patrimonial não localizado!", Toast.LENGTH_SHORT).show();

                            }
                        }
                        catch (Exception e){

                        }

                    }
                    return true;
                }
                return false;
            }
        });


        //GRAVA AS ALTERAÇÕES NO BEM PATRIMONIAL
        btnGrava.setOnClickListener(new View.OnClickListener() {

            @Override
         public void onClick(View view) {

            String idLocal = String.valueOf(((SpinnerObject) spLocalizacao.getSelectedItem()).getId());
            String idEstado = String.valueOf(Integer.parseInt(((SpinnerObject) spConservacao.getSelectedItem()).getId()));
            String idSituacao = String.valueOf(Integer.parseInt(((SpinnerObject) spSituacao.getSelectedItem()).getId()));

             String getCODIGO = (txtPlaca.getText().toString());
             String data_atual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

             if (chkAlterar.isChecked()) {
             if (chkLocal.isChecked() == true && (chkEstado.isChecked() == false && chkSituacao.isChecked() == false)) {
                 db.updateDados(getCODIGO, txtDescricao.getText().toString(), idLocal,  spLocalizacao.getSelectedItem().toString(), txtCDestado.getText().toString(), txtEstado.getText().toString(), txtCDsituacao.getText().toString(), txtSituacao.getText().toString(), data_atual);

             }
             else if (chkEstado.isChecked() == true && chkLocal.isChecked() == false && chkSituacao.isChecked() == false) {
                 db.updateDados(getCODIGO, txtDescricao.getText().toString(), txtCDlocal.getText().toString(), txtLocalizacao.getText().toString(), idEstado, spConservacao.getSelectedItem().toString(), txtCDsituacao.getText().toString(), txtSituacao.getText().toString(), data_atual);

             }
             else if (chkSituacao.isChecked() == true && chkLocal.isChecked() == false && chkEstado.isChecked() ==false) {
                 db.updateDados(getCODIGO, txtDescricao.getText().toString(), txtCDlocal.getText().toString(), txtLocalizacao.getText().toString(), txtCDestado.getText().toString(), txtEstado.getText().toString(), idSituacao, spSituacao.getSelectedItem().toString(), data_atual);

             }
             else if (chkLocal.isChecked() == true && chkEstado.isChecked() == true && chkSituacao.isChecked() == false) {
                 db.updateDados(getCODIGO, txtDescricao.getText().toString(), idLocal, spLocalizacao.getSelectedItem().toString(), idEstado, spConservacao.getSelectedItem().toString(), txtCDsituacao.getText().toString(), txtSituacao.getText().toString(), data_atual);

             }
             else if (chkLocal.isChecked() == true && chkEstado.isChecked() == false && chkSituacao.isChecked() == true) {
                 db.updateDados(getCODIGO, txtDescricao.getText().toString(), idLocal, spLocalizacao.getSelectedItem().toString(), txtCDestado.getText().toString(), txtEstado.getText().toString(), idSituacao, spSituacao.getSelectedItem().toString(), data_atual);

             }
             else if (chkLocal.isChecked() == false && chkEstado.isChecked() == true && chkSituacao.isChecked() == true) {
                 db.updateDados(getCODIGO, txtDescricao.getText().toString(), txtCDlocal.getText().toString(), txtLocalizacao.getText().toString(), idEstado, spConservacao.getSelectedItem().toString(), idSituacao, spSituacao.getSelectedItem().toString(), data_atual);
             }

             else if (chkLocal.isChecked() == true && chkEstado.isChecked() == true && chkSituacao.isChecked() == true) {
                 db.updateDados(getCODIGO, txtDescricao.getText().toString(), idLocal, spLocalizacao.getSelectedItem().toString(), idEstado, spConservacao.getSelectedItem().toString(), idSituacao, spSituacao.getSelectedItem().toString(), data_atual);

             } else {

             }

         }
             if (chkLocal.isChecked() == false && (chkEstado.isChecked() == false && chkSituacao.isChecked() == false)) {
                 db.updateDados(getCODIGO, txtDescricao.getText().toString(),txtCDlocal.getText().toString(), txtLocalizacao.getText().toString(), txtCDestado.getText().toString(), txtEstado.getText().toString(), txtCDsituacao.getText().toString(), txtSituacao.getText().toString(), data_atual);
             } else {

             }
                 Toast.makeText(MainActivity2.this, "Bem Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();


                lvItens.setAdapter(null);
                listItem.clear();
                btnGrava.setEnabled(false);
                txtPlaca.setText("");
                txtDescricao.setText("");
                txtLocalizacao.setText("");
                txtEstado.setText("");
                txtSituacao.setText("");
                txtData.setText("");
                chkAlterar.setChecked(false);
                chkAlterar.setVisibility(View.INVISIBLE);
                chkLocal.setVisibility(View.INVISIBLE);
                chkEstado.setVisibility(View.INVISIBLE);
                chkSituacao.setVisibility(View.INVISIBLE);
                spLocalizacao.setVisibility(View.INVISIBLE);
                spConservacao.setVisibility(View.INVISIBLE);
                spSituacao.setVisibility(View.INVISIBLE);


            }
        });

        //ABRE A CÂMERA PARA LER CÓDIGO DE BARRAS

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verificarPermissoes();
                int REQUEST_CODE = 0;
                ActivityCompat.requestPermissions(MainActivity2.this, new String[]
                        {Manifest.permission.CAMERA}, REQUEST_CODE);
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Leitor - Placa Patrimonial");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();

            }
        });



        //CHECBOX QUE HABILITA OS MENUS NOVOS
        chkAlterar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (chkAlterar.isChecked()) {
                    chkLocal.setVisibility(v.VISIBLE);
                    chkLocal.setChecked(false);
                    chkEstado.setVisibility(v.VISIBLE);
                    chkEstado.setChecked(false);
                    chkSituacao.setVisibility(v.VISIBLE);
                    chkSituacao.setChecked(false);
                    spLocalizacao.setSelection(0);
                    spConservacao.setSelection(0);
                    spSituacao.setSelection(0);

                }
                else{
                    chkLocal.setVisibility(v.INVISIBLE);
                    chkEstado.setVisibility(v.INVISIBLE);
                    chkSituacao.setVisibility(v.INVISIBLE);
                    spLocalizacao.setVisibility(v.INVISIBLE);
                    spConservacao.setVisibility(v.INVISIBLE);
                    spSituacao.setVisibility(v.INVISIBLE);


                }

            }
        });

        chkLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkLocal.isChecked()){
                    spLocalizacao.setVisibility(view.VISIBLE);
                }else{
                    spLocalizacao.setVisibility(view.INVISIBLE);
                }
            }
        });

        chkEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkEstado.isChecked()){
                    spConservacao.setVisibility(view.VISIBLE);
                }else{
                    spConservacao.setVisibility(view.INVISIBLE);
                }
            }
        });

        chkSituacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkSituacao.isChecked()){
                    spSituacao.setVisibility(view.VISIBLE);
                }else{
                    spSituacao.setVisibility(view.INVISIBLE);
                }
            }
        });

    }


    //MOSTRA OS DADOS DA TABELA BENS A LISTVIEW
    private void viewDados() {
        Cursor cursor = db.viewDados(txtResultado.getText().toString());

        if (cursor.getCount() == 0){
            Toast.makeText(this, "Bem Patrimonial não localizado!", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                listItem.add(cursor.getString(1)); //indice 1 é codigo
                listItem.add(cursor.getString(2)); //indice 2 é descrição
                listItem.add(cursor.getString(3)); //indice 3 é localização
                listItem.add(cursor.getString(4)); //indice 4 é estado
                listItem.add(cursor.getString(5)); //indice 5 é situação
                listItem.add(cursor.getString(6)); //indice 6 é data
                listItem.add(cursor.getString(7)); //indice 7 é cd_local
                listItem.add(cursor.getString(8)); //indice 8 é cd_estado
                listItem.add(cursor.getString(9)); //indice 9 é cd_situacao

            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            lvItens.setAdapter(adapter);
        }
    }


    //CARREGA O RESULTADO APÓS LEITURA DO CÓDIGO DE BARRAS DO BEM
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){
            if (result.getContents() != null) {
                txtResultado.setText(result.getContents());
                String x = txtResultado.getText().toString();
                String temp = "";
                for (int i = 0; i < x.length(); i++) {
                    if (!x.substring(i, i + 1).equals("0")) { //checa se chegou no primeiro caracter q não é 0
                        temp += x.substring(i, x.length()); // temp fica com os valores correspondentes à substring da posição atual ate o fim
                        break; // sai do laço
                    }
                }
                txtResultado.setText(temp);


                try {

                viewDados();

                String text = lvItens.getItemAtPosition(0).toString();
                String text1 = lvItens.getItemAtPosition(1).toString();
                String localizacao = lvItens.getItemAtPosition(2).toString();
                String estado = lvItens.getItemAtPosition(3).toString();
                String situacao = lvItens.getItemAtPosition(4).toString();
                String data_atual = lvItens.getItemAtPosition(5).toString();

                if(txtResultado.getText().toString().equals(text)) {
                    txtPlaca.setText(text);
                    txtDescricao.setText(text1);
                    txtLocalizacao.setText(localizacao);
                    txtEstado.setText(estado);
                    txtSituacao.setText(situacao);
                    txtData.setText(data_atual);

                    btnGrava.setEnabled(true);

                    listItem.clear();
                    lvItens.setAdapter(null);
                    txtResultado.setText("");
                    chkAlterar.setVisibility(View.VISIBLE);

                }else{
                    Toast.makeText(MainActivity2.this, "Bem Patrimonial não localizado!", Toast.LENGTH_SHORT).show();


                }
                }
                catch (Exception e){
                    txtResultado.setText("");

                }
            } else {
                alert("Leitura Cancelada");
            }
        } else {

            super.onActivityResult(requestCode, resultCode, data);


        }

    }
    private void alert (String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    private void loadSpinnerLocais() {
        // database handler
        DAO_BENS db = new DAO_BENS(getApplicationContext());
        List<SpinnerObject> lables = db.getAllLabels();
        // Criando adaptador para o spinner
        ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(this, android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Anexando adaptador de dados para o spinner
        spLocalizacao.setAdapter(dataAdapter);
    }

    private void loadSpinnerEstado() {
        // database handler
        DAO_BENS db = new DAO_BENS(getApplicationContext());
        List<SpinnerObject> lables = db.getAllLabels1();
        // Criando adaptador para o spinner
        ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(this, android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Anexando adaptador de dados para o spinner
        spConservacao.setAdapter(dataAdapter);
    }

    private void loadSpinnerSituacao() {
        // database handler
        DAO_BENS db = new DAO_BENS(getApplicationContext());
        List<SpinnerObject> lables = db.getAllLabels2();
        // Criando adaptador para o spinner
        ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(this, android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Anexando adaptador de dados para o spinner
        spSituacao.setAdapter(dataAdapter);
    }

    /*public boolean verificarPermissoes () {
        List<String> permissoesRequeridas = new ArrayList<>();

        for (String permissao : appPermissoes) {

            if (ContextCompat.checkSelfPermission(this, permissao) != PackageManager.PERMISSION_GRANTED) {
                permissoesRequeridas.add(permissao);

            }
        }

        if (!permissoesRequeridas.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissoesRequeridas.toArray(new String[permissoesRequeridas.size()]), CODIGO_PERMISSOES_REQUERIDAS);
            return false;
        }

        return true;

    }*/


}
