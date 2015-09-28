package it.aeg2000srl.aegagent.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.infrastructure.DbHelper;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public class ProductRepository implements IProductRepository {
    DbHelper _db;

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
    public Product getByCode(String code) {
        Product prod = null;

        try {
            Cursor crs = _db.getReadableDatabase().query(
                    DbHelper.ProductsTable.TABLENAME,
                    DbHelper.ProductsTable._COL_NAMES,
                    DbHelper.ProductsTable.COL_CODE + "=?",
                    new String[]{code},
                    null, null, null);

            if (!crs.isAfterLast()) {
                crs.moveToFirst();
                prod = make(crs);
                crs.close();
            }

        } catch (SQLiteException sqlexc) {
            return null;
        }

        return prod;
    }

    @Override
    public Product getById(long id) {
        Product prod = null;

        try {
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

        } catch (SQLiteException sqlexc) {
            return null;
        }

        return prod;
    }

    @Override
    public long add(Product product) {
        ContentValues raw_data = toRaw(product);
        raw_data.remove(DbHelper.ProductsTable._ID);
        try {
            long id = _db.getWritableDatabase().insertOrThrow(DbHelper.ProductsTable.TABLENAME, null, raw_data);
            return id;
        } catch (SQLiteException exc) {
            Log.e("Error", "Error inserting product: " + exc.toString());
            return 0;
        }
    }

    @Override
    public void edit(Product product) {
        ContentValues raw_data = toRaw(product);
        try {
            _db.getWritableDatabase().update(DbHelper.ProductsTable.TABLENAME, raw_data,
                    DbHelper.ProductsTable.COL_CODE + "=?",
                    new String[] {product.getCode()} );
        } catch (SQLiteException exc) {
            Log.e("Error updating product", exc.toString());
        }
    }

    @Override
    public void remove(Product product) {
        try {
            _db.getWritableDatabase().delete(DbHelper.ProductsTable.TABLENAME,
                    DbHelper.ProductsTable.COL_CODE + "=?",
                    new String[] {product.getCode()} );
        } catch (SQLiteException exc) {
            Log.e("Error deleting product", exc.toString());
        }
    }

    @Override
    public ArrayList<Product> getAll() {
        ArrayList<Product> products = new ArrayList<>();

        try {
            Cursor crs = _db.getReadableDatabase().query(
                    DbHelper.ProductsTable.TABLENAME,
                    DbHelper.ProductsTable._COL_NAMES,
                    null, null,null, null, null);

            crs.moveToFirst();

            if (! crs.isAfterLast()) {
                products = new ArrayList<>();

                do {
                    Product prod = make(crs);
                    products.add(prod);
                } while (crs.moveToNext());
            }

            crs.close();

        } catch (SQLiteException sqlexc) {
            return null;
        }

        return products;
    }

    @Override
    public ArrayList<Product> findByName(String name) {
        ArrayList<Product> products = new ArrayList<>();

        try {
//            Cursor crs = _db.getReadableDatabase().query(
//                    DbHelper.CustomersTable.TABLENAME,
//                    DbHelper.CustomersTable._COL_NAMES,
//                    DbHelper.CustomersTable.COL_NAME + " like '%?%' ",
//                    new String[] {name},
//                    null, null, null);

            String sql = "SELECT * FROM " + DbHelper.ProductsTable.TABLENAME + " WHERE " + DbHelper.ProductsTable.COL_NAME
                    + " LIKE '%" + name + "%'";
            Cursor crs = _db.getReadableDatabase().rawQuery(sql, null);

            crs.moveToFirst();

            if (! crs.isAfterLast()) {
                products = new ArrayList<>();

                do {
                    Product prod = make(crs);
                    products.add(prod);
                } while (crs.moveToNext());
            }

            crs.close();

        } catch (SQLiteException sqlexc) {
            return null;
        }

        return products;
    }

}
