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
import com.example.root.hospitalsnearyou.ModelClass.ModelClassDB;
import com.example.root.hospitalsnearyou.R;
import com.example.root.hospitalsnearyou.adapter.HospNameAdap;

import java.util.ArrayList;

public class SearchHospitalList extends Fragment {

    ArrayList<ModelClassDB> hospNameList = new ArrayList<>();
    HospitalDataBase dbHelper;
    HospNameAdap hospNameAdap;
    ListView listView;
    View rootView;
    String userCity;
    String userState;
    int position;
    long rowId;
    private MainActivity mainactivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_hospital_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.searchHospital);
        mainactivity = (MainActivity) getActivity();
        Bundle bundle = this.getArguments();
        userCity = bundle.getString("city");
        userState = bundle.getString("state");

        dbHelper = new HospitalDataBase(getActivity());
        addListListener();
        addDataTolist();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void addDataTolist() {
        hospNameList.clear();
        hospNameList.addAll(dbHelper.stateWiseHospital(userState, userCity));
        hospNameAdap = new HospNameAdap(getActivity(), hospNameList);
        hospNameAdap.notifyDataSetChanged();
        listView.setAdapter(hospNameAdap);
    }

    private void addListListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mainactivity.gotoDeatailsFrag1(i, hospNameList.get(i).getState(),hospNameList.get(i).getCity());
            }
        });
    }
}
