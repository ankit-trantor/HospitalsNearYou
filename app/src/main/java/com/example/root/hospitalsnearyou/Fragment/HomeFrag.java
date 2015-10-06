package com.example.root.hospitalsnearyou.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.root.hospitalsnearyou.DB.HospitalDataBase;
import com.example.root.hospitalsnearyou.R;

public class HomeFrag extends Fragment {
    AutoCompleteTextView edtState, edtCity;
    Button submit;
    String[] state = {"Delhi", "Maharashtra", "Andhra Pradesh", "Kerala","Karnataka","Tamil Nadu", "Punjab","Haryana","Sikkim", "Rajasthan",
            "Gujarat", "Assam", "Jammu and Kashmir",
            "Himachal Pradesh"};
    String[] city = {"Delhi","Pune","Mumbai","Rohtak","North West Delhi", "Madhya Pradesh","Cochin","Kannur","Karakonam","Alappuzha","South Delhi","West Delhi",
            "South West Delhi","Chennai","Ludhiana","Jaipur","Patiala","Mangalore","Indore", "Bengaluru","Amritsar","Zirakpur","Ropar","Sirsa","Jodhpur","Gangtok","Jammu","Kangra"};
    ArrayAdapter<String> adapterForState;
    ArrayAdapter<String> adapterForCity;

    public HomeFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterForState = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, state);
        adapterForCity = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, city);
        //Getting the instance of AutoCompleteTextView


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hospital_name_list, container, false);
        edtCity = (AutoCompleteTextView) rootView.findViewById(R.id.city);
        edtState = (AutoCompleteTextView) rootView.findViewById(R.id.state);
        edtState.setThreshold(1);//will start working from first character
        edtState.setAdapter(adapterForState);//setting the adapterForState data into the AutoCompleteTextView
        edtState.setTextColor(Color.BLACK);

        edtCity.setThreshold(1);
        edtCity.setAdapter(adapterForCity);
        edtCity.setTextColor(Color.BLACK);

        submit = (Button) rootView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtCity.getText().toString();
                String state = edtState.getText().toString();
//                HomeFrag fragment = new HomeFrag();
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
