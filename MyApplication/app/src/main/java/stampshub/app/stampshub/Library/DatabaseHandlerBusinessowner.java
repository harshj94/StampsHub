package stampshub.app.stampshub.Library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by hp on 30-06-2015.
 */
public class DatabaseHandlerBusinessowner extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "stampshubdemo";

    // Login table name
    private static final String TABLE_LOGIN = "business_owner";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERTYPE = "usertype";
    private static final String KEY_bname = "business_name";
    private static final String KEY_bemail = "email_id";
    private static final String KEY_addr1 = "address1";
    private static final String KEY_addr2 = "address2";
    private static final String KEY_addr3 = "address3";
    private static final String KEY_bcountry = "country";
    private static final String KEY_bpostcode = "postcode";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    public DatabaseHandlerBusinessowner(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERTYPE+" TEXT,"
                + KEY_bname + " TEXT,"
                + KEY_bemail + " TEXT UNIQUE,"
                + KEY_addr1+ " TEXT,"
                + KEY_addr2+" TEXT,"
                + KEY_addr3+" TEXT,"
                + KEY_bcountry+" TEXT,"
                + KEY_bpostcode+" TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        // Create tables ag
        // ain
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void adduser(String utype,String business_name, String email_id, String address1, String address2,String address3,String country,String postcode,String uid,String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USERTYPE, utype); // User Type
        values.put(KEY_bname, business_name); // Business Name
        values.put(KEY_bemail, email_id); // Email
        values.put(KEY_addr1, address1); // Address1
        values.put(KEY_addr2, address2); // Address2
        values.put(KEY_addr3, address3); // Address3
        values.put(KEY_bcountry, country); // Country
        values.put(KEY_bpostcode, postcode); // Country
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }


    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("utype",cursor.getString(1));
            user.put("business_name", cursor.getString(2));
            user.put("email_id", cursor.getString(3));
            user.put("address1", cursor.getString(4));
            user.put("address2",cursor.getString(5));
            user.put("address3",cursor.getString(6));
            user.put("country",cursor.getString(7));
            user.put("postcode",cursor.getString(8));
            user.put("uid",cursor.getString(9));
            user.put("created_at", cursor.getString(10));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
    /**
     * Getting user login status
     * return true if rows are there in table
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
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

}

