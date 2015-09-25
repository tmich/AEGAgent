package it.aeg2000srl.aegagent;

import android.test.AndroidTestCase;

import it.aeg2000srl.aegagent.core.Customer;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public class CustomerTest extends AndroidTestCase {
    public void testCustomerHasValidData() {
        long id = 48;
        String name="Tornatora SpA";
        String code = "00099";
        String address = "Via Ciao Core";
        String cap="00169";
        String city="Roma";
        String prov = "RM";
        String telephone="0662420909";
        String vatNumber="32165498476";

        Customer cust = new Customer();
        cust.setId(id);
        cust.setName(name);
        cust.setCode(code);
        cust.setAddress(address);
        cust.setCap(cap);
        cust.setCity(city);
        cust.setProv(prov);
        cust.setTelephone(telephone);
        cust.setVatNumber(vatNumber);

        assertEquals(cust.getId(), id);
        assertEquals(cust.getName(), name);
        assertEquals(cust.getCode(), code);
        assertEquals(cust.getAddress(), address);
        assertEquals(cust.getCap(), cap);
        assertEquals(cust.getCity(), city);
        assertEquals(cust.getProv(), prov);
        assertEquals(cust.getTelephone(), telephone);
        assertEquals(cust.getVatNumber(), vatNumber);
    }
}
