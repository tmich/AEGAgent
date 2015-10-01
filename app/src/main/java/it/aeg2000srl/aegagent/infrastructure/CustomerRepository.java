package it.aeg2000srl.aegagent.infrastructure;

/**
 * Created by Tiziano on 24/09/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.ICustomerRepository;

public class CustomerRepository implements ICustomerRepository {
    DbHelper _db;

    protected String getSelect() {
        return String.format("SELECT %s,%s,%s,%s,%s,%s,%s,%s,%s from %s order by %s", DbHelper.CustomersTable._ID,
                DbHelper.CustomersTable.CODE, DbHelper.CustomersTable.NAME,
                DbHelper.CustomersTable.ADDRESS, DbHelper.CustomersTable.CAP,
                DbHelper.CustomersTable.CITY, DbHelper.CustomersTable.PROV,
                DbHelper.CustomersTable.TEL, DbHelper.CustomersTable.IVA,
                DbHelper.CustomersTable.TABLENAME, DbHelper.CustomersTable.NAME);
    }

    public CustomerRepository(DbHelper db) {
        _db = db;
    }

    public CustomerRepository(Context context) {
        _db = new DbHelper(context);
    }

    protected ContentValues toRaw(Customer cust) {
        ContentValues cv = new ContentValues();

        cv.put(DbHelper.CustomersTable._ID, cust.getId());
        cv.put(DbHelper.CustomersTable.CODE, cust.getCode());
        cv.put(DbHelper.CustomersTable.NAME, cust.getName());
        cv.put(DbHelper.CustomersTable.ADDRESS, cust.getAddress());
        cv.put(DbHelper.CustomersTable.CAP, cust.getCap());
        cv.put(DbHelper.CustomersTable.CITY, cust.getCity());
        cv.put(DbHelper.CustomersTable.PROV, cust.getProv());
        cv.put(DbHelper.CustomersTable.TEL, cust.getTelephone());
        cv.put(DbHelper.CustomersTable.IVA, cust.getVatNumber());

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
            DatabaseUtils.queryNumEntries(_db.getReadableDatabase(), DbHelper.CustomersTable.TABLENAME);
        } catch (SQLiteException e) {
            Log.e(getClass().getCanonicalName() + ".size()", e.toString());
        }

        return cnt;
    }

    @Override
    public void addAll(Iterable<Customer> items) throws SQLiteException {
        try {
            _db.getWritableDatabase().beginTransaction();

            for (Customer c : items) {
                insert(c, true);
                update(c);
            }

            _db.getWritableDatabase().setTransactionSuccessful();
        } catch (SQLiteException exc) {
            Log.e("Error", "Error inserting all customers: " + exc.toString());
            throw exc;
        }
        finally {
            _db.getWritableDatabase().endTransaction();
        }
    }

    @Override
    public Customer getByCode(String code) {
        Customer c = null;

        try {
            Cursor crs = _db.getReadableDatabase().query(
                    DbHelper.CustomersTable.TABLENAME,
                    DbHelper.CustomersTable.getColumnNames(),
                    DbHelper.CustomersTable.CODE + "=?",
                    new String[]{code},
                    null, null, null);

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
    public Customer getById(long id) {
        Customer c = null;

        try {
            Cursor crs = _db.getReadableDatabase().query(
                    DbHelper.CustomersTable.TABLENAME,
                    DbHelper.CustomersTable.getColumnNames(),
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
    public long add(Customer customer) throws SQLiteException{
        long newId = 0;
        _db.getWritableDatabase().beginTransaction();

        try {
            newId = insert(customer, true);
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
            return newId;
        } catch (SQLiteException e) {
            _db.getWritableDatabase().endTransaction();
            throw e;
        }
    }

    protected long insert(Customer customer, boolean ignore)
    {
        String ins_sql = "INSERT " + (ignore ? "OR IGNORE" : "") + " INTO " +
                DbHelper.CustomersTable.TABLENAME + " (" +
                DbHelper.CustomersTable.CODE + "," +
                DbHelper.CustomersTable.NAME + "," +
                DbHelper.CustomersTable.ADDRESS + "," +
                DbHelper.CustomersTable.CAP + "," +
                DbHelper.CustomersTable.CITY + "," +
                DbHelper.CustomersTable.TEL + "," +
                DbHelper.CustomersTable.IVA + ")" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement ins_stmt = _db.getWritableDatabase().compileStatement(ins_sql);

        ins_stmt.bindString(1, customer.getCode());
        ins_stmt.bindString(2, customer.getName());
        ins_stmt.bindString(3, customer.getAddress());
        ins_stmt.bindString(4, customer.getCap());
        ins_stmt.bindString(5, customer.getCity());
        ins_stmt.bindString(6, customer.getTelephone());
        ins_stmt.bindString(7, customer.getVatNumber());

        return ins_stmt.executeInsert();
    }

    @Override
    public void edit(Customer customer) throws SQLiteException{
        _db.getWritableDatabase().beginTransaction();

        try {
            update(customer);
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
        } catch (SQLiteException e) {
            _db.getWritableDatabase().endTransaction();
            throw e;
        }
    }

    protected void update(Customer c) {
        String upd_sql = "UPDATE " + DbHelper.CustomersTable.TABLENAME + " SET " +
                DbHelper.CustomersTable.CODE + "=?," +
                DbHelper.CustomersTable.NAME + "=?," +
                DbHelper.CustomersTable.ADDRESS + "=?," +
                DbHelper.CustomersTable.CAP + "=?," +
                DbHelper.CustomersTable.CITY + "=?," +
                DbHelper.CustomersTable.TEL + "=?," +
                DbHelper.CustomersTable.IVA + "=?" +
                " WHERE " + DbHelper.CustomersTable.CODE + "=?";

        SQLiteStatement upd_stmt = _db.getWritableDatabase().compileStatement(upd_sql);

        upd_stmt.bindString(1, c.getCode());
        upd_stmt.bindString(2, c.getName());
        upd_stmt.bindString(3, c.getAddress());
        upd_stmt.bindString(4, c.getCap());
        upd_stmt.bindString(5, c.getCity());
        upd_stmt.bindString(6, c.getTelephone());
        upd_stmt.bindString(7, c.getVatNumber());
        upd_stmt.bindString(8, c.getCode());

        upd_stmt.executeUpdateDelete();
    }

    @Override
    public void remove(Customer customer) throws SQLiteException{
        _db.getWritableDatabase().beginTransaction();

        try {
            delete(customer);
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
        } catch (SQLiteException exc) {
            _db.getWritableDatabase().endTransaction();
            throw exc;
        }
    }

    protected void delete(Customer c) {
        String del_sql = "DELETE FROM " + DbHelper.CustomersTable.TABLENAME + " WHERE " + DbHelper.CustomersTable._ID + "=?";
        SQLiteStatement del = _db.getWritableDatabase().compileStatement(del_sql);
        del.bindLong(1, c.getId());
        del.executeUpdateDelete();
    }

    @Override
    public ArrayList<Customer> getAll() throws SQLiteException{
        ArrayList<Customer> customers = new ArrayList<>();

        Cursor crs = _db.getReadableDatabase().rawQuery(getSelect(), null);
        crs.moveToFirst();

        if (! crs.isAfterLast()) {
            do {
                Customer c = make(crs);
                customers.add(c);
            } while (crs.moveToNext());
        }
        crs.close();

        return customers;
    }

    @Override
    public ArrayList<Customer> findByName(String name) throws SQLiteException {
        ArrayList<Customer> customers = new ArrayList<>();

        String select = getSelect() + " WHERE " + DbHelper.CustomersTable.NAME + " LIKE " + DatabaseUtils.sqlEscapeString("%" + name + "%") + "";
        Cursor crs = _db.getReadableDatabase().rawQuery(select, null);

        crs.moveToFirst();
        if (! crs.isAfterLast()) {
            do {
                Customer c = make(crs);
                customers.add(c);
            } while (crs.moveToNext());
        }

        crs.close();

        return customers;
    }

    @Override
    protected void finalize() {
        _db.close();
    }
}
