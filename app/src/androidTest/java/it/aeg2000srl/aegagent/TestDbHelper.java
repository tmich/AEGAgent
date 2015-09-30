package it.aeg2000srl.aegagent;

import android.content.ContentValues;
import android.content.Context;

import java.util.Random;
import java.util.UUID;

import it.aeg2000srl.aegagent.core.Product;
import it.aeg2000srl.aegagent.infrastructure.DbHelper;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public class TestDbHelper extends DbHelper {
    public static String DATABASE_NAME = "agent_test.db";

    public TestDbHelper(Context context) {
        super(context, DATABASE_NAME);
        seedProducts();
        seedCustomers();
    }

    protected void seedCustomers() {
        String text = "TEST VALUE";
        for (int i=0; i<5; i++) {
            ContentValues cv = new ContentValues();
            String newCode = UUID.randomUUID().toString().substring(0, 5);
            cv.put(CustomersTable.COL_CODE, newCode);
            cv.put(CustomersTable.COL_NAME, "Cliente di Test n. " + new Random().nextInt(5));
            cv.put(CustomersTable.COL_ADDRESS, text);
            cv.put(CustomersTable.COL_CAP, "00000");
            cv.put(CustomersTable.COL_CITY, text);
            cv.put(CustomersTable.COL_TEL, text);
            cv.put(CustomersTable.COL_IVA, UUID.randomUUID().toString().substring(0, 11));
            getWritableDatabase().insert(CustomersTable.TABLENAME, null, cv);
        }
    }

    protected void seedProducts() {
        for (int i=0; i<5; i++) {
            ContentValues cv = new ContentValues();
            String newCode = UUID.randomUUID().toString().substring(0, 5);
            cv.put(ProductsTable.COL_CODE, newCode);
            cv.put(ProductsTable.COL_NAME, "Prodotto di Test n. " + new Random().nextInt(5));
            cv.put(ProductsTable.COL_PRICE, new Random().nextDouble());
            getWritableDatabase().insert(ProductsTable.TABLENAME, null, cv);
        }
    }
}
