package helper;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();
// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
// Database Name
	private static final String DATABASE_NAME = "tellr";
	// Login table name
	private static final String TABLE_LOGIN = "login";
	private static final String TABLE_ATMS = "atms";
	private static final String TABLE_BANKS = "banks";
// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_PHONE = "name";
	private static final String KEY_UID = "u_id";

	private static final String KEY_AID = "id";
	private static final String KEY_ATMID = "atm_id";
	private static final String KEY_ANAME= "atm";
	private static final String KEY_BANK_ID= "bank_id";
	private static final String KEY_ATOWN= "town";
	private static final String KEY_ALAT= "alat";
	private static final String KEY_ALONG= "along";
	private static final String KEY_VISA= "visa";
	private static final String KEY_MASTERCARD= "mastercard";
	private static final String KEY_AMXPRESS= "americanxpress";
	private static final String KEY_CIRRUS= "cirrus";

	private static final String KEY_BID= "id";
	private static final String KEY_BANKID= "bank_id";
	private static final String KEY_BANK= "bank";
	private static final String KEY_ADDRESS= "address";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," 
				+ KEY_EMAIL + " TEXT,"
				+ KEY_PHONE + " TEXT,"
				+ KEY_UID + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);

		String CREATE_ATM_TABLE = "CREATE TABLE " + TABLE_ATMS + "("
				+ KEY_AID + " INTEGER PRIMARY KEY,"
				+ KEY_ATMID + " INTEGER,"
				+ KEY_ANAME + " TEXT,"
				+ KEY_BANK_ID + " INTEGER,"
				+ KEY_ATOWN + " TEXT,"
				+ KEY_ALAT + " DOUBLE,"
				+ KEY_ALONG + " DOUBLE,"
				+ KEY_VISA + " INTEGER,"
				+ KEY_MASTERCARD + " INTEGER,"
				+ KEY_AMXPRESS + " INTEGER,"
				+ KEY_CIRRUS + " INTEGER" + ")";
		db.execSQL(CREATE_ATM_TABLE);

		String CREATE_TABLE_BANKS = "CREATE TABLE " + TABLE_BANKS + "("
				+ KEY_BID + " INTEGER PRIMARY KEY,"
				+ KEY_BANKID + " INTEGER,"
				+ KEY_BANK + " TEXT,"
				+ KEY_ADDRESS + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_BANKS);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser( String email, String u_id, String phone) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_EMAIL, email);
		values.put(KEY_PHONE, phone);
		values.put(KEY_UID, u_id);
		// Inserting Row
		long id = db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection
	 	Log.d("get_user", "user created: " + id + " " + email + " " + phone + " " +u_id);
	}

	public void addATM( String atmid, String atm, String bank_id, String town, String alat, String along, String visa, String _master, String amxpress, String cirrus) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ATMID, atmid);
		values.put(KEY_ANAME, atm);
		values.put(KEY_BANK_ID, bank_id);
		values.put(KEY_ATOWN, town);
		values.put(KEY_ALAT, alat);
		values.put(KEY_ALONG, along);
		values.put(KEY_VISA, visa);
		values.put(KEY_MASTERCARD, _master);
		values.put(KEY_AMXPRESS, amxpress);
		values.put(KEY_CIRRUS, cirrus);
		// Inserting Row
		long id = db.insert(TABLE_ATMS, null, values);
		db.close(); // Closing database connection
		Log.d(TAG, "New atm added: " + id);
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails(String tag) {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("email", cursor.getString(1));
			user.put("name", cursor.getString(2));
			user.put("u_id", cursor.getString(3));
		
		}
		cursor.close();
		db.close();
		// return user
		Log.d("get_user", tag+" Fetching user data: " + user.toString());

		return user;
	}

	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();

		Log.d("get_user", "We are starting over");
	}

	private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}

		return Math.round(dist);
	}
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}
