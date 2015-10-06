package com.example.root.hospitalsnearyou.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.root.hospitalsnearyou.ModelClass.BloodBank;
import com.example.root.hospitalsnearyou.ModelClass.Hospital;

import java.util.ArrayList;

/**
 * Created by root on 28/9/15.
 */
public class HospitalDataBase {
    private static final String KEY_ROW_ID_HOSPITAL = "_id";
    //    private static final String KEY_HOSPITAL_ID = "hospitalId";
//    private static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_STATE_HOSPITAL = "state";
    public static final String KEY_CITY_HOSPITAL = "city";
    private static final String KEY_HOSPITAL_PRIVATE = "pvt";
    private static final String KEY_CATEGORY_HOSPITAL = "category";
    private static final String KEY_SYSTEM_OF_MEDICINE = "SystemsOfMedicine";
    private static final String KEY_CONTACT_HOSPITAL = "contact";
    private static final String KEY_PINCODE_HOSPITAL = "pincode";
    private static final String KEY_EMAIL_HOSPITAL = "email";
    private static final String KEY_WEBSITE_HOSPITAL = "website";
    private static final String KEY_Specializations = "Specializations";
    private static final String KEY_SERVICES = "Services";
    private static final String HOSPITAL_TABLE_NAME = "hospital";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hospitalDb";


    private static final String KEY_ROW_ID = "_id";
    private static final String KEY_HOSPITAL_ID = "hospitalId";
    private static final String KEY_STATE = "state";
    private static final String KEY_CITY = "city";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_HOSPITAL_NAME = "h_name";
    private static final String KEY_contact = "contact";
    private static final String KEY_PINCODE = "pincode";
    private static final String KEY_email = "email";
    private static final String KEY_website = "website";
    private static final String KEY_category = "category";
    private static final String KEY_BLOOD_COMPONENT = "blood_component";
    private static final String KEY_BLOOD_GROUP = "blood_group";
    private static final String KEY_SERVICE_TIME = "service_time";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_HELPLINE = "helpline";
    private static final String KEY_FAX = "fax";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String BLOOD_BANK_TABLE_NAME = "bloodBank";


    private static final String DATABASE_CREATE_HOSPITAL = "create table hospital(_id integer primary key autoincrement default 1, "
            + " state text , city text , pvt text , category text , SystemsOfMedicine text ," +
            "contact text , pincode text , email text ," +
            "website text , Specializations text , Services text);";

    private static final String DATABASE_CREATE_BLOODBANK = "create table bloodBank(_id integer primary key autoincrement default 1,"
            + " state text , city text , district text , h_name text ,  address text  , pincode text , contact text , helpline text ," +
            " fax text , category text ,   website text , email text , blood_component text , blood_group text , service_time text ," +
            " latitude text , longitude text);";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    ArrayList<Hospital> hospitalData = new ArrayList<Hospital>();
    ArrayList<BloodBank> bloodBankData = new ArrayList<BloodBank>();

    private Context context;


    public void insertIntoDbHospital(ArrayList<Hospital> hospitalData) {
        this.hospitalData = hospitalData;
        for (int i = 0; i < hospitalData.size(); i++) {
            ContentValues values = new ContentValues();
//            values.put(KEY_HOSPITAL_ID, hospitalData.get(i).getHospitalId());
//            values.put(KEY_TIMESTAMP, hospitalData.get(i).getTimestamp());
            values.put(KEY_STATE_HOSPITAL, hospitalData.get(i).getState());
            values.put(KEY_CITY_HOSPITAL, hospitalData.get(i).getCity());
            values.put(KEY_HOSPITAL_PRIVATE, hospitalData.get(i).getPvt());
            values.put(KEY_CATEGORY_HOSPITAL, hospitalData.get(i).getCategory());
            values.put(KEY_SYSTEM_OF_MEDICINE, hospitalData.get(i).getSystemsOfMedicine());
            values.put(KEY_CONTACT_HOSPITAL, hospitalData.get(i).getContact());
            values.put(KEY_PINCODE_HOSPITAL, hospitalData.get(i).getPincode());
            values.put(KEY_EMAIL_HOSPITAL, hospitalData.get(i).getEmail());
            values.put(KEY_WEBSITE_HOSPITAL, hospitalData.get(i).getWebsite());
            values.put(KEY_Specializations, hospitalData.get(i).getSpecializations());
            values.put(KEY_SERVICES, hospitalData.get(i).getServices());
            db.insert(HOSPITAL_TABLE_NAME, null, values);
        }
    }

