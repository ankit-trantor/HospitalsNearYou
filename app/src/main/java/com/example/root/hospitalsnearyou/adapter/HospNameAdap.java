package com.example.root.hospitalsnearyou.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.hospitalsnearyou.Fragment.ListFragment1;
import com.example.root.hospitalsnearyou.ModelClass.Hospital;
import com.example.root.hospitalsnearyou.R;

import java.util.ArrayList;

public class HospNameAdap extends ArrayAdapter<Hospital> {
    ArrayList<Hospital> arrayList = new ArrayList<>();
    View row;
    Context context;
    ListFragment1 listFragment1 = new ListFragment1();
    public HospNameAdap(Context context, ArrayList<Hospital> playlistArray) {
        super(context, R.layout.fragment_list, android.R.id.text1, playlistArray);
        this.arrayList = playlistArray;
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.name_list, parent, false);
        } else
            row = convertView;

        Hospital modelclass = arrayList.get(position);

        TextView textView = (TextView) row.findViewById(R.id.itemOnList);
        textView.setSingleLine(true);
        textView.setText(modelclass.getPvt());
        textView.setTextColor(Color.BLACK);
        ImageView imageView = (ImageView) row.findViewById(R.id.playlist_image);
        imageView.setImageResource(R.drawable.hospital);


       /* row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listFragment1.onClick(position);
            }
        });*/
        return row;
    }

}

