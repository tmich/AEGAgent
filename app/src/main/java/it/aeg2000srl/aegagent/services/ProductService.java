package it.aeg2000srl.aegagent.services;

import android.content.ContentValues;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.IProductRepository;
import it.aeg2000srl.aegagent.core.Product;
import it.aeg2000srl.aegagent.infrastructure.ProductRepository;
import it.aeg2000srl.aegagent.mvp.ProductViewModel;
import it.aeg2000srl.aegagent.mvp.ViewModelFactory;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public class ProductService {

    IProductRepository _repo;
    Context _context;
    ViewModelFactory viewModelFactory;

    public ProductService(Context context, IProductRepository repo) {
        _repo = repo;
        _context = context;
        viewModelFactory = new ViewModelFactory(context);
    }



    public ProductService(Context context) {
        this(context, new ProductRepository(context));
    }

    public ProductViewModel getById(long id) {
        return viewModelFactory.toProductViewModel(_repo.getById(id));
    }


    public Iterable<ProductViewModel> findByName(String name) {
        ArrayList<ProductViewModel> pvms = new ArrayList<>();
        for(Product p : _repo.findByName(name)){
            pvms.add(viewModelFactory.toProductViewModel(p));
        }
        return pvms;
    }

    public Iterable<ProductViewModel> getAll() {
        ArrayList<ProductViewModel> pvms = new ArrayList<>();

        for(Product p : _repo.getAll()){
            pvms.add(viewModelFactory.toProductViewModel(p));
        }

        return pvms;
    }

    public void save(ContentValues data) {
        long id = 0;
        String code = data.getAsString("code");
        Product p = new Product();

        if(_repo.getByCode(code) != null) {
            p = _repo.getByCode(code);
            id = p.getId();
        }

        p.setName(data.getAsString("name"));
        p.setCode(data.getAsString("code"));
        p.setPrice(data.getAsDouble("price"));

        if(id > 0) {
            _repo.edit(p);
        } else {
            _repo.add(p);
        }
    }

    public void saveAll(Iterable<ContentValues> data) {
        ArrayList<Product> products = new ArrayList<>();

        for (ContentValues cv :
                data) {
            Product prod = new Product();
            prod.setName(cv.getAsString("name"));
            prod.setCode(cv.getAsString("code"));
            prod.setPrice(cv.getAsDouble("price"));

            products.add(prod);
        }

        _repo.addAll(products);
    }

}