package com.example.root.hospitalsnearyou.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.root.hospitalsnearyou.ModelClass.Hospital;

import java.util.ArrayList;

/**
 * Created by root on 28/9/15.
 */
public class HospitalDataBase {
    private static final String KEY_ROW_ID = "_id";
//    private static final String KEY_HOSPITAL_ID = "hospitalId";
//    private static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_STATE = "state";
    public static final String KEY_CITY = "city";
    private static final String KEY_HOSPITAL_PRIVATE = "pvt";
    private static final String KEY_category = "category";
    private static final String KEY_SYSTEM_OF_MEDICINE = "SystemsOfMedicine";
    private static final String KEY_contact = "contact";
    private static final String KEY_PINCODE = "pincode";
    private static final String KEY_email = "email";
    private static final String KEY_website = "website";
    private static final String KEY_Specializations = "Specializations";
    private static final String KEY_SERVICES = "Services";
    private static final String TABLE_NAME = "hospital";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hospitalDb";

    private static final String DATABASE_CREATE = "create table hospital(_id integer primary key autoincrement default 1, "
            + " state text , city text , pvt text , category text , SystemsOfMedicine text ," +
            "contact text , pincode text , email text ," +
            "website text , Specializations text , Services text);";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    ArrayList<Hospital> hospitalData = new ArrayList<Hospital>();
    private Context context;


    public void insertIntoDb(ArrayList<Hospital> hospitalData) {
        this.hospitalData = hospitalData;
        for (int i = 0; i < hospitalData.size(); i++) {
            ContentValues values = new ContentValues();
//            values.put(KEY_HOSPITAL_ID, hospitalData.get(i).getHospitalId());
//            values.put(KEY_TIMESTAMP, hospitalData.get(i).getTimestamp());
            values.put(KEY_STATE, hospitalData.get(i).getState());
            values.put(KEY_CITY, hospitalData.get(i).getCity());
            values.put(KEY_HOSPITAL_PRIVATE, hospitalData.get(i).getPvt());
            values.put(KEY_category, hospitalData.get(i).getCategory());
            values.put(KEY_SYSTEM_OF_MEDICINE, hospitalData.get(i).getSystemsOfMedicine());
            values.put(KEY_contact, hospitalData.get(i).getContact());
            values.put(KEY_PINCODE, hospitalData.get(i).getPincode());
            values.put(KEY_email, hospitalData.get(i).getEmail());
            values.put(KEY_website, hospitalData.get(i).getWebsite());
            values.put(KEY_Specializations, hospitalData.get(i).getSpecializations());
            values.put(KEY_SERVICES, hospitalData.get(i).getServices());
//            open();
            db.insert(TABLE_NAME, null, values);
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

            db.execSQL(DATABASE_CREATE);
            Log.e("dbdb", "created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
            Log.e("dbdb", "upgraded");
        }
    }

    public ArrayList<Hospital> readFromDatabase() {
        ArrayList<Hospital> hospitalDataList = new ArrayList<>();
        String read = "";
        read += "select * from " + TABLE_NAME;
        open();

        Cursor cursor = db.rawQuery(read, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(KEY_HOSPITAL_PRIVATE));
                    long _id = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID)));
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
        readData += "select * from " + TABLE_NAME + " where city='" + userCity + "' AND state='" + userState + "'";
        open();
        Cursor cursor = db.rawQuery(readData, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(KEY_HOSPITAL_PRIVATE));
//                    String timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP));
                    String state = cursor.getString(cursor.getColumnIndex(KEY_STATE));
                    String city = cursor.getString(cursor.getColumnIndex(KEY_CITY));
                    String category = cursor.getString(cursor.getColumnIndex(KEY_category));
                    String website = cursor.getString(cursor.getColumnIndex(KEY_website));
                    String contact = cursor.getString(cursor.getColumnIndex(KEY_contact));
                    String email = cursor.getString(cursor.getColumnIndex(KEY_email));
                    String systemsOfMedicine = cursor.getString(cursor.getColumnIndex(KEY_SYSTEM_OF_MEDICINE));
                    String pincode = cursor.getString(cursor.getColumnIndex(KEY_PINCODE));
                    String specialization = cursor.getString(cursor.getColumnIndex(KEY_Specializations));
                    String services = cursor.getString(cursor.getColumnIndex(KEY_SERVICES));
                    long hospId = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ROW_ID)));
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
        read += "select * from " + TABLE_NAME + " where _id =" + rowId;
        open();

        Cursor cursor = db.rawQuery(read, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    String hospitalName = cursor.getString(cursor.getColumnIndex(KEY_HOSPITAL_PRIVATE));
//                    String timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP));
                    String state = cursor.getString(cursor.getColumnIndex(KEY_STATE));
                    String city = cursor.getString(cursor.getColumnIndex(KEY_CITY));
                    String category = cursor.getString(cursor.getColumnIndex(KEY_category));
                    String website = cursor.getString(cursor.getColumnIndex(KEY_website));
                    String contact = cursor.getString(cursor.getColumnIndex(KEY_contact));

                    String email = cursor.getString(cursor.getColumnIndex(KEY_email));
                    String systemsOfMedicine = cursor.getString(cursor.getColumnIndex(KEY_SYSTEM_OF_MEDICINE));
                    String pincode = cursor.getString(cursor.getColumnIndex(KEY_PINCODE));
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
