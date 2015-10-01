package it.aeg2000srl.aegagent.core;

/**
 * Created by tiziano.michelessi on 01/10/2015.
 */
public interface IOrderRepository extends IRepository<Order> {
    Iterable<Order> findByCustomerId(long customer_id);
}
