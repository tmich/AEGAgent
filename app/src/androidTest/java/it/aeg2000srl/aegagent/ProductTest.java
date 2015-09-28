package it.aeg2000srl.aegagent;

import android.test.AndroidTestCase;

import it.aeg2000srl.aegagent.core.Product;

/**
 * Created by Tiziano on 28/09/2015.
 */
public class ProductTest extends AndroidTestCase {
    void testProductShouldHasValidData() {
        Product p = new Product();
        p.setCode("X1289");
        p.setId(12);
        p.setName("Cocco bello cocco fresco");
        p.setPrice(9.50);

        assertEquals(p.getCode(), "X1289");
        assertEquals(p.getId(), 12);
        assertEquals(p.getName(), "Cocco bello cocco fresco");
        assertEquals(p.getPrice(), 9.50);
    }
}
