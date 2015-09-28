package it.aeg2000srl.aegagent.core;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public interface IProductRepository extends IRepository<Product> {
    Iterable<Product> findByName(String name);
    Product getByCode(String code);
}
