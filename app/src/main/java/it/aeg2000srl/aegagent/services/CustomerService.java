package it.aeg2000srl.aegagent.services;
import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.ICustomerRepository;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public class CustomerService {
    ICustomerRepository _repo;

    public CustomerService(ICustomerRepository repo) {
        _repo = repo;
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
