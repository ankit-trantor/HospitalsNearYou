package com.example.root.hospitalsnearyou.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.hospitalsnearyou.DB.HospitalDataBase;
import com.example.root.hospitalsnearyou.ModelClass.ModelClassDB;
import com.example.root.hospitalsnearyou.R;

import java.util.ArrayList;

public class HospitalDetailsFrag extends Fragment {
    HospitalDataBase dbHelper;
    TextView pincode, email, website, contact, hospitalName, specialization, service, timestamp, systemsOfMedicine, city;
    TextView state, category, phoneNo;
    Bundle bundle = new Bundle();
    int position;
    String cno;
    String address;
    ModelClassDB modelClassDB;
    ArrayList<ModelClassDB> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hospital_details, container, false);
        position = getArguments().getInt("position");
        dbHelper = new HospitalDataBase(getActivity());
        arrayList.addAll(dbHelper.readFromDatabase());
        modelClassDB = dbHelper.getSinglerecord(arrayList.get(position).getRowId());
        findId(rootView);
        emailTextViewAction();
        websiteTextViewAction();
        addDialer();
        return rootView;
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void websiteTextViewAction() {
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String site = website.getText().toString();
                if (site.equals("NA")) {

                } else {
                    Log.e("site", "" + site);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + site));
                    startActivity(intent);
                }
            }
        });
    }

    private void emailTextViewAction() {
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = email.getText().toString();
                if (to.equals("NA")) {

                } else {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + to));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.map);
        menuItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Uri s = Uri.parse("geo:0,0?q=" + modelClassDB.getPvt() + ", " + address);
        if (address != null) {
            showMap(s);
        }
        return super.onOptionsItemSelected(item);
    }

    private void findId(View rootView) {
        email = (TextView) rootView.findViewById(R.id.EmailName);
        website = (TextView) rootView.findViewById(R.id.websiteName);
        hospitalName = (TextView) rootView.findViewById(R.id.HospitalName);
        timestamp = (TextView) rootView.findViewById(R.id.TimestampName);
        state = (TextView) rootView.findViewById(R.id.StateName);
        city = (TextView) rootView.findViewById(R.id.Cityname);
        specialization = (TextView) rootView.findViewById(R.id.specializationName);
        service = (TextView) rootView.findViewById(R.id.ServiceName);
        systemsOfMedicine = (TextView) rootView.findViewById(R.id.MedicineName);
        category = (TextView) rootView.findViewById(R.id.categoryName);
        pincode = (TextView) rootView.findViewById(R.id.pincodeName);
        contact = (TextView) rootView.findViewById(R.id.contactName);
        phoneNo = (TextView) rootView.findViewById(R.id.Phone_No);
        setAllText();
    }

    private void addDialer() {
        phoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + cno));
                startActivity(callIntent);
            }
        });


    }

    private void setAllText() {
        email.setText(modelClassDB.getEmail());
        email.setTextSize(15);
        website.setText(modelClassDB.getWebsite());
        if (!modelClassDB.getWebsite().equals("NA")) {
            website.setTextColor(Color.BLUE);
        }
        if (!modelClassDB.getEmail().equals("NA")) {
            email.setTextColor(Color.BLUE);
        }
        hospitalName.setText(modelClassDB.getPvt());
        timestamp.setText(modelClassDB.getTimestamp());
        state.setText(modelClassDB.getState());
        city.setText(modelClassDB.getCity());
        specialization.setText(modelClassDB.getSpecializations());
        systemsOfMedicine.setText(modelClassDB.getSystemsOfMedicine());
        service.setText(modelClassDB.getServices());
        category.setText(modelClassDB.getCategory());
        pincode.setText(modelClassDB.getPincode());
        contact.setText(modelClassDB.getContact());
        String contact = modelClassDB.getContact();
        if (contact.contains("Phone:")) {
            String[] splits = contact.split("Phone:");
            address = splits[0];
            String no1 = splits[1].trim();
            phoneNo.setText(no1);
            phoneNo.setTextColor(Color.BLUE);
        } else if (contact.contains("Phone-")) {
            String[] splits = contact.split("Phone-");
            address = splits[0];
            String no[] = splits[1].split(",");
            cno = no[0].trim();
            phoneNo.setText(cno);
            phoneNo.setTextColor(Color.BLUE);
        }
    }
}