    public void insertIntoDbBloodBank(ArrayList<BloodBank> bloodBankData) {
        this.bloodBankData = bloodBankData;
        for (int i = 0; i < bloodBankData.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_STATE, bloodBankData.get(i).getState());
            values.put(KEY_CITY, bloodBankData.get(i).getCity());
            values.put(KEY_DISTRICT, bloodBankData.get(i).getDistrict());
            values.put(KEY_HOSPITAL_NAME, bloodBankData.get(i).getHospitalName());
            values.put(KEY_ADDRESS, bloodBankData.get(i).getAddress());
            values.put(KEY_PINCODE, bloodBankData.get(i).getPincode());
            values.put(KEY_contact, bloodBankData.get(i).getContact());
            values.put(KEY_HELPLINE, bloodBankData.get(i).getHelpline());
            values.put(KEY_FAX, bloodBankData.get(i).getFax());
            values.put(KEY_category, bloodBankData.get(i).getCategory());
            values.put(KEY_website, bloodBankData.get(i).getWebsite());
            values.put(KEY_email, bloodBankData.get(i).getEmail());
            values.put(KEY_BLOOD_COMPONENT, bloodBankData.get(i).getBloodComponent());
            values.put(KEY_BLOOD_GROUP, bloodBankData.get(i).getBloodGroup());
            values.put(KEY_SERVICE_TIME, bloodBankData.get(i).getServiceTime());
            values.put(KEY_LATITUDE, bloodBankData.get(i).getLatitude());
            values.put(KEY_LONGITUDE, bloodBankData.get(i).getLangitude());
            db.insert(BLOOD_BANK_TABLE_NAME, null, values);
        }
    }

    public HospitalDataBase(Context context) {
        this.context = context;
    }

    public HospitalDataBase open() {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_HOSPITAL);
            db.execSQL(DATABASE_CREATE_BLOODBANK);
            Log.e("dbdb", "created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + HOSPITAL_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + BLOOD_BANK_TABLE_NAME);
            onCreate(db);
            Log.e("dbdb", "upgraded");
        }
    }

    public ArrayList<Hospital> readFromDatabase() {
        ArrayList<Hospital> hospitalDataList = new ArrayList<>();
        String read = "";
        read += "select * from " + HOSPITAL_TABLE_NAME;
        open();

        Cursor cursor = db.rawQuery(read, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(KEY_HOSPITAL_PRIVATE));
                    long _id = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID_HOSPITAL)));
                    Hospital hospital = new Hospital();
                    hospital.setRowId(_id);
                    hospital.setPvt(hospitalName);
                    hospitalDataList.add(hospital);

                } while (cursor.moveToNext());
            cursor.close();
            close();

        }
        return hospitalDataList;
    }

    public ArrayList<Hospital> stateWiseHospital(String userState, String userCity) {
        ArrayList<Hospital> hospitalList = new ArrayList<>();
        String readData = "";
        readData += "select * from " + HOSPITAL_TABLE_NAME + " where city='" + userCity + "' AND state='" + userState + "'";
        open();
        Cursor cursor = db.rawQuery(readData, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(KEY_HOSPITAL_PRIVATE));
//                    String timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP));
                    String state = cursor.getString(cursor.getColumnIndex(KEY_STATE_HOSPITAL));
                    String city = cursor.getString(cursor.getColumnIndex(KEY_CITY_HOSPITAL));
                    String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_HOSPITAL));
                    String website = cursor.getString(cursor.getColumnIndex(KEY_WEBSITE_HOSPITAL));
                    String contact = cursor.getString(cursor.getColumnIndex(KEY_CONTACT_HOSPITAL));
                    String email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL_HOSPITAL));
                    String systemsOfMedicine = cursor.getString(cursor.getColumnIndex(KEY_SYSTEM_OF_MEDICINE));
                    String pincode = cursor.getString(cursor.getColumnIndex(KEY_PINCODE_HOSPITAL));
                    String specialization = cursor.getString(cursor.getColumnIndex(KEY_Specializations));
                    String services = cursor.getString(cursor.getColumnIndex(KEY_SERVICES));
                    long hospId = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID_HOSPITAL)));
                    Hospital hospital = new Hospital();
                    hospital.setState(state);
                    hospital.setCity(city);
                    hospital.setCategory(category);
                    hospital.setSpecializations(specialization);
                    hospital.setPincode(pincode);
                    hospital.setContact(contact);
                    hospital.setSystemsOfMedicine(systemsOfMedicine);
                    hospital.setServices(services);
                    hospital.setWebsite(website);
//                    hospital.setTimestamp(timestamp);
                    hospital.setEmail(email);
                    hospital.setRowId(hospId);
                    hospital.setPvt(hospitalName);
                    hospitalList.add(hospital);
                } while (cursor.moveToNext());
            cursor.close();
            close();
        }
        return hospitalList;
    }

    public Hospital getSinglerecord(long rowId) {
        Hospital hospital = new Hospital();
        String read = "";
        read += "select * from " + HOSPITAL_TABLE_NAME + " where _id =" + rowId;
        open();

        Cursor cursor = db.rawQuery(read, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(KEY_HOSPITAL_PRIVATE));
//                    String timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP));
                    String state = cursor.getString(cursor.getColumnIndex(KEY_STATE_HOSPITAL));
                    String city = cursor.getString(cursor.getColumnIndex(KEY_CITY_HOSPITAL));
                    String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_HOSPITAL));
                    String website = cursor.getString(cursor.getColumnIndex(KEY_WEBSITE_HOSPITAL));
                    String contact = cursor.getString(cursor.getColumnIndex(KEY_CONTACT_HOSPITAL));

                    String email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL_HOSPITAL));
                    String systemsOfMedicine = cursor.getString(cursor.getColumnIndex(KEY_SYSTEM_OF_MEDICINE));
                    String pincode = cursor.getString(cursor.getColumnIndex(KEY_PINCODE_HOSPITAL));
                    String specialization = cursor.getString(cursor.getColumnIndex(KEY_Specializations));
                    String services = cursor.getString(cursor.getColumnIndex(KEY_SERVICES));

                    hospital.setState(state);
                    hospital.setCity(city);
                    hospital.setCategory(category);
                    hospital.setSpecializations(specialization);
                    hospital.setPincode(pincode);
                    hospital.setContact(contact);
                    hospital.setSystemsOfMedicine(systemsOfMedicine);
                    hospital.setServices(services);
                    hospital.setWebsite(website);
//                    hospital.setTimestamp(timestamp);
                    hospital.setEmail(email);
                    hospital.setPvt(hospitalName);
//                    hospitalDetails.add(hospital);
                } while (cursor.moveToNext());
            cursor.close();
            close();

        }
        return hospital;
    }

}
