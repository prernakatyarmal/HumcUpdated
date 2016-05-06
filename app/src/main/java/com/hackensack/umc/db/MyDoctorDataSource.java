package com.hackensack.umc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hackensack.umc.datastructure.DoctorDetails;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhagyashree_kumawat on 10/20/2015.
 */
public class MyDoctorDataSource {
    private SQLiteDatabase database;
    private HackenSackSqliteHelper sqliteHelper;
    private String[] allColumns = {HackenSackSqliteHelper.COLUMN_ID
            , HackenSackSqliteHelper.COLUMN_NAME
            , HackenSackSqliteHelper.COLUMN_SPECIALITY, HackenSackSqliteHelper.COLUMN_ADDRESS,
            HackenSackSqliteHelper.COLUMN_PHONE,
            HackenSackSqliteHelper.COLUMN_DEGREE, HackenSackSqliteHelper.COLUMN_HUMCID
            , HackenSackSqliteHelper.COLUMN_IMAGE_URL, HackenSackSqliteHelper.COLUMN_GENDER,
            HackenSackSqliteHelper.COLUMN_NPI};
    private String[] allAddressColumns = {HackenSackSqliteHelper.COLUMN_HUMCID
            , HackenSackSqliteHelper.COLUMN_STATE
            , HackenSackSqliteHelper.COLUMN_SUITE ,HackenSackSqliteHelper.COLUMN_STREET, HackenSackSqliteHelper.COLUMN_ADDR_PHONE, HackenSackSqliteHelper.COLUMN_ZIP};


    public MyDoctorDataSource(Context context) {
        sqliteHelper = HackenSackSqliteHelper.getSqliteHelperInstance(context);
    }

    public void open() throws SQLException {
        database = sqliteHelper.getWritableDatabase();
    }

    public void close() {
        sqliteHelper.close();
    }

    public void deleteMyDoctor(DoctorDetails doctorDetails) {
        String id = doctorDetails.getDoctorHumcId();
        if (database != null && database.isOpen())
            database.delete(HackenSackSqliteHelper.TABLE_MYDOCTOR, HackenSackSqliteHelper.COLUMN_HUMCID + " = '" + id + "'", null);

    }

    public int getMyDoctorCount() {
        int myDocCount = 0;
        String[] col = {HackenSackSqliteHelper.COLUMN_ID};
        if (database != null && database.isOpen()) {
            Cursor cursor = database.query(HackenSackSqliteHelper.TABLE_MYDOCTOR, col, null, null, null, null, null);
            myDocCount = cursor.getCount();
            cursor.close();
        }
        return myDocCount;
    }

    public boolean isFavoriteDoc(String humcId) {
        String[] col = {HackenSackSqliteHelper.COLUMN_ID};
        if (database != null && database.isOpen()) {
            Cursor cursor = database.query(HackenSackSqliteHelper.TABLE_MYDOCTOR, col, HackenSackSqliteHelper.COLUMN_HUMCID + " = '" + humcId + "'", null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }
        }
        return false;
    }

    public long addDoctorToFavorite(DoctorDetails doctorDetails) {
        long insertId;
        ContentValues contentValues = new ContentValues();
        contentValues.put(HackenSackSqliteHelper.COLUMN_NAME, doctorDetails.getDoctorFullName());
        contentValues.put(HackenSackSqliteHelper.COLUMN_SPECIALITY, doctorDetails.getDoctorSpeciality());
        contentValues.put(HackenSackSqliteHelper.COLUMN_ADDRESS, doctorDetails.getDoctorFirstAddress());
        contentValues.put(HackenSackSqliteHelper.COLUMN_PHONE, doctorDetails.getDoctorFirstPhone());
        contentValues.put(HackenSackSqliteHelper.COLUMN_DEGREE, doctorDetails.getDoctorDegree());
        contentValues.put(HackenSackSqliteHelper.COLUMN_HUMCID, doctorDetails.getDoctorHumcId());
        contentValues.put(HackenSackSqliteHelper.COLUMN_IMAGE_URL, doctorDetails.getDocImageUrl());
        contentValues.put(HackenSackSqliteHelper.COLUMN_GENDER, doctorDetails.getGender());
        contentValues.put(HackenSackSqliteHelper.COLUMN_NPI, doctorDetails.getDoctorNpi());
        if (doctorDetails.getAddress() != null)
            for (int i = 0; i < doctorDetails.getAddress().size(); i++) {
                DoctorDetails.Address address = doctorDetails.getAddress().get(i);
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put(HackenSackSqliteHelper.COLUMN_HUMCID, doctorDetails.getDoctorHumcId());
                contentValues1.put(HackenSackSqliteHelper.COLUMN_ADDR_PHONE, address.getPhone());
                contentValues1.put(HackenSackSqliteHelper.COLUMN_STREET, address.getStreet());
                contentValues1.put(HackenSackSqliteHelper.COLUMN_SUITE, address.getSuite());
                contentValues1.put(HackenSackSqliteHelper.COLUMN_STATE, address.getState());
                contentValues1.put(HackenSackSqliteHelper.COLUMN_ZIP, address.getZip());
                database.insert(HackenSackSqliteHelper.TABLE_ADDRESS, null, contentValues1);
            }
        insertId = database.insert(HackenSackSqliteHelper.TABLE_MYDOCTOR, null, contentValues);
        return insertId;
    }

