package com.example.macmini1_cuceimobile.horarioudeg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Paulo on 20/06/2016.
 */

public class AdaptadorHorario extends ArrayAdapter<String>{

    private LinearLayout linearLayout;

    public AdaptadorHorario(Context context, ArrayList<String> strings, LinearLayout linearLayout) {

        super(context, R.layout.activity_main2,strings);
        this.linearLayout = linearLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());
        convertView = vi.inflate( R.layout.activity_main2, null);

        return convertView;
    }
}
