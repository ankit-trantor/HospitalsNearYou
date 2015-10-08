package com.example.root.hospitalsnearyou.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.root.hospitalsnearyou.Activity.MainActivity;
import com.example.root.hospitalsnearyou.DB.HospitalDataBase;
import com.example.root.hospitalsnearyou.ModelClass.Hospital;
import com.example.root.hospitalsnearyou.R;
import com.example.root.hospitalsnearyou.adapter.HospNameAdap;

import java.util.ArrayList;

public class ListFragment1 extends Fragment {
    ArrayList<Hospital> hospNameList = new ArrayList<>();
    HospitalDataBase dbHelper;
    HospNameAdap hospNameAdap;
    ListView listView;
    View rootView;
    int position;
    long rowId;
    private MainActivity mainactivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.listHospName);
        mainactivity = (MainActivity) getActivity();
        addListListener();
        dbHelper = new HospitalDataBase(getActivity());
        addDataTolist();
        return rootView;
    }

    private void addDataTolist() {
        hospNameList.clear();
        hospNameList.addAll(dbHelper.readHospitalDataFromDatabase());
        hospNameAdap = new HospNameAdap(getActivity(), hospNameList);
        hospNameAdap.notifyDataSetChanged();
        listView.setAdapter(hospNameAdap);
    }

    private void addListListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainactivity.hospitalDetailsFrag(i);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
}
