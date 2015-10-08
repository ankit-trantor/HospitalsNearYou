package com.example.root.hospitalsnearyou.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.root.hospitalsnearyou.DB.HospitalDataBase;
import com.example.root.hospitalsnearyou.R;

import java.util.ArrayList;

public class HomeFrag extends Fragment {
    AutoCompleteTextView edtState, edtCity;
    Button findHospital, findBloodBank;
    String[] state = {"Delhi", "Andhra Pradesh", "Kerala", "Karnataka", "Tamil Nadu", "Punjab", "Haryana", "Sikkim", "Rajasthan",
            "Gujarat", "Assam", "Jammu and Kashmir",
            "Himachal Pradesh"};
    String[] city = {"Delhi", "Pune", "Mumbai", "Rohtak", "North West Delhi", "Madhya Pradesh", "Cochin", "Kannur", "Karakonam", "Alappuzha", "South Delhi", "West Delhi",
            "South West Delhi", "Chennai", "Ludhiana", "Jaipur", "Patiala", "Mangalore", "Indore", "Bengaluru", "Amritsar", "Zirakpur", "Ropar", "Sirsa", "Jodhpur", "Gangtok", "Jammu", "Kangra"};
    ArrayAdapter<String> adapterForState;
    ArrayAdapter<String> adapterForCity;
    public HomeFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterForState = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, state);

        //Getting the instance of AutoCompleteTextView
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        edtCity = (AutoCompleteTextView) rootView.findViewById(R.id.city);
        edtState = (AutoCompleteTextView) rootView.findViewById(R.id.state);



        edtState.setThreshold(1);//will start working from first character
        edtState.setAdapter(adapterForState);//setting the adapterForState data into the AutoCompleteTextView
        edtState.setTextColor(Color.BLACK);

        edtState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> cityList = new ArrayList<String>();
                cityList.clear();
                String state = edtState.getText().toString();
                HospitalDataBase hospitalDataBase = new HospitalDataBase(getActivity());
                cityList = hospitalDataBase.cityFromDb(state);

                adapterForCity = new ArrayAdapter<String>
                        (getActivity(), android.R.layout.select_dialog_item, cityList);
                edtCity.setThreshold(1);
                edtCity.setAdapter(adapterForCity);
                edtCity.setTextColor(Color.BLACK);
            }
        });
        findBloodBank = (Button) rootView.findViewById(R.id.bloodBank);
        findBloodBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtCity.getText().toString();
                String state = edtState.getText().toString();
                GotoUserHospDetails gotoUserHospDetails = new GotoUserHospDetails();
                Bundle bundle1 = new Bundle();
                bundle1.putString("city", city);
                bundle1.putString("state", state);
                gotoUserHospDetails.setArguments(bundle1);
                SearchHospitalList searchHospitalList = new SearchHospitalList();
                Bundle bundle = new Bundle();
                bundle.putString("city", city);
                bundle.putString("state", state);
                searchHospitalList.setArguments(bundle);
            }
        });


        findHospital = (Button) rootView.findViewById(R.id.findHospital);
        findHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtCity.getText().toString();
                String state = edtState.getText().toString();

                GotoUserHospDetails gotoUserHospDetails = new GotoUserHospDetails();
                Bundle bundle1 = new Bundle();
                bundle1.putString("city", city);
                bundle1.putString("state", state);
                gotoUserHospDetails.setArguments(bundle1);
                SearchHospitalList searchHospitalList = new SearchHospitalList();
                Bundle bundle = new Bundle();
                bundle.putString("city", city);
                bundle.putString("state", state);
                searchHospitalList.setArguments(bundle);
                HospitalDataBase hospitalDataBase = new HospitalDataBase(getActivity());
//                ArrayList<String> cityList = hospitalDataBase.cityFromDb(state);
//
//                adapterForCity = new ArrayAdapter<String>
//                        (getActivity(), android.R.layout.select_dialog_item, cityList);
//                edtCity.setThreshold(1);
//                edtCity.setAdapter(adapterForCity);
//                edtCity.setTextColor(Color.BLACK);

                if (hospitalDataBase.stateWiseHospital(state, city).size() > 0) {

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, searchHospitalList).commit();
                } else {
                    Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return rootView;
    }

}
