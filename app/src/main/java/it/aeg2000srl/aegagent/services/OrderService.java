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
        CustomerService csrv = new CustomerService(context);
        OrderItemService isrv = new OrderItemService(context);
        Iterable<Order> orders = repo.findByCustomerId(customer.getId());
        for(Order order: orders) {
            order.setCustomer(csrv.getById(order.getCustomerId()));
            order.getItems().clear();
            for (OrderItem it : isrv.getItemsByOrderId(order.getId())) {
                order.add(it);
            }
        }
        return orders;
    }

    public void addItem(long order_id, OrderItem item) {
        OrderItemRepository orderItemRepository = new OrderItemRepository(context);
        item.setOrderId(order_id);
        orderItemRepository.add(item);
    }

    public long save(Order o) {
        long id = o.getId();
        if (id == 0) {
            id = repo.add(o);
        } else {
            repo.edit(o);
        }

        OrderItemRepository orderItemRepository = new OrderItemRepository(context);
        for (OrderItem item : o.getItems()) {
            item.setOrderId(id);

            if (item.getId() == 0) {
                orderItemRepository.add(item);
            } else {
                orderItemRepository.edit(item);
            }
        }

        return id;
    }
}
