package it.aeg2000srl.aegagent.services;

import android.content.ContentValues;
import android.content.Context;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.IOrderRepository;
import it.aeg2000srl.aegagent.core.Order;
import it.aeg2000srl.aegagent.core.OrderItem;
import it.aeg2000srl.aegagent.core.OrderItemRepository;
import it.aeg2000srl.aegagent.infrastructure.OrderRepository;

/**
 * Created by tiziano.michelessi on 01/10/2015.
 */
public class OrderService {
    IOrderRepository repo;
    Context context;

    public OrderService(Context context, IOrderRepository repository) {
        this.context = context;
        this.repo = repository;
    }

    public OrderService(Context context) {
        this.context = context;
        this.repo = new OrderRepository(context);
    }

    public Order createNew() {
        return new Order();
    }

    public Order getById(long id) {
        CustomerService csrv = new CustomerService(context);
        Order order = repo.getById(id);
        order.setCustomer(csrv.getById(order.getCustomerId()));
        return order;
    }

    public Iterable<Order> getByCustomer(Customer customer) {
        return repo.findByCustomerId(customer.getId());
    }

    public void addItem(Order order, OrderItem item) {
        OrderItemRepository orderItemRepository = new OrderItemRepository(context);
        item.setOrderId(order.getId());
        orderItemRepository.add(item);
    }

    public void save(Order o) {
        if (o.getId() == 0) {
            repo.add(o);
        } else {
            repo.edit(o);
        }
    }
}
