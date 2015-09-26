package it.aeg2000srl.aegagent.services;
import android.content.Context;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.ICustomerRepository;
import it.aeg2000srl.aegagent.infrastructure.CustomerRepository;
import it.aeg2000srl.aegagent.infrastructure.DbHelper;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public class CustomerService {
    ICustomerRepository _repo;
    Context _context;

    public CustomerService(Context context, ICustomerRepository repo) {
        _repo = repo;
        _context = context;
    }

    public CustomerService(Context context) {
        this(context, new CustomerRepository(context));
    }

    public Customer getById(long id) {
        return _repo.getById(id);
    }


    public Iterable<Customer> findByName(String name) {
        return _repo.findByName(name);
    }

    public Iterable<Customer> getAll() {
        return _repo.getAll();
    }
}
