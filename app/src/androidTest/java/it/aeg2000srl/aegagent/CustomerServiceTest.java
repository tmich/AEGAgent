package it.aeg2000srl.aegagent;

import android.test.AndroidTestCase;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.infrastructure.CustomerRepository;
import it.aeg2000srl.aegagent.infrastructure.DbHelper;
import it.aeg2000srl.aegagent.services.CustomerService;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public class CustomerServiceTest extends AndroidTestCase {
    TestDbHelper dbh;
    CustomerRepository repo;
    CustomerService serv;

    @Override
    protected void setUp() {
        dbh = new TestDbHelper(getContext());
        repo = new CustomerRepository(dbh);
        serv = new CustomerService(getContext(), repo);
    }

//    public void testCustomerServiceSizeShouldBeMoreThanZero() {
//        assertTrue(repo.size() > 0);
//    }
//
//    public void testCustomerServiceShouldRetrieveCustomerById() {
//        Customer c = serv.getById(1);
//        assertEquals(c.getId(), 1);
//    }
}
