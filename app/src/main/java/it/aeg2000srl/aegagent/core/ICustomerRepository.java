package it.aeg2000srl.aegagent.core;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public interface ICustomerRepository extends IRepository<Customer> {
    Iterable<Customer> findByName(String name);
    Customer getByCode(String code);
}
