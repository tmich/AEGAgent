package it.aeg2000srl.aegagent.services;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.IProductRepository;
import it.aeg2000srl.aegagent.core.Product;
import it.aeg2000srl.aegagent.infrastructure.ProductRepository;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public class ProductService {

    IProductRepository _repo;
    Context _context;

    public ProductService(Context context, IProductRepository repo) {
        _repo = repo;
        _context = context;
    }

    public ProductService(Context context) {
        this(context, new ProductRepository(context));
    }

    public Product getById(long id) {
        return _repo.getById(id);
    }


    public Iterable<Product> findByName(String name) {
        return _repo.findByName(name);
    }

    public Iterable<Product> getAll() {
        return _repo.getAll();
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