package it.aeg2000srl.aegagent.mvp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import java.util.ArrayList;
import java.util.Date;

import it.aeg2000srl.aegagent.services.CustomerService;
import it.aeg2000srl.aegagent.services.OrderService;
import it.aeg2000srl.aegagent.services.ProductService;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public class OrderDetailsPresenter {
    private IOrderDetailsView _view;
//    private long _order_id;
//    private long _customer_id;
    private OrderService orderService;
    private OrderViewModel orderViewModel;
    private CustomerService customerService;
    private CustomerViewModel customerViewModel;

    static final int PICK_PRODUCT_REQUEST = 1;  // The request code

    public OrderDetailsPresenter(long order_id, long customer_id, IOrderDetailsView view) {
        _view = view;
//        _customer_id = customer_id;
//        _order_id = order_id;
        orderService = new OrderService(_view.getContext());
//        orderViewModel = orderService.getById(order_id);

        customerService = new CustomerService(_view.getContext());
        customerViewModel = customerService.getById(customer_id);

        // pulsante "crea ordine"
        _view.setActionOnNewButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // apro l'activity prodotti per farmi restituire un risultato
                Intent chooseProduct = new Intent(_view.getContext(), ProductsActivity.class);
                chooseProduct.setAction("CHOOSE_PRODUCT");
                _view.startActivityForResult(chooseProduct, PICK_PRODUCT_REQUEST);
            }
        });

        // carico l'ordine dal db o ne creo uno nuovo
        if (order_id != 0) {
            orderViewModel = orderService.getById(order_id);

            // ordine già esistente: carico le voci
            _view.getAdapter().clear();
            _view.getAdapter().addAll(orderViewModel.Items);
        } else {
            // ordine appena creato, carico il cliente
            orderViewModel = new OrderViewModel();
            orderViewModel.Customer = customerViewModel;
        }

//        customerDetailsView.setCustomerName(order.getCustomer().getName());
        _view.setOrderViewModel(orderViewModel);
        _view.update();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_PRODUCT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // The user picked a product.
                long id = data.getLongExtra("result", 0);
                //showMessage(String.valueOf(id));
                if (id > 0) {
                    // TODO: set quantity, discount and notes
                    addItem(id, 1, "prova", "");
                }
            }
        }
    }

    public void addItem(long product_id, int qty, String notes, String discount) {
//        ProductService pserv = new ProductService(customerDetailsView.getContext());
//        Product p = pserv.getById(product_id);

        if (product_id > 0) {
            ProductService pserv = new ProductService(_view.getContext());
            ProductViewModel p = pserv.getById(product_id);
            OrderItemViewModel item = new OrderItemViewModel();
            item.Product = p;
            item.Discount = discount;
            item.Notes = notes;
            item.Quantity = qty;


//            order.add(item);
            _view.getAdapter().add(item);
        }

        _view.update();
    }

    //todo: remove item

    public void save() {
        OrderViewModel orderViewModel = new OrderViewModel();
        orderViewModel.CreationDate = new Date();
        orderViewModel.Customer = customerViewModel;
        orderViewModel.Items = new ArrayList<>();

        for (int i=0; i < _view.getAdapter().getCount(); i++) {
            OrderItemViewModel item = (OrderItemViewModel) _view.getAdapter().getItem(i);
            orderViewModel.Items.add(item);
        }

        orderViewModel.Notes = "notes";
        orderViewModel.UserId = 1;
        // TODO: replace with real values

        long id = orderService.save(orderViewModel);

    }
}
