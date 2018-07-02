package br.com.gabryel.petshop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Gabryel Boeira on 26/06/2018.
 */

public class Conexao extends SQLiteOpenHelper {

    private static Conexao conexao;

    public static Conexao getInstance(){
        return conexao;
    }


    public Conexao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        conexao = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String statement  = " create table petshop (" +
                " id integer primary key autoincrement," +
                " nomeDono varchar (255)," +
                " nomeAnimal varchar (255)," +
                " procedimento varchar (255)," +
                " tipo varchar (50)," +
                " numero varchar (20), " +
                " imagem blob" +
                ")";

        db.execSQL(statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }}
