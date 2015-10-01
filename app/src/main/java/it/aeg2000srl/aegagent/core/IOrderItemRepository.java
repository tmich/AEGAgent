package it.aeg2000srl.aegagent.core;

/**
 * Created by tiziano.michelessi on 01/10/2015.
 */
public interface IOrderItemRepository extends IRepository<OrderItem> {
    Iterable<OrderItem> getByOrderId(long order_id);
}
