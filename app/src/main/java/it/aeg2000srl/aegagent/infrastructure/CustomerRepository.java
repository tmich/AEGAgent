package it.aeg2000srl.aegagent.infrastructure;

/**
 * Created by Tiziano on 24/09/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.ICustomerRepository;

public class CustomerRepository implements ICustomerRepository {
    DbHelper _db;

    public CustomerRepository(DbHelper db) {
        _db = db;
    }

    public CustomerRepository(Context context) {
        _db = new DbHelper(context);
    }

    protected ContentValues toRaw(Customer cust) {
        ContentValues cv = new ContentValues();

        cv.put(DbHelper.CustomersTable._ID, cust.getId());
        cv.put(DbHelper.CustomersTable.COL_CODE, cust.getCode());
        cv.put(DbHelper.CustomersTable.COL_NAME, cust.getName());
        cv.put(DbHelper.CustomersTable.COL_ADDRESS, cust.getAddress());
        cv.put(DbHelper.CustomersTable.COL_CAP, cust.getCap());
        cv.put(DbHelper.CustomersTable.COL_CITY, cust.getCity());
        cv.put(DbHelper.CustomersTable.COL_PROV, cust.getProv());
        cv.put(DbHelper.CustomersTable.COL_TEL, cust.getTelephone());
        cv.put(DbHelper.CustomersTable.COL_IVA, cust.getVatNumber());

        return cv;
    }

    protected Customer make(Cursor crs) {
        Customer c = new Customer();
        c.setId(crs.getLong(0));
        c.setCode(crs.getString(1));
        c.setName(crs.getString(2));
        c.setAddress(crs.getString(3));
        c.setCap(crs.getString(4));
        c.setCity(crs.getString(5));
        c.setProv(crs.getString(6));
        c.setTelephone(crs.getString(7));
        c.setVatNumber(crs.getString(8));

        return c;
    }

    public long size() {
        long cnt = -1;

        try {
            cnt = DatabaseUtils.queryNumEntries(_db.getReadableDatabase(), DbHelper.CustomersTable.TABLENAME);
        } catch (SQLiteException exc) {
            //
        }

        return cnt;
    }

    @Override
    public Customer getById(long id) {
        Customer c = null;

        try {
            Cursor crs = _db.getReadableDatabase().query(
                    DbHelper.CustomersTable.TABLENAME,
                    DbHelper.CustomersTable._COL_NAMES,
                    DbHelper.CustomersTable._ID + "=?",
                    new String[]{String.valueOf(id)},
                    null, null, null, "1");

            if (!crs.isAfterLast()) {
                crs.moveToFirst();
                c = make(crs);
                crs.close();
            }

        } catch (SQLiteException sqlexc) {
            return null;
        }

        return c;
    }

    @Override
    public long add(Customer customer) {
        ContentValues raw_data = toRaw(customer);
        raw_data.remove(DbHelper.CustomersTable._ID);
        try {
            long id = _db.getWritableDatabase().insertOrThrow(DbHelper.CustomersTable.TABLENAME, null, raw_data);
            return id;
        } catch (SQLiteException exc) {
            Log.e("Error", "Error inserting customer: " + exc.toString());
            return 0;
        }
    }

    @Override
    public void edit(Customer customer) {
        ContentValues raw_data = toRaw(customer);
        try {
            _db.getWritableDatabase().update(DbHelper.CustomersTable.TABLENAME, raw_data,
                    DbHelper.CustomersTable._ID + "=?",
                    new String[] {String.valueOf(customer.getId())} );
        } catch (SQLiteException exc) {
            Log.e("Error updating customer", exc.toString());
        }
    }

    @Override
    public void remove(Customer customer) {
        try {
            _db.getWritableDatabase().delete(DbHelper.CustomersTable.TABLENAME,
                    DbHelper.CustomersTable._ID + "=?",
                    new String[] {String.valueOf(customer.getId())} );
        } catch (SQLiteException exc) {
            Log.e("Error deleting customer", exc.toString());
        }
    }

    @Override
    public ArrayList<Customer> getAll() {
        ArrayList<Customer> customers = null;

        try {
            Cursor crs = _db.getReadableDatabase().query(
                    DbHelper.CustomersTable.TABLENAME,
                    DbHelper.CustomersTable._COL_NAMES,
                    null, null,null, null, null);

            crs.moveToFirst();

            if (! crs.isAfterLast()) {
                customers = new ArrayList<>();

                do {
                    Customer c = make(crs);
                    customers.add(c);
                } while (crs.moveToNext());
            }

            crs.close();

        } catch (SQLiteException sqlexc) {
            return null;
        }

        return customers;
    }

    @Override
    public ArrayList<Customer> findByName(String name) {
        ArrayList<Customer> customers = null;

        try {
            Cursor crs = _db.getReadableDatabase().query(
                    DbHelper.CustomersTable.TABLENAME,
                    DbHelper.CustomersTable._COL_NAMES,
                    DbHelper.CustomersTable.COL_NAME + " like '%?%' ",
                    new String[] {name},
                    null, null, null);

            crs.moveToFirst();

            if (! crs.isAfterLast()) {
                customers = new ArrayList<>();

                do {
                    Customer c = make(crs);
                    customers.add(c);
                } while (crs.moveToNext());
            }

            crs.close();

        } catch (SQLiteException sqlexc) {
            return null;
        }

        return customers;
    }
}
