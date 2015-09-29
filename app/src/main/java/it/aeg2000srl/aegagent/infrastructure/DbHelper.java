package it.aeg2000srl.aegagent.infrastructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.Product;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "agent.db";

    /** customers table **/
    public static class CustomersTable implements BaseColumns {
        public static final String TABLENAME = "customers";

        public static final String COL_CODE = "code";
        public static final String COL_NAME = "name";
        public static final String COL_ADDRESS = "address";
        public static final String COL_CAP = "cap";
        public static final String COL_CITY = "city";
        public static final String COL_PROV = "prov";
        public static final String COL_TEL = "tel";
        public static final String COL_IVA = "iva";

        public static final String[] _COL_NAMES = {_ID, COL_CODE, COL_NAME, COL_ADDRESS,
                COL_CAP, COL_CITY, COL_PROV, COL_TEL, COL_IVA};

        public static final String _CREATE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
                TABLENAME, _ID, COL_CODE, COL_NAME, COL_ADDRESS, COL_CAP, COL_CITY, COL_PROV, COL_TEL, COL_IVA);
        public static final String _DROP = String.format("DROP TABLE IF EXISTS %s;", TABLENAME);
    }

    /** END - customers table **/

    /** products table **/
    public static class ProductsTable implements BaseColumns {
        public static final String TABLENAME = "products";

        public static final String COL_CODE = "code";
        public static final String COL_NAME = "name";
        public static final String COL_PRICE = "price";

        public static final String[] _COL_NAMES = {_ID, COL_CODE, COL_NAME, COL_PRICE};
        public static final String _CREATE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s DOUBLE);",
                TABLENAME, _ID, COL_CODE, COL_NAME, COL_PRICE);
        public static final String _DROP = String.format("DROP TABLE IF EXISTS %s;", TABLENAME);
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DbHelper(Context context, String db_name) {
        super(context, db_name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // tables creation
        db.execSQL(CustomersTable._CREATE);
        db.execSQL(ProductsTable._CREATE);

        // unique indexes creations
        db.execSQL("CREATE UNIQUE INDEX idx_cust_unique_code ON " + CustomersTable.TABLENAME + "(" + CustomersTable.COL_CODE + ");");
        db.execSQL("CREATE UNIQUE INDEX idx_prod_unique_code ON " + ProductsTable.TABLENAME + "(" + ProductsTable.COL_CODE + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // tables deletion
        db.execSQL(CustomersTable._DROP);
        db.execSQL(ProductsTable._DROP);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}