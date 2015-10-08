package com.example.root.hospitalsnearyou.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.root.hospitalsnearyou.Activity.MainActivity;
import com.example.root.hospitalsnearyou.DB.HospitalDataBase;
import com.example.root.hospitalsnearyou.ModelClass.BloodBank;
import com.example.root.hospitalsnearyou.R;
import com.example.root.hospitalsnearyou.adapter.BloodBankAdap;

import java.util.ArrayList;


public class BloodBankList extends Fragment {
    ArrayList<BloodBank> hospNameList = new ArrayList<>();
    HospitalDataBase dbHelper;
    BloodBankAdap hospNameAdap;
    ListView listView;
    View rootView;
    int position;
    long rowId;
    private MainActivity mainactivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_blood_bank_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.listBloodBank);
        mainactivity = (MainActivity) getActivity();
        addListListener();
        dbHelper = new HospitalDataBase(getActivity());
        addDataTolist();
        return rootView;
    }

    private void addDataTolist() {
        hospNameList.clear();
        hospNameList.addAll(dbHelper.readBloddBankDataFromDB());
        hospNameAdap = new BloodBankAdap(getActivity(), hospNameList);
        hospNameAdap.notifyDataSetChanged();
        listView.setAdapter(hospNameAdap);
    }

    private void addListListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainactivity.gotoBloodBankDeatails(i);
            }
        });
    }
}
