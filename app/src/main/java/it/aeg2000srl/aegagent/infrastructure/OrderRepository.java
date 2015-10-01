package it.aeg2000srl.aegagent.infrastructure;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.ArrayList;
import java.util.Date;
import it.aeg2000srl.aegagent.core.IOrderRepository;
import it.aeg2000srl.aegagent.core.Order;

/**
 * Created by tiziano.michelessi on 01/10/2015.
 */
public class OrderRepository implements IOrderRepository {
    DbHelper _db;

    public OrderRepository(DbHelper db) {
        _db = db;
    }

    public OrderRepository(Context context) {
        _db = new DbHelper(context);
    }

    protected String getSelect() {
        return String.format("SELECT %s, %s, %s, %s, %s, %s FROM %s ", DbHelper.OrdersTable._ID, DbHelper.OrdersTable.CUSTOMER_ID, DbHelper.OrdersTable.CREATION_DATE,
                DbHelper.OrdersTable.USER_ID, DbHelper.OrdersTable.NOTES, DbHelper.OrdersTable.SENT_DATE, DbHelper.OrdersTable.TABLENAME);
    }

    protected Order make(Cursor c) {
        Order o = new Order();
        o.setId(c.getLong(0));
        o.setCustomerId(c.getLong(1));
        o.setCreationDate(new Date(c.getInt(2)));
        o.setUserId(c.getLong(3));
        o.setNotes(c.getString(4));
        o.setSentDate( new Date(c.getInt(5)) );

        return o;
    }

    @Override
    public Iterable<Order> findByCustomerId(long customer_id) {
        ArrayList<Order> orders = new ArrayList<>();
        String sql = getSelect() + " WHERE " + DbHelper.OrdersTable.CUSTOMER_ID + "=" + customer_id + ";";
        Cursor crs = _db.getReadableDatabase().rawQuery(sql, null);

        crs.moveToFirst();
        if (! crs.isAfterLast()) {
            do {
                Order o = make(crs);
                orders.add(o);
            } while (crs.moveToNext());
        }

        crs.close();

        return orders;
    }

    @Override
    public Order getById(long id) {
        Order o = null;
        String sql = getSelect() + " WHERE " + DbHelper.OrdersTable._ID + "=" + id + ";";
        Cursor crs = _db.getReadableDatabase().rawQuery(sql, null);

        crs.moveToFirst();
        if (! crs.isAfterLast()) {
            o = make(crs);
        }
        crs.close();

        return o;
    }

    @Override
    public long add(Order order) throws SQLiteException {
        long newId = 0;
        _db.getWritableDatabase().beginTransaction();

        try {
            newId = insert(order);
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
            return newId;
        } catch (SQLiteException e) {
            _db.getWritableDatabase().endTransaction();
            throw e;
        }
    }

    protected long insert(Order order) {
        String sql = "INSERT INTO " + DbHelper.OrdersTable.TABLENAME + " (" +
                DbHelper.OrdersTable.CUSTOMER_ID + "," +
                DbHelper.OrdersTable.CREATION_DATE + "," +
                DbHelper.OrdersTable.USER_ID + "," +
                DbHelper.OrdersTable.NOTES + "," +
                DbHelper.OrdersTable.SENT_DATE + ") " +
                " VALUES (?, ?, ?, ?, ?)";

        SQLiteStatement stmt = _db.getWritableDatabase().compileStatement(sql);
        stmt.bindLong(1, order.getCustomerId());
        stmt.bindLong(2, order.getCreationDate().getTime());
        stmt.bindLong(3, order.getUserId());
        stmt.bindString(4, order.getNotes() != null ? order.getNotes() : "");
        stmt.bindLong(5, order.getSentDate() != null ? order.getSentDate().getTime() : 0);

        return stmt.executeInsert();
    }

    @Override
    public void edit(Order order) throws SQLiteException{
        _db.getWritableDatabase().beginTransaction();

        try {
            update(order);
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
        } catch (SQLiteException e) {
            _db.getWritableDatabase().endTransaction();
            throw e;
        }
    }

    protected void update(Order order) {
        String sql = "UPDATE " + DbHelper.OrdersTable.TABLENAME + " SET " +
                DbHelper.OrdersTable.CUSTOMER_ID + "=?," +
                DbHelper.OrdersTable.CREATION_DATE + "=?," +
                DbHelper.OrdersTable.USER_ID + "=?," +
                DbHelper.OrdersTable.NOTES + "=?" +
                DbHelper.OrdersTable.SENT_DATE + "=?" +
                " WHERE " + DbHelper.OrdersTable._ID + "=?";

        SQLiteStatement stmt = _db.getWritableDatabase().compileStatement(sql);

        stmt.bindLong(1, order.getCustomerId());
        stmt.bindLong(2, order.getCreationDate().getTime());
        stmt.bindLong(3, order.getUserId());
        stmt.bindString(4, order.getNotes());
        stmt.bindLong(5, order.getSentDate() != null ? order.getSentDate().getTime() : 0);

        stmt.executeUpdateDelete();
    }

    @Override
    public void remove(Order order) {
        _db.getWritableDatabase().beginTransaction();

        try {
            delete(order);
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
        } catch (SQLiteException exc) {
            _db.getWritableDatabase().endTransaction();
            throw exc;
        }
    }

    protected void delete(Order order) {
        String sql = "DELETE FROM " + DbHelper.OrdersTable.TABLENAME + " WHERE " + DbHelper.OrdersTable._ID + "=?";
        SQLiteStatement stmt = _db.getWritableDatabase().compileStatement(sql);
        stmt.bindLong(1, order.getId());
        stmt.executeUpdateDelete();
    }

    @Override
    public ArrayList<Order> getAll() {
        ArrayList<Order> orders = new ArrayList<>();

        Cursor crs = _db.getReadableDatabase().rawQuery(getSelect(), null);
        crs.moveToFirst();

        if (! crs.isAfterLast()) {
            do {
                Order o = make(crs);
                orders.add(o);
            } while (crs.moveToNext());
        }
        crs.close();

        return orders;
    }

    @Override
    public long size() {
        long cnt = -1;
        try {
            cnt = DatabaseUtils.queryNumEntries(_db.getReadableDatabase(), DbHelper.OrdersTable.TABLENAME);
        } catch (SQLiteException e) {
            Log.e(getClass().getCanonicalName() + ".size()", e.toString());
        }
        return cnt;
    }

    @Override
    public void addAll(Iterable<Order> items) {
        try {
            _db.getWritableDatabase().beginTransaction();

            for (Order o : items) {
                insert(o);
                update(o);
            }

            _db.getWritableDatabase().setTransactionSuccessful();
        } catch (SQLiteException exc) {
            Log.e("Error", "Error inserting all orders: " + exc.toString());
            throw exc;
        }
        finally {
            _db.getWritableDatabase().endTransaction();
        }
    }

    @Override
    protected void finalize() {
        _db.close();
    }
}
