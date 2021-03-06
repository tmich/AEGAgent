package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.content.Intent;

import java.util.List;

import it.aeg2000srl.aegagent.core.Order;
import it.aeg2000srl.aegagent.services.CustomerService;
import it.aeg2000srl.aegagent.services.OrderService;

/**
 * Created by tiziano.michelessi on 26/09/2015.
 */
public class CustomerDetailsPresenter {
    // productsView
    ICustomerDetailsView customerDetailsView;

    //service
    CustomerService customerService;

    // model
    CustomerViewModel customerViewModel;

    public CustomerDetailsPresenter(ICustomerDetailsView view, long customer_id) {
        customerDetailsView = view;
        customerService = new CustomerService(customerDetailsView.getContext());
        customerViewModel = customerService.getById(customer_id);
        customerDetailsView.getWaitingOrdersAdapter().clear();
        OrderService orderService = new OrderService(customerDetailsView.getContext());
        customerDetailsView.getWaitingOrdersAdapter().addAll((List<OrderViewModel>) orderService.getByCustomerId(customerViewModel.Id));
        customerDetailsView.setCustomer(customerViewModel);
        customerDetailsView.update();
    }

    public void createNewOrder(long customer_id) {
        Intent newOrderIntent = new Intent(customerDetailsView.getContext(), OrderDetailsActivity.class);
        newOrderIntent.putExtra("customer_id", customer_id);
        newOrderIntent.putExtra("order_id", 0L);
        customerDetailsView.getContext().startActivity(newOrderIntent);
    }
}
