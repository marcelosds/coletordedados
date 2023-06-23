package com.marcelotkd.coletordados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginEntra extends AppCompatActivity {

    EditText username,password;
    Button btnLogin;

    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_entra);

        setTitle("Coletor de Dados Patrimoniais");

        username = (EditText) findViewById(R.id.usernameLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        myDB = new DBHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("") || pass.equals("")){
                    Toast.makeText(LoginEntra.this, "Digite as Credenciais.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                  Boolean result = myDB.checkusernamePassword(user, pass);
                  if(result == true){
                      Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                      startActivity(intent);
                      username.setText("");
                      password.setText("");
                  }
                  else
                  {
                      Toast.makeText(LoginEntra.this, "Credenciais Inválidas!.", Toast.LENGTH_SHORT).show();
                  }
                }
            }
        });
    }
}