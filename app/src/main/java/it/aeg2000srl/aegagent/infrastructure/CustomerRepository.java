package it.aeg2000srl.aegagent.infrastructure;

/**
 * Created by Tiziano on 24/09/2015.
 */
import java.util.ArrayList;
import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.IRepository;

public class CustomerRepository implements IRepository<Customer>  {



    public CustomerRepository() {


    }

    @Override
    public Customer getById(long id) {


        return null;
    }

    @Override
    public void add(Customer customer) {

    }

    @Override
    public void edit(Customer customer) {

    }

    @Override
    public void remove(Customer customer) {

    }

    @Override
    public Iterable<Customer> getAll() {
        return null;
    }
}
