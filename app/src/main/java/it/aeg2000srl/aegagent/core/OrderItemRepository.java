package it.aeg2000srl.aegagent.core;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.infrastructure.DbHelper;

/**
 * Created by tiziano.michelessi on 01/10/2015.
 */
public class OrderItemRepository implements IOrderItemRepository {
    DbHelper _db;

    public OrderItemRepository(DbHelper db) {
        _db = db;
    }

    public OrderItemRepository(Context context) {
        _db = new DbHelper(context);
    }

    protected String getSelect() {
        return String.format("SELECT %s, %s, %s, %s, %s FROM %s ", DbHelper.OrderItemsTable._ID, DbHelper.OrderItemsTable.ORDER_ID, DbHelper.OrderItemsTable.QTY,
                DbHelper.OrderItemsTable.DISCOUNT, DbHelper.OrderItemsTable.NOTES, DbHelper.OrderItemsTable.TABLENAME);
    }

    protected OrderItem make(Cursor c) {
        OrderItem it = new OrderItem();
        it.setId(c.getLong(0));
        it.setOrderId(c.getLong(1));
        it.setQty(c.getInt(2));
        it.setDiscount(c.getString(3));
        it.setNotes(c.getString(4));

        return it;
    }

    @Override
    public Iterable<OrderItem> getByOrderId(long order_id) {
        ArrayList<OrderItem> items = new ArrayList<>();
        String sql = getSelect() + " WHERE " + DbHelper.OrderItemsTable.ORDER_ID + "=" + order_id + ";";
        Cursor crs = _db.getReadableDatabase().rawQuery(sql, null);

        crs.moveToFirst();
        if (! crs.isAfterLast()) {
            do {
                OrderItem it = make(crs);
                items.add(it);
            } while (crs.moveToNext());
        }

        crs.close();

        return items;
    }

    @Override
    public OrderItem getById(long id) {
        OrderItem it = null;
        String sql = getSelect() + " WHERE " + DbHelper.OrderItemsTable._ID + "=" + id + ";";
        Cursor crs = _db.getReadableDatabase().rawQuery(sql, null);

        crs.moveToFirst();
        if (! crs.isAfterLast()) {
            it = make(crs);
        }
        crs.close();

        return it;
    }

    @Override
    public long add(OrderItem orderItem) throws SQLiteException {
        long newId = 0;
        _db.getWritableDatabase().beginTransaction();

        try {
            newId = insert(orderItem);
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
            return newId;
        } catch (SQLiteException e) {
            _db.getWritableDatabase().endTransaction();
            throw e;
        }
    }

    protected long insert(OrderItem orderItem) {
        String sql = "INSERT INTO " + DbHelper.OrderItemsTable.TABLENAME + " (" +
                DbHelper.OrderItemsTable.ORDER_ID + "," +
                DbHelper.OrderItemsTable.QTY + "," +
                DbHelper.OrderItemsTable.DISCOUNT + "," +
                DbHelper.OrderItemsTable.NOTES + ") " +
                " VALUES (?, ?, ?, ?)";

        SQLiteStatement stmt = _db.getWritableDatabase().compileStatement(sql);
        stmt.bindLong(1, orderItem.getOrderId());
        stmt.bindLong(2, orderItem.getQty());

        if (orderItem.getDiscount() != null) {
            stmt.bindString(3,  orderItem.getDiscount());
        } else {
            stmt.bindNull(3);
        }

        if (orderItem.getNotes() != null) {
            stmt.bindString(4, orderItem.getNotes());
        } else {
            stmt.bindNull(4);
        }

        return stmt.executeInsert();
    }

    @Override
    public void edit(OrderItem orderItem) throws SQLiteException {
        _db.getWritableDatabase().beginTransaction();

        try {
            update(orderItem);
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
        } catch (SQLiteException e) {
            _db.getWritableDatabase().endTransaction();
            throw e;
        }
    }

    protected void update(OrderItem orderItem) {
        String sql = "UPDATE " + DbHelper.OrderItemsTable.TABLENAME + " SET " +
                DbHelper.OrderItemsTable.ORDER_ID + "=?," +
                DbHelper.OrderItemsTable.QTY + "=?," +
                DbHelper.OrderItemsTable.DISCOUNT + "=?," +
                DbHelper.OrderItemsTable.NOTES + "=?" +
                " WHERE " + DbHelper.OrderItemsTable._ID + "=?";

        SQLiteStatement stmt = _db.getWritableDatabase().compileStatement(sql);

        stmt.bindLong(1, orderItem.getOrderId());
        stmt.bindLong(2, orderItem.getQty());
        stmt.bindString(3, orderItem.getDiscount());
        stmt.bindString(4, orderItem.getNotes());

        stmt.executeUpdateDelete();
    }

    @Override
    public void remove(OrderItem orderItem) {
        _db.getWritableDatabase().beginTransaction();

        try {
            delete(orderItem.getId());
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
        } catch (SQLiteException exc) {
            _db.getWritableDatabase().endTransaction();
            throw exc;
        }
    }

    protected void delete(long id) {
        String sql = "DELETE FROM " + DbHelper.OrderItemsTable.TABLENAME + " WHERE " + DbHelper.OrderItemsTable._ID + "=?";
        SQLiteStatement stmt = _db.getWritableDatabase().compileStatement(sql);
        stmt.bindLong(1, id);
        stmt.executeUpdateDelete();
    }

    @Override
    public Iterable<OrderItem> getAll() {
        ArrayList<OrderItem> items = new ArrayList<>();

        Cursor crs = _db.getReadableDatabase().rawQuery(getSelect(), null);
        crs.moveToFirst();

        if (! crs.isAfterLast()) {
            do {
                OrderItem it = make(crs);
                items.add(it);
            } while (crs.moveToNext());
        }
        crs.close();

        return items;
    }

    @Override
    public long size() {
        long cnt = -1;
        try {
            cnt = DatabaseUtils.queryNumEntries(_db.getReadableDatabase(), DbHelper.OrderItemsTable.TABLENAME);
        } catch (SQLiteException e) {
            Log.e(getClass().getCanonicalName() + ".size()", e.toString());
        }
        return cnt;
    }

    @Override
    public void addAll(Iterable<OrderItem> items) {
        try {
            _db.getWritableDatabase().beginTransaction();

            for (OrderItem item : items) {
                insert(item);
                update(item);
            }

            _db.getWritableDatabase().setTransactionSuccessful();
        } catch (SQLiteException exc) {
            Log.e("Error", "Error inserting all order items: " + exc.toString());
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
