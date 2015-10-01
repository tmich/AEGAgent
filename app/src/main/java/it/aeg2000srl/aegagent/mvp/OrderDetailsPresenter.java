package it.aeg2000srl.aegagent.mvp;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.Order;
import it.aeg2000srl.aegagent.core.OrderItem;
import it.aeg2000srl.aegagent.services.CustomerService;
import it.aeg2000srl.aegagent.services.OrderService;

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
//        ProductService pserv = new ProductService(_view.getContext());
//        Product p = pserv.getById(product_id);

        if (product_id > 0) {
            OrderItem item = new OrderItem(product_id, qty);
            //service.addItem(order, item);
            //item.setProductId(p.getId());
            order.add(item);
            _view.getAdapter().add(item);
        }

        _view.update();
    }

    //todo: remove item

    public void save() {
        long id = service.save(order);

//        for(OrderItem item : order.getItems()) {
//            service.addItem(id, item);
//        }
    }
}
