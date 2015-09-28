package it.aeg2000srl.aegagent;

import android.content.ContentValues;
import android.test.AndroidTestCase;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.infrastructure.CustomerRepository;
import it.aeg2000srl.aegagent.infrastructure.DbHelper;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public class CustomerRepositoryTest extends AndroidTestCase {

    TestDbHelper dbh;
    long _id;

    @Override
    protected void setUp() {
        dbh = new TestDbHelper(getContext());

        /* seed */
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.CustomersTable.COL_NAME, "AEG2000 s.r.l.");
        cv.put(DbHelper.CustomersTable.COL_CODE, "00001");
        cv.put(DbHelper.CustomersTable.COL_ADDRESS, "Via Monte del Marmo");
        cv.put(DbHelper.CustomersTable.COL_CAP, "00165");
        cv.put(DbHelper.CustomersTable.COL_CITY, "Roma");
        cv.put(DbHelper.CustomersTable.COL_TEL, "066242632");
        cv.put(DbHelper.CustomersTable.COL_IVA, "09876574839");

        ContentValues cv2 = new ContentValues();
        cv2.put(DbHelper.CustomersTable.COL_NAME, "Bar Pippo s.r.l.");
        cv2.put(DbHelper.CustomersTable.COL_CODE, "00002");
        cv2.put(DbHelper.CustomersTable.COL_ADDRESS, "Via Pippo Baudo");
        cv2.put(DbHelper.CustomersTable.COL_CAP, "20120");
        cv2.put(DbHelper.CustomersTable.COL_CITY, "Milano");
        cv2.put(DbHelper.CustomersTable.COL_TEL, "066242632");
        cv2.put(DbHelper.CustomersTable.COL_IVA, "65976574839");

        _id = dbh.getWritableDatabase().insert(DbHelper.CustomersTable.TABLENAME, null, cv);
        dbh.getWritableDatabase().insert(DbHelper.CustomersTable.TABLENAME, null, cv2);
    }

    public void testCustomerRepositoryShouldAddNewCustomer() {
        long id;

        String name="Un cliente a caso s.r.l.";
        String address = "Piazza Starnese";
        String cap="20258";
        String city="Rho";
        String prov = "MI";
        String telephone="066242632";
        String vatNumber="09876574839";

        Customer cust = new Customer();

        cust.setName(name);
        cust.setAddress(address);
        cust.setCap(cap);
        cust.setCity(city);
        cust.setTelephone(telephone);
        cust.setVatNumber(vatNumber);

        CustomerRepository repo = new CustomerRepository(dbh);
        long cnt = repo.size();
        repo.add(cust);
        assertEquals(repo.size(), cnt + 1);
    }

    public void testCustomerRepositoryShouldUpdateExistingCustomer() {
        CustomerRepository repo = new CustomerRepository(dbh);
        Customer c1 = repo.getById(_id);
        c1.setName("MODIFICATO");
        assertEquals(c1.getName(), "MODIFICATO");
    }
}
