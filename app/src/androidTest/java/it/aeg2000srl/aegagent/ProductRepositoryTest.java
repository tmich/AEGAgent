package it.aeg2000srl.aegagent;

import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.test.AndroidTestCase;

import java.util.Random;
import java.util.UUID;

import it.aeg2000srl.aegagent.core.IProductRepository;
import it.aeg2000srl.aegagent.core.Product;
import it.aeg2000srl.aegagent.infrastructure.DbHelper;
import it.aeg2000srl.aegagent.infrastructure.ProductRepository;

/**
 * Created by Tiziano on 28/09/2015.
 */
public class ProductRepositoryTest extends AndroidTestCase {
    TestDbHelper dbh;
    IProductRepository repo;

    @Override
    protected void  setUp() {
        dbh = new TestDbHelper(getContext());
        repo = new ProductRepository(dbh);
    }

    public void testProductRepositoryShouldAddNewProduct() {
        Product p = new Product();
        String newCode = UUID.randomUUID().toString().substring(0,5);
        p.setCode(newCode);
        p.setName("ProductRepository");
        p.setPrice(new Random().nextDouble());

        //long id = dbh.getWritableDatabase().insert(DbHelper.ProductsTable.TABLENAME, null, cv);
        long id = repo.add(p);
        assertTrue(id > 0);
    }

    public void testProductRepositoryShouldUpdateExistingProduct() {
        String text = "ProductRepositoryTest";

        Product p = new Product();
        String newCode = UUID.randomUUID().toString().substring(0, 5);
        p.setCode(newCode);
        p.setName(text);
        p.setPrice(new Random().nextDouble());
        long id = repo.add(p);

        p = repo.getById(id);
        p.setName("MODIFICATO");
        repo.edit(p);
    }

    public void testProductRepositoryShouldRemoveExistingCustomer() {
        String text = "ProductRepositoryTest";

        Product p = new Product();
        String newCode = UUID.randomUUID().toString().substring(0, 5);
        p.setCode(newCode);
        p.setName(text);
        p.setPrice(new Random().nextDouble());
        long id = repo.add(p);

        p = repo.getById(id);
        repo.remove(p);

        Product nothing = repo.getById(id);
        assertNull(nothing);
    }
}
