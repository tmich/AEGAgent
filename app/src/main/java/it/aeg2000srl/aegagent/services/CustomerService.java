package it.aeg2000srl.aegagent.services;
import android.content.ContentValues;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.ICustomerRepository;
import it.aeg2000srl.aegagent.infrastructure.CustomerRepository;
import it.aeg2000srl.aegagent.infrastructure.DbHelper;
import it.aeg2000srl.aegagent.mvp.CustomerViewModel;
import it.aeg2000srl.aegagent.mvp.ViewModelFactory;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public class CustomerService {
    ICustomerRepository _repo;
    Context _context;
    ViewModelFactory viewModelFactory;

    public CustomerService(Context context, ICustomerRepository repo) {
        _repo = repo;
        _context = context;
        viewModelFactory = new ViewModelFactory(context);
    }

    public CustomerService(Context context) {
        this(context, new CustomerRepository(context));
    }

    public CustomerViewModel getById(long id) {
        return viewModelFactory.toCustomerViewModel(_repo.getById(id));
    }


    public Iterable<CustomerViewModel> findByName(String name) {
        ArrayList<CustomerViewModel> customerViewModels = new ArrayList<>();
        for (Customer c : _repo.findByName(name)) {
            customerViewModels.add(viewModelFactory.toCustomerViewModel(c));
        }
        return customerViewModels;
    }

    public Iterable<CustomerViewModel> getAll() {
        List<CustomerViewModel> customerViewModels = new ArrayList<>();
        List<Customer> results = (List<Customer>) _repo.getAll();
        int sz = results.size();
        for (Customer c : results.subList(0, (sz > 100 ? 100 : sz))) {
            customerViewModels.add(viewModelFactory.toCustomerViewModel(c));
        }
        return customerViewModels;
    }

    public void save(ContentValues data) {
        long id = 0;
        String code = data.getAsString("code");
        Customer c = new Customer();

        if(_repo.getByCode(code) != null) {
            c = _repo.getByCode(code);
            id = c.getId();
        }

        c.setName(data.getAsString("name"));
        c.setCode(data.getAsString("code"));
        c.setAddress(data.getAsString("address"));
        c.setVatNumber(data.getAsString("iva"));
        c.setProv(data.getAsString("prov"));
        c.setCity(data.getAsString("city"));
        c.setTelephone(data.getAsString("tel"));
        c.setCap(data.getAsString("cap"));

        if(id > 0) {
            _repo.edit(c);
        } else {
            _repo.add(c);
        }
    }

    public void saveAll(Iterable<ContentValues> data) {
        ArrayList<Customer> customers = new ArrayList<>();

        for (ContentValues cv :
                data) {
            Customer c = new Customer();
            c.setName(cv.getAsString("name"));
            c.setCode(cv.getAsString("code"));
            c.setAddress(cv.getAsString("address"));
            c.setVatNumber(cv.getAsString("iva"));
            c.setProv(cv.getAsString("prov"));
            c.setCity(cv.getAsString("city"));
            c.setTelephone(cv.getAsString("tel"));
            c.setCap(cv.getAsString("cap"));

            customers.add(c);
        }

        _repo.addAll(customers);
    }

}
