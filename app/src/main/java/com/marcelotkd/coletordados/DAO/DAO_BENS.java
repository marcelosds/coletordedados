package com.marcelotkd.coletordados.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.marcelotkd.coletordados.OBJETOS.Bem;
import com.marcelotkd.coletordados.SpinnerObject;

import java.util.ArrayList;
import java.util.List;

public class DAO_BENS extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "bens.db";
    public static final String TABLE_NAME = "bens_table";



    public DAO_BENS(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();


    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //CRIA TABELA DOS BENS PATRIMONIAIS
       String tab_bens = "CREATE TABLE bens_table (ID INT, CODIGO TEXT, DESCRICAO TEXT, LOCALIZACAO TEXT, ESTADO TEXT, SITUACAO TEXT, DATA TEXT, CDLOCAL TEXT, CDESTADO TEXT, CDSITUACAO TEXT, CDINVENTARIO TEXT);";
        db.execSQL(tab_bens);
        String val_bens = "INSERT INTO bens_table (ID, CODIGO, DESCRICAO,LOCALIZACAO, ESTADO, SITUACAO, DATA, CDLOCAL, CDESTADO, CDSITUACAO, CDINVENTARIO) VALUES " +
                "(1, 1, 'Bem Teste', 'Local Teste 01', 'Excelente', 'Situação Teste 01', '00/00/0000', 1, 1, 1, 1)," +
                "(2, 2, 'Bem Teste 01', 'Local Teste 02', 'Bom', 'Situação Teste 02', '00/00/0000', 2, 2, 2, 1);";
        db.execSQL(val_bens);


        //CRIA TABELA DAS LOCALIZAÇÕES
        String tab_locais = "CREATE TABLE localizacao_spinner(CODIGO INT, LOCALIZACAO TEXT);";
        db.execSQL(tab_locais);
        String val_locais = "INSERT INTO localizacao_spinner (CODIGO, LOCALIZACAO) VALUES " +
                "(1, 'Local Teste 01')," +
                "(2, 'Local Teste 02')," +
                "(3, 'Local Teste 03');";
        db.execSQL(val_locais);


        //CRIA TABELA DOS ESTADOS DE CONSERVAÇÃO
        String tab_estado = "CREATE TABLE estado_spinner(CODIGO INT, ESTADO TEXT);";
        db.execSQL(tab_estado);
        String val_estado = "INSERT INTO estado_spinner (CODIGO, ESTADO) VALUES " +
                "(1, 'Excelente'), " +
                "(2, 'Bom'), " +
                "(3, 'Regular'), " +
                "(4, 'Péssimo'); ";
        db.execSQL(val_estado);



        //CRIA TABELA DAS SITUAÇÕES
        String tab_situacao = "CREATE TABLE situacao_spinner(CODIGO INT, SITUACAO TEXT);";
        db.execSQL(tab_situacao);
        String val_situacao = "INSERT INTO situacao_spinner (CODIGO, SITUACAO) VALUES " +
                "(1, 'Situação Teste 01')," +
                "(2, 'Situação Teste 02')," +
                "(3, 'Situação Teste 03'); ";
        db.execSQL(val_situacao);


    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS bens_table");
        db.execSQL("DROP TABLE IF EXISTS localizacao_spinner");
        db.execSQL("DROP TABLE IF EXISTS estado_spinner");
        db.execSQL("DROP TABLE IF EXISTS situacao_spinner");
        onCreate(db);

    }

    //ATUALIZA AS INFORMAÇÕES ALTERADAS DOS BENS PATRIMONIAIS
    public boolean updateDados(String inCODIGO, String inDESCRICAO, String inCDLOCAL, String inLOCALIZACAO, String inCDESTADO, String inESTADO, String inCDSITUACAO, String inSITUACAO, String inDATA){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CODIGO", inCODIGO);
        cv.put("DESCRICAO", inDESCRICAO);
        cv.put("CDLOCAL", inCDLOCAL);
        cv.put("LOCALIZACAO", inLOCALIZACAO);
        cv.put("CDESTADO", inCDESTADO);
        cv.put("ESTADO", inESTADO);
        cv.put("CDSITUACAO", inCDSITUACAO);
        cv.put("SITUACAO", inSITUACAO);
        cv.put("DATA", inDATA);

        String[] args = {inCODIGO+""};

        int result = db.update("bens_table",  cv, "CODIGO=?", args);

       return result !=0;
    }




    //CURSOR QUE SELECIONA OS BENS FILTRADOS DA TABELA BENS_TABLE, PARA MOSTRAR NA 1ª LISTA
    public Cursor viewDados(String CODIGO) {

         SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM bens_table WHERE CODIGO = " + "'" + CODIGO + "'";

        Cursor cursor = db.rawQuery(query, null);

        return cursor;

    }

    //CURSOR QUE SELECIONA TODOS OS BENS DA TABELA BENS_TABLE, PARA MOSTRAR NA 2ª LISTA
    public Cursor viewTudo() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM bens_table";

        Cursor cursor = db.rawQuery(query, null);

        return cursor;

    }

        private Context getContext () {
            return null;
        }


        //CARREGA ARRAY DAS LOCALIZAÇÕES
        public List < SpinnerObject> getAllLabels(){
        List < SpinnerObject > spinner_locais = new ArrayList < SpinnerObject > ();
        // Seleciona todas as consultas
        String selectQuery = "SELECT  * FROM localizacao_spinner;" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop através de todas as linhas e adicionando à lista
        if ( cursor.moveToFirst () ) {
            do {
                spinner_locais.add ( new SpinnerObject ( cursor.getString(0) , cursor.getString(1) ) );
            } while (cursor.moveToNext());
        }

        // Fecha conexão
        cursor.close();
        db.close();

        return spinner_locais;
    }

        //CARREGA ARRAY DOS ESTADOS DE CONSERVAÇÃO
       public List < SpinnerObject> getAllLabels1(){
        List < SpinnerObject > spinner_estado = new ArrayList < SpinnerObject > ();
        // Seleciona todas as consultas
        String selectQuery = "SELECT  * FROM estado_spinner;" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop através de todas as linhas e adicionando à lista
        if ( cursor.moveToFirst () ) {
            do {
                spinner_estado.add ( new SpinnerObject ( cursor.getString(0) , cursor.getString(1) ) );
            } while (cursor.moveToNext());
        }

        // Fecha conexão
        cursor.close();
        db.close();

        return spinner_estado;
    }


        //CARREGA ARRAY DAS SITUAÇÕES
        public List < SpinnerObject> getAllLabels2(){
        List < SpinnerObject > spinner_situacao = new ArrayList < SpinnerObject > ();
        // Seleciona todas as consultas
        String selectQuery = "SELECT  * FROM situacao_spinner;" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop através de todas as linhas e adicionando à lista
        if ( cursor.moveToFirst () ) {
            do {
                spinner_situacao.add ( new SpinnerObject ( cursor.getString(0) , cursor.getString(1) ) );
            } while (cursor.moveToNext());
        }

        // Fecha conexão
        cursor.close();
        db.close();

        return spinner_situacao;
    }

}

