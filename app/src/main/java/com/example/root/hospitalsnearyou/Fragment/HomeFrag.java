package com.example.root.hospitalsnearyou.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.hospitalsnearyou.DB.HospitalDataBase;
import com.example.root.hospitalsnearyou.R;

public class HomeFrag extends Fragment {
    EditText edtCity, edtState;
    Button submit;

    public HomeFrag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hospital_name_list, container, false);
        edtCity = (EditText) rootView.findViewById(R.id.city);
        edtState = (EditText) rootView.findViewById(R.id.state);
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
                bundle1.putString("state", state);gotoUserHospDetails.setArguments(bundle1);
                SearchHospitalList searchHospitalList = new SearchHospitalList();
                Bundle bundle = new Bundle();
                bundle.putString("city", city);
                bundle.putString("state", state);
                searchHospitalList.setArguments(bundle);
                HospitalDataBase hospitalDataBase=new HospitalDataBase(getActivity());
                if(hospitalDataBase.stateWiseHospital(state,city).size()>0) {

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, searchHospitalList).commit();
                }
                else
                {
                    Toast.makeText(getActivity(),"No records found",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return rootView;
    }

}
