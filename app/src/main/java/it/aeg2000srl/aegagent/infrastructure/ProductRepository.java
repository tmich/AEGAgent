package it.aeg2000srl.aegagent.infrastructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.core.IProductRepository;
import it.aeg2000srl.aegagent.core.Product;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public class ProductRepository implements IProductRepository {
    DbHelper _db;

    protected String getSelect() {
        return String.format("SELECT %s, %s,%s,%s from %s ", DbHelper.ProductsTable._ID,
                DbHelper.ProductsTable.COL_CODE, DbHelper.ProductsTable.COL_NAME,
                DbHelper.ProductsTable.COL_PRICE, DbHelper.ProductsTable.TABLENAME);
    }

    public ProductRepository(DbHelper db) {
        _db = db;
    }

    public ProductRepository(Context context) {
        _db = new DbHelper(context);
    }

    protected ContentValues toRaw(Product prod) {
        ContentValues cv = new ContentValues();

        cv.put(DbHelper.ProductsTable._ID, prod.getId());
        cv.put(DbHelper.ProductsTable.COL_CODE, prod.getCode());
        cv.put(DbHelper.ProductsTable.COL_NAME, prod.getName());
        cv.put(DbHelper.ProductsTable.COL_PRICE, prod.getPrice());

        return cv;
    }

    protected Product make(Cursor crs) {
        Product prod = new Product();
        prod.setId(crs.getLong(0));
        prod.setCode(crs.getString(1));
        prod.setName(crs.getString(2));
        prod.setPrice(crs.getDouble(3));

        return prod;
    }

    public long size() {
        long cnt = -1;

        try {
            cnt = DatabaseUtils.queryNumEntries(_db.getReadableDatabase(), DbHelper.ProductsTable.TABLENAME);
        } catch (SQLiteException exc) {
            //
        }

        return cnt;
    }

    @Override
    public void addAll(Iterable<Product> items) {
        try {
            _db.getWritableDatabase().beginTransaction();

            for (Product c : items) {
                insert(c, true);
                update(c);
            }

            _db.getWritableDatabase().setTransactionSuccessful();
        } catch (SQLiteException exc) {
            Log.e("Error", "Error inserting all products: " + exc.toString());
            throw exc;
        }
        finally {
            _db.getWritableDatabase().endTransaction();
        }
    }

    @Override
    public Product getByCode(String code) throws SQLiteException {
        Product prod = null;

        String whereClause = DbHelper.ProductsTable.COL_CODE + "=" + DatabaseUtils.sqlEscapeString(code);
        String sql = getSelect() + " WHERE " + whereClause;
        Cursor crs = _db.getReadableDatabase().rawQuery(sql, null);

        if (!crs.isAfterLast()) {
            crs.moveToFirst();
            prod = make(crs);
            crs.close();
        }

        return prod;
    }

    @Override
    public Product getById(long id) throws SQLiteException {
        Product prod = null;

        Cursor crs = _db.getReadableDatabase().query(
                DbHelper.ProductsTable.TABLENAME,
                DbHelper.ProductsTable._COL_NAMES,
                DbHelper.ProductsTable._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, "1");

        if (!crs.isAfterLast()) {
            crs.moveToFirst();
            prod = make(crs);
            crs.close();
        }

        return prod;
    }

    @Override
    public long add(Product product) throws SQLiteException {
        long newId = 0;
        _db.getWritableDatabase().beginTransaction();

        try {
            newId = insert(product, true);
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
            return newId;
        } catch (SQLiteException e) {
            _db.getWritableDatabase().endTransaction();
            throw e;
        }
    }

    protected long insert(Product product, boolean ignore) {
        String ins_sql = "INSERT " + (ignore ? "OR IGNORE" : "") + " INTO " +
                DbHelper.ProductsTable.TABLENAME + " (" +
                DbHelper.ProductsTable.COL_CODE + "," +
                DbHelper.ProductsTable.COL_NAME + "," +
                DbHelper.ProductsTable.COL_PRICE + ")" +
                " VALUES (?, ?, ?)";

        SQLiteStatement ins_stmt = _db.getWritableDatabase().compileStatement(ins_sql);

        ins_stmt.bindString(1, product.getCode());
        ins_stmt.bindString(2, product.getName());
        ins_stmt.bindDouble(3, product.getPrice());

        return ins_stmt.executeInsert();
    }

    @Override
    public void edit(Product product) throws SQLiteException {
        _db.getWritableDatabase().beginTransaction();

        try {
            update(product);
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
        } catch (SQLiteException e) {
            _db.getWritableDatabase().endTransaction();
            throw e;
        }
    }

    protected void update(Product product) {
        String upd_sql = "UPDATE " + DbHelper.ProductsTable.TABLENAME + " SET " +
                DbHelper.ProductsTable.COL_CODE + "=?," +
                DbHelper.ProductsTable.COL_NAME + "=?," +
                DbHelper.ProductsTable.COL_PRICE + "=?"+
                " WHERE " + DbHelper.ProductsTable.COL_CODE + "=?";

        SQLiteStatement upd_stmt = _db.getWritableDatabase().compileStatement(upd_sql);

        upd_stmt.bindString(1, product.getCode());
        upd_stmt.bindString(2, product.getName());
        upd_stmt.bindDouble(3, product.getPrice());
        upd_stmt.bindString(4, product.getCode());

        upd_stmt.executeUpdateDelete();
    }

    @Override
    public void remove(Product product) throws SQLiteException {
        _db.getWritableDatabase().beginTransaction();

        try {
            delete(product);
            _db.getWritableDatabase().setTransactionSuccessful();
            _db.getWritableDatabase().endTransaction();
        } catch (SQLiteException exc) {
            _db.getWritableDatabase().endTransaction();
            throw exc;
        }
    }

    protected void delete(Product product) {
        String del_sql = "DELETE FROM " + DbHelper.ProductsTable.TABLENAME + " WHERE " + DbHelper.ProductsTable._ID + "=?";
        SQLiteStatement del = _db.getWritableDatabase().compileStatement(del_sql);
        del.bindLong(1, product.getId());
        del.executeUpdateDelete();
    }

    @Override
    public ArrayList<Product> getAll() throws SQLiteException {
        ArrayList<Product> products = new ArrayList<>();

        Cursor crs = _db.getReadableDatabase().query(
                DbHelper.ProductsTable.TABLENAME,
                DbHelper.ProductsTable._COL_NAMES,
                null, null, null, null, null);

        crs.moveToFirst();

        if (! crs.isAfterLast()) {
            products = new ArrayList<>();

            do {
                Product prod = make(crs);
                products.add(prod);
            } while (crs.moveToNext());
        }

        crs.close();

        return products;
    }

    @Override
    public ArrayList<Product> findByName(String name) throws SQLiteException {
        ArrayList<Product> products = new ArrayList<>();

        String select = getSelect() + " WHERE " + DbHelper.ProductsTable.COL_NAME + " LIKE " + DatabaseUtils.sqlEscapeString("%" + name + "%") + "";
        Cursor crs = _db.getReadableDatabase().rawQuery(select, null);

        crs.moveToFirst();

        if (! crs.isAfterLast()) {
            products = new ArrayList<>();

            do {
                Product prod = make(crs);
                products.add(prod);
            } while (crs.moveToNext());
        }

        crs.close();
        return products;
    }

    @Override
    protected void finalize() {
        _db.close();
    }
}
