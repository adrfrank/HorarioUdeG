package com.example.macmini1_cuceimobile.horarioudeg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by root on 12/04/16.
 */



public class AdaptadorDrawer extends ArrayAdapter<String>  {

    private LinearLayout linearLayout;

    public AdaptadorDrawer(Context context, ArrayList<String> strings, LinearLayout linearLayout) {

        super(context, R.layout.drawer,strings);
        this.linearLayout = linearLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());
        convertView = vi.inflate( R.layout.drawer, null);

        Switch aSwitch = (Switch) convertView.findViewById(R.id.drawer1);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    linearLayout.setVisibility(View.VISIBLE);



                } else {
                    linearLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        return convertView;

    }



}
