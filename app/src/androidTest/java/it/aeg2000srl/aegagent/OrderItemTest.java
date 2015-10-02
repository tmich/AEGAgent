package it.aeg2000srl.aegagent;

import android.test.AndroidTestCase;

import junit.framework.Test;

import it.aeg2000srl.aegagent.core.OrderItem;
import it.aeg2000srl.aegagent.core.Product;
import it.aeg2000srl.aegagent.infrastructure.ProductRepository;
import it.aeg2000srl.aegagent.services.ProductService;

/**
 * Created by Tiziano on 30/09/2015.
 */
public class OrderItemTest extends AndroidTestCase {
    ProductRepository repo;
    ProductService serv;
    TestDbHelper dbh;

    public void setUp() {
        dbh = new TestDbHelper(getContext());
        repo = new ProductRepository(dbh);
        serv = new ProductService(getContext(), repo);
    }

    public void testOrderItemWasCorrectlyInitialized() {
        Product p = repo.getAll().get(1);
        OrderItem it = new OrderItem(p.getId());
        assertEquals(it.getQty(), 1);
    }
}
