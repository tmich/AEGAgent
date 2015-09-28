package it.aeg2000srl.aegagent;

import android.content.ContentValues;
import android.test.AndroidTestCase;

import it.aeg2000srl.aegagent.infrastructure.DbHelper;

/**
 * Created by Tiziano on 28/09/2015.
 */
public class ProductRepositoryTest extends AndroidTestCase {
    TestDbHelper dbh;

    @Override
    protected void  setUp() {
        dbh = new TestDbHelper(getContext());

        /* seed */
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.ProductsTable.COL_CODE, "P0001");
        cv.put(DbHelper.ProductsTable.COL_NAME, "Ciocco Panna Tronchetto");
        cv.put(DbHelper.ProductsTable.COL_PRICE, 7.70);

        dbh.getWritableDatabase().insert(DbHelper.ProductsTable.TABLENAME, null, cv);
    }

    public void testInsertNewProduct() {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.ProductsTable.COL_CODE, "P0001");
        cv.put(DbHelper.ProductsTable.COL_NAME, "Altro Ciocco Panna Tronchetto");
        cv.put(DbHelper.ProductsTable.COL_PRICE, 7.70);

        long id = dbh.getWritableDatabase().insert(DbHelper.ProductsTable.TABLENAME, null, cv);
        assertTrue(id > 0);
    }
}