    public List<DoctorDetails> getAllMyDoctors() {
        List<DoctorDetails> myDoctors = new ArrayList<DoctorDetails>();
        if (database != null && database.isOpen()) {
            Cursor cursor = database.query(HackenSackSqliteHelper.TABLE_MYDOCTOR,
                    allColumns, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DoctorDetails doctorDetails = cursorToMyDoc(cursor);
                doctorDetails.setDoctorAddress(getAllDoctorAddresses(doctorDetails));
                myDoctors.add(doctorDetails);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }

        return myDoctors;
    }

    public ArrayList<DoctorDetails.Address> getAllDoctorAddresses(DoctorDetails doctorDetails) {
        ArrayList<DoctorDetails.Address> mDoctorAddresses = new ArrayList<DoctorDetails.Address>();
        if (database != null && database.isOpen()) {
            Cursor cursor = database.query(HackenSackSqliteHelper.TABLE_ADDRESS,
                    allAddressColumns, HackenSackSqliteHelper.COLUMN_HUMCID + "=?", new String[]{doctorDetails.getDoctorHumcId()}, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DoctorDetails.Address address = cursorToDocAddress(cursor, doctorDetails);
                mDoctorAddresses.add(address);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }

        return mDoctorAddresses;
    }

    public void deleteOldestRecord() {
        if (database != null && database.isOpen()) {
            Cursor cursor = database.query(HackenSackSqliteHelper.TABLE_MYDOCTOR, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                String humcId = cursor.getString(cursor.getColumnIndex(HackenSackSqliteHelper.COLUMN_HUMCID));

                database.delete(HackenSackSqliteHelper.TABLE_MYDOCTOR, HackenSackSqliteHelper.COLUMN_HUMCID + "=?", new String[]{humcId});
                database.delete(HackenSackSqliteHelper.TABLE_ADDRESS, HackenSackSqliteHelper.COLUMN_HUMCID + "=?", new String[]{humcId});
            }
            cursor.close();
        }

    }

    private DoctorDetails cursorToMyDoc(Cursor cursor) {
        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setDbEntryId(cursor.getInt(0));
        doctorDetails.setDoctorFullName(cursor.getString(1));
        doctorDetails.setDoctorSpeciality(cursor.getString(2));
        doctorDetails.setmFirstAddress(cursor.getString(3));
        doctorDetails.setmPhoneNum(cursor.getString(4));
        doctorDetails.setDoctorDegree(cursor.getString(5));
        doctorDetails.setDoctorHumcId(cursor.getString(6));
        doctorDetails.setmDocImageUrl(cursor.getString(7));
        doctorDetails.setGender(cursor.getString(8));
        doctorDetails.setDoctorNpi(cursor.getString(9));
        return doctorDetails;
    }

    private DoctorDetails.Address cursorToDocAddress(Cursor cursor, DoctorDetails doctorDetails) {
        DoctorDetails.Address address = doctorDetails.new Address();
        address.setmState(cursor.getString(1));
        address.setmSuite(cursor.getString(2));
        address.setStreet(cursor.getString(3));
        address.setmPhone(cursor.getString(4));
        address.setmZip(cursor.getString(5));
        return address;
    }


}
