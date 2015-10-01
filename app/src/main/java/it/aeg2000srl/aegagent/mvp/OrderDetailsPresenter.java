package it.aeg2000srl.aegagent.mvp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.Order;
import it.aeg2000srl.aegagent.core.OrderItem;
import it.aeg2000srl.aegagent.core.Product;
import it.aeg2000srl.aegagent.services.CustomerService;
import it.aeg2000srl.aegagent.services.OrderService;
import it.aeg2000srl.aegagent.services.ProductService;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public class OrderDetailsPresenter {
    private IOrderDetailsView _view;
    private long _order_id;
    private long _customer_id;
    private OrderService service;
    private Order order;

    public OrderDetailsPresenter(long order_id, long customer_id, IOrderDetailsView view) {
        _view = view;
        _customer_id = customer_id;
        _order_id = order_id;
        service = new OrderService(_view.getContext());

        // carico l'ordine dal db o ne creo uno nuovo
        if (order_id != 0) {
            order = service.getById(order_id);

            // ordine già esistente: carico le voci
            _view.getAdapter().clear();

            for (OrderItem item : order.getItems()) {
                _view.getAdapter().add(item);
            }
        } else {
            // ordine appena creato, carico il cliente
            order = service.createNew();
            CustomerService customerService = new CustomerService(_view.getContext());
            Customer customer = customerService.getById(_customer_id);
            order.setCustomer(customer);
        }

        _view.setCustomerName(order.getCustomer().getName());
        _view.setOrder(order);
        _view.update();
    }

    public void addItem(long product_id, int qty, String notes) {
        ProductService pserv = new ProductService(_view.getContext());
        Product p = pserv.getById(product_id);

        if (p != null) {
            OrderItem item = new OrderItem(p, qty);
            service.addItem(order, item);
            _view.getAdapter().add(item);
        }

        _view.update();
    }

    public void save() {
        service.save(order);
    }
}
