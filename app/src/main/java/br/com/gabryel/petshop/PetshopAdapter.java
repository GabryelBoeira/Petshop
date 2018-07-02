package br.com.gabryel.petshop;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gabryel Boeira on 27/06/2018.
 */

public class PetshopAdapter extends BaseAdapter {

    //Construtor da classe
    private List<Petshop> petshops;
    Activity act;

    public PetshopAdapter(List<Petshop> petshops, Activity act) {
        this.petshops = petshops;
        this.act = act;
    }
    //fim do construtor

    @Override
    public int getCount() {
        return this.petshops.size();
    }

    @Override
    public Object getItem(int position) {
        return this.petshops.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = act.getLayoutInflater().inflate(R.layout.petshop_adapter, parent, false);

        TextView textView1 = (TextView) v.findViewById(R.id.textView);
        TextView textView2 = (TextView) v.findViewById(R.id.textView2);
        ImageView image = (ImageView) v.findViewById(R.id.imageAdapter);
        Bitmap raw;
        byte[] imagemArray;

        Petshop p = petshops.get(position);

        textView1.setText(p.getNomeDono());
        textView2.setText(p.getNumero());
        imagemArray = p.getImagem();

        //imagem personalizada
        if(imagemArray != null){
            raw  = BitmapFactory.decodeByteArray(imagemArray,0,imagemArray.length);
            image.setImageBitmap(raw);
        }

        return v;
    }

    public void remove(Petshop petshop) {
        this.petshops.remove(petshop);
    }

}