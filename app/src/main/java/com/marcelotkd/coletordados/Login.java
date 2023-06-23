package com.marcelotkd.coletordados;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Login extends AppCompatActivity {

    EditText username,password,repassword;
    Button btnSingUp, btnSignIn, carregaImagem;
    DBHelper myDB;


    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private final long ONE_DAY = 24 * 60 * 60 * 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String installDate = preferences.getString("InstallDate", null);
        if(installDate == null) {
            // Primeira execução salva a data atual
            SharedPreferences.Editor editor = preferences.edit();
            Date now = new Date();
            String dateString = formatter.format(now);
            editor.putString("InstallDate", dateString);
            // Executa a edição
            editor.commit();

        }
        else {
            // Não é a primeira execução, então verifique a data
            Date before = null;
            try {
                before = (Date)formatter.parse(installDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date now = new Date();
            long diff = now.getTime() - before.getTime();
            long days = diff / ONE_DAY;
            if(days >= 30) {
                // Mais de 365 diaS?
                Intent intent = new Intent(getApplicationContext(), licenca.class);
                startActivity(intent);

            }
            else
            {
                setContentView(R.layout.activity_login);
                setTitle("Coletor de Dados Patrimoniais");

            }

        }


        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);

        btnSingUp = (Button) findViewById(R.id.btnSingUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);


        myDB = new DBHelper(this);



        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if(user.equals("") || pass.equals("") || repass.equals(""))
                {
                    Toast.makeText(Login.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(pass.equals(repass))
                    {
                       Boolean usercheckResult = myDB.checkusername(user);
                       if(usercheckResult == false)
                       {
                          Boolean regResult = myDB.insertData(user,pass);
                          if(regResult ==true)
                          {
                              Toast.makeText(Login.this, "Cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();
                              Intent intent = new Intent(getApplicationContext(), LoginEntra.class);
                              startActivity(intent);
                              username.setText("");
                              password.setText("");
                              repassword.setText("");
                          }
                          else
                          {
                              Toast.makeText(Login.this, "Cadastro Falhou!!", Toast.LENGTH_SHORT).show();
                          }
                       }
                       else
                       {
                           Toast.makeText(Login.this, "Usuário já existe! \n Por favor faça Login.", Toast.LENGTH_SHORT).show();
                       }
                    }
                    else
                    {
                        Toast.makeText(Login.this, "Senhas não são iguais!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginEntra.class);
                startActivity(intent);
            }
        });
    }


}