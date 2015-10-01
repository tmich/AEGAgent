package it.aeg2000srl.aegagent.infrastructure;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeoutException;

import it.aeg2000srl.aegagent.core.Order;
import it.aeg2000srl.aegagent.mvp.OrderDetailsActivity;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "agent.db";
    private Context context;

    /** customers table **/
    public static class CustomersTable implements BaseColumns {
        public static final String TABLENAME = "customers";

        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String CAP = "cap";
        public static final String CITY = "city";
        public static final String PROV = "prov";
        public static final String TEL = "tel";
        public static final String IVA = "iva";

        protected static final String[] _COL_NAMES = {_ID, CODE, NAME, ADDRESS,
                CAP, CITY, PROV, TEL, IVA};

        public static final String _CREATE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
                TABLENAME, _ID, CODE, NAME, ADDRESS, CAP, CITY, PROV, TEL, IVA);
        public static final String _DROP = String.format("DROP TABLE IF EXISTS %s;", TABLENAME);

        public static String[] getColumnNames() {
            return _COL_NAMES;
        }
    }

    /** END - customers table **/

    /** products table **/
    public static class ProductsTable implements BaseColumns {
        public static final String TABLENAME = "products";

        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String PRICE = "price";

        protected static final String[] _COL_NAMES = {_ID, CODE, NAME, PRICE};
        public static final String _CREATE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s DOUBLE);",
                TABLENAME, _ID, CODE, NAME, PRICE);
        public static final String _DROP = String.format("DROP TABLE IF EXISTS %s;", TABLENAME);

        public static String[] getColumnNames() {
            return _COL_NAMES;
        }
    }
    /** END - products table **/

    /** orders table **/
    public static class OrdersTable implements BaseColumns {
        public static final String TABLENAME = "orders";

        public static final String CUSTOMER_ID = "customer_id";
        public static final String CREATION_DATE = "creation_date";
        public static final String USER_ID = "user_id";
        public static final String NOTES = "notes";
        public static final String SENT_DATE = "sent_date";

        protected static final String[] _COL_NAMES = {_ID, CUSTOMER_ID, CREATION_DATE, USER_ID, NOTES, SENT_DATE};

        public static final String _CREATE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, " +
                "%s INTEGER NOT NULL REFERENCES %s(%s), " +
                "%s INTEGER, " +
                "%s INTEGER NOT NULL REFERENCES %s(%s), " +
                "%s TEXT, %s INTEGER);", TABLENAME, _ID,
                CUSTOMER_ID, CustomersTable.TABLENAME, CustomersTable._ID,
                CREATION_DATE,
                USER_ID, UsersTable.TABLENAME, UsersTable._ID,
                NOTES, SENT_DATE);

        public static final String _DROP = String.format("DROP TABLE IF EXISTS %s;", TABLENAME);

        public static String[] getColumnNames() {
            return _COL_NAMES;
        }
    }
    /** END - orders table **/

    /** order items table **/
    public static class OrderItemsTable implements BaseColumns {
        public static final String TABLENAME = "order_items";

        public static final String ORDER_ID = "order_id";
        public static final String QTY = "quantity";
        public static final String DISCOUNT = "discount";
        public static final String NOTES = "notes";

        protected static final String[] _COL_NAMES = {_ID, ORDER_ID, QTY, DISCOUNT, NOTES};

        public static final String _CREATE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, " +
                "%s INTEGER NOT NULL REFERENCES %s(%s), " +
                "%s INTEGER DEFAULT 0, " +
                "%s TEXT, " +
                "%s TEXT); ", TABLENAME, _ID,
                ORDER_ID, OrdersTable.TABLENAME, OrdersTable._ID,
                QTY,
                DISCOUNT,
                NOTES);

        public static final String _DROP = String.format("DROP TABLE IF EXISTS %s;", TABLENAME);

        public static String[] getColumnNames() {
            return _COL_NAMES;
        }
    }
    /** END - order items table **/

    /** users table **/
    public static class UsersTable implements BaseColumns {
        public static final String TABLENAME = "users";

        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String LAST_LOGIN_DATE = "last_login_date";
        public static final String ACTIVE = "active";

        protected static final String[] _COL_NAMES = { _ID, USERNAME, PASSWORD, LAST_LOGIN_DATE, ACTIVE };

        public static final String _CREATE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, " +
                "%s TEXT NOT NULL, " +
                "%s TEXT NOT NULL, " +
                "%s INTEGER, " +
                "%s BIT DEFAULT 1); ", TABLENAME, _ID,
                USERNAME, PASSWORD, LAST_LOGIN_DATE, ACTIVE);

        public static final String _DROP = String.format("DROP TABLE IF EXISTS %s;", TABLENAME);

        public static String[] getColumnNames() {
            return _COL_NAMES;
        }
    }
    /** END - users table **/

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
        db.execSQL(UsersTable._CREATE);
        db.execSQL(OrdersTable._CREATE);
        db.execSQL(OrderItemsTable._CREATE);

        // unique indexes creations
        db.execSQL("CREATE UNIQUE INDEX idx_cust_unique_code ON " + CustomersTable.TABLENAME + "(" + CustomersTable.CODE + ");");
        db.execSQL("CREATE UNIQUE INDEX idx_prod_unique_code ON " + ProductsTable.TABLENAME + "(" + ProductsTable.CODE + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // tables deletion
        db.execSQL(OrderItemsTable._DROP);
        db.execSQL(OrdersTable._DROP);
        db.execSQL(UsersTable._DROP);
        db.execSQL(CustomersTable._DROP);
        db.execSQL(ProductsTable._DROP);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}