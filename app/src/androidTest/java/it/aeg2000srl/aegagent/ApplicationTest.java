package it.aeg2000srl.aegagent;

import android.app.Application;
import android.test.ApplicationTestCase;
import it.aeg2000srl.aegagent.core.*;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testCustomerHasValidData() {
        long id = 1;
        String name="AEG2000 s.r.l.";
        String address = "Via Monte del Marmo";
        String cap="00165";
        String city="Roma";
        String telephone="066242632";
        String vatNumber="09876574839";

        Customer cust = new Customer();
        cust.setId(id);
        cust.setName(name);
        cust.setAddress(address);
        cust.setCap(cap);
        cust.setCity(city);
        cust.setTelephone(telephone);
        cust.setVatNumber(vatNumber);

        assertEquals(cust.getId(), id);
        assertEquals(cust.getName(), name);
        assertEquals(cust.getAddress(), address);
        assertEquals(cust.getCap(), cap);
        assertEquals(cust.getCity(), city);
        assertEquals(cust.getTelephone(), telephone);
        assertEquals(cust.getVatNumber(), vatNumber);
    }
}