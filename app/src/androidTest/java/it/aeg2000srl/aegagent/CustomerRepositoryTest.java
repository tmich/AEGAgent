package it.aeg2000srl.aegagent;

import android.test.AndroidTestCase;

import java.util.UUID;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.ICustomerRepository;
import it.aeg2000srl.aegagent.infrastructure.CustomerRepository;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public class CustomerRepositoryTest extends AndroidTestCase {
    ICustomerRepository repo;
    TestDbHelper dbh;
    long last_inserted_id;

    @Override
    protected void setUp() {
        dbh = new TestDbHelper(getContext());
        repo = new CustomerRepository(dbh);
    }

    public void testCustomerRepositoryShouldAddNewCustomer() {
        String text = "CustomerRepositoryTest-setUp";
        Customer c = new Customer();
        String newCode = UUID.randomUUID().toString().substring(0, 5);
        c.setCode(newCode);
        c.setName(text);
        c.setAddress(text);
        c.setCap("00000");
        c.setCity(text);
        c.setTelephone(text);
        c.setVatNumber(UUID.randomUUID().toString().substring(0, 11));
        last_inserted_id = repo.add(c);
        assertTrue(last_inserted_id > 0);
    }

    public void testCustomerRepositoryShouldUpdateExistingCustomer() {
        String text = "CustomerRepositoryTest-setUp";

        Customer c = new Customer();
        String newCode = UUID.randomUUID().toString().substring(0, 5);
        c.setCode(newCode);
        c.setName(text);
        c.setAddress(text);
        c.setCap("00000");
        c.setCity(text);
        c.setTelephone(text);
        c.setVatNumber(UUID.randomUUID().toString().substring(0, 11));
        last_inserted_id = repo.add(c);

        Customer c1 = repo.getById(last_inserted_id);
        c1.setName("MODIFICATO");
        repo.edit(c1);
    }

    public void testCustomerRepositoryShouldRemoveExistingCustomer() {
        String text = "CustomerRepositoryTest-setUp";

        Customer c = new Customer();
        String newCode = UUID.randomUUID().toString().substring(0, 5);
        c.setCode(newCode);
        c.setName(text);
        c.setAddress(text);
        c.setCap("00000");
        c.setCity(text);
        c.setTelephone(text);
        c.setVatNumber(UUID.randomUUID().toString().substring(0, 11));
        last_inserted_id = repo.add(c);

        Customer c1 = repo.getById(last_inserted_id);
        repo.remove(c1);

        Customer nobody = repo.getById(last_inserted_id);
        assertNull(nobody);
    }
}
