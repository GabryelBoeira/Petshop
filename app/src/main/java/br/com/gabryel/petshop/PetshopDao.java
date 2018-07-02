package br.com.gabryel.petshop;

/**
 * Created by Aluno on 02/07/2018.
 */


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabryel Boeira on 27/06/2018.
 */

public class PetshopDao  {

    static ArrayList<Petshop> petshop = new ArrayList<Petshop>();
    static Integer id = 0;

    public void salvar (Petshop petshop){
        SQLiteDatabase conn = Conexao.getInstance().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nomeDono", petshop.getNomeDono());
        values.put("nomeAnimal", petshop.getNomeAnimal());
        values.put("procedimento", petshop.getProcedimento());
        values.put("tipo", petshop.getTipo());
        values.put("numero", petshop.getNumero());
        values.put("imagem", petshop.getImagem());

        if(petshop.getId() == null){
            conn.insert("petshop", null,values);
        } else {
            conn.update("petshop", values,"id = ?", new String [] {petshop.getId().toString()});
        }

    }

    public List<Petshop> listar(){
        SQLiteDatabase conn = Conexao.getInstance().getReadableDatabase();


        Cursor p = conn.query("petshop",new String[] {"id","nomeDono","nomeAnimal","procedimento","tipo","numero", "imagem"},
                null, null, null, null,"nomeDono");

        ArrayList<Petshop> petshops = new ArrayList<Petshop>();

        if (p.moveToFirst()){
            do{
                Petshop pet = new Petshop();

                pet.setId(p.getInt(0));
                pet.setNomeDono(p.getString(1));
                pet.setNomeAnimal(p.getString(2));
                pet.setProcedimento(p.getString(3));
                pet.setTipo(p.getString(4));
                pet.setNumero(p.getString(5));
                pet.setImagem(p.getBlob(6));

                petshops.add(pet);

            } while (p.moveToNext());
        }

        return petshops;
    }

    public void excluir(Petshop petshop) {

        SQLiteDatabase conn = Conexao.getInstance().getWritableDatabase();

        conn.delete("petshop","id = ?", new String [] {petshop.getId().toString()});

    }

}