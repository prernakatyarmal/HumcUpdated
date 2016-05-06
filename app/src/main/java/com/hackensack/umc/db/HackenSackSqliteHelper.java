package com.hackensack.umc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bhagyashree_kumawat on 10/20/2015.
 */
public class HackenSackSqliteHelper extends SQLiteOpenHelper {

    public static final String TABLE_MYDOCTOR = "myDoctor";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SPECIALITY = "speciality";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_DEGREE = "degree";
    public static final String COLUMN_HUMCID = "humcID";
    public static final String COLUMN_IMAGE_URL = "imageurl";
    public static final String COLUMN_NPI = "npi";
    public static final String COLUMN_GENDER = "gender";

    public static final String TABLE_ADDRESS = "addressTable";

    public static final String COLUMN_ADDR_PHONE = "mPhone";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_SUITE = "suite";
    public static final String COLUMN_ZIP = "zip";
    public static final String COLUMN_STREET = "street";


    private static final String DATABASE_NAME = "hackenSack.db";
    private static final int DATABASE_VERSION = 1;

    private static HackenSackSqliteHelper sqliteHelper;
    private static final String CREATE_TABLE_DOCTOR = "create table "
            + TABLE_MYDOCTOR + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_SPECIALITY + " text not null, " + COLUMN_ADDRESS + " text, " + COLUMN_PHONE + " text, " +
            COLUMN_DEGREE + " text, " + COLUMN_HUMCID + " text, " + COLUMN_IMAGE_URL + " text, " + COLUMN_GENDER + " text, " + COLUMN_NPI + " text);";
    private static final String CREATE_TABLE_ADDRESS = "create table " + TABLE_ADDRESS + " (" + COLUMN_HUMCID
            + " text," +COLUMN_STREET+" text, "+ COLUMN_SUITE + " text," + COLUMN_ADDR_PHONE + " text, " +
            COLUMN_STATE + " text, " + COLUMN_ZIP +" text);";


    private HackenSackSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized HackenSackSqliteHelper getSqliteHelperInstance(Context context) {
        if (sqliteHelper == null)
            sqliteHelper = new HackenSackSqliteHelper(context);
        return sqliteHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DOCTOR);
        db.execSQL(CREATE_TABLE_ADDRESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYDOCTOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        onCreate(db);

    }
}
