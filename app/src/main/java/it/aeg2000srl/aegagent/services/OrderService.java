package it.aeg2000srl.aegagent.services;

import android.content.ContentValues;
import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.IOrderRepository;
import it.aeg2000srl.aegagent.core.Order;
import it.aeg2000srl.aegagent.core.OrderItem;
import it.aeg2000srl.aegagent.core.OrderItemRepository;
import it.aeg2000srl.aegagent.infrastructure.CustomerRepository;
import it.aeg2000srl.aegagent.infrastructure.OrderRepository;
import it.aeg2000srl.aegagent.mvp.OrderItemViewModel;
import it.aeg2000srl.aegagent.mvp.OrderViewModel;
import it.aeg2000srl.aegagent.mvp.ViewModelFactory;

/**
 * Created by tiziano.michelessi on 01/10/2015.
 */
public class OrderService {
    IOrderRepository repo;
    Context context;
    ViewModelFactory viewModelFactory;

    public OrderService(Context context, IOrderRepository repository) {
        this.context = context;
        this.repo = repository;
        viewModelFactory = new ViewModelFactory(context);
    }

    public OrderService(Context context) {
        this(context, new OrderRepository(context));
    }

//    public Order createNew() {
//        return new Order();
//    }

    public OrderViewModel getById(long id) {
//        CustomerService csrv = new CustomerService(context);
        CustomerRepository customerRepository = new CustomerRepository(context);
        Order order = repo.getById(id);
        order.setCustomer(customerRepository.getById(order.getCustomerId()));
        return viewModelFactory.toOrderViewModel(order);
    }

    public Iterable<OrderViewModel> getByCustomerId(long customer_id) {
//        CustomerService csrv = new CustomerService(context);
        CustomerRepository customerRepository = new CustomerRepository(context);
//        OrderItemService isrv = new OrderItemService(context);
        OrderItemRepository orderItemRepository = new OrderItemRepository(context);
        ArrayList<OrderViewModel> orderViewModels = new ArrayList<>();

        for(Order order: repo.findByCustomerId(customer_id)) {
            order.setCustomer(customerRepository.getById(order.getCustomerId()));
            order.getItems().clear();
            for (OrderItem it : orderItemRepository.getByOrderId(order.getId())) {
                order.add(it);
            }

            orderViewModels.add(viewModelFactory.toOrderViewModel(order));
        }
        return orderViewModels;
    }

//    public void addItem(long order_id, OrderItem item) {
//        OrderItemRepository orderItemRepository = new OrderItemRepository(context);
//        item.setOrderId(order_id);
//        orderItemRepository.add(item);
//    }

    public long save(OrderViewModel o) {
        long id = o.Id;
        Order order = viewModelFactory.toOrder(o);

        if (id == 0) {
            id = repo.add(order);
        } else {
            repo.edit(order);
        }

        OrderItemRepository orderItemRepository = new OrderItemRepository(context);
        for (OrderItem item : order.getItems()) {

            if (item.getId() == 0) {
                orderItemRepository.add(item);
            } else {
                orderItemRepository.edit(item);
            }
        }

        return id;
    }
}
