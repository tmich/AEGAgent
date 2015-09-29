package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.content.Intent;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.services.CustomerService;

/**
 * Created by tiziano.michelessi on 26/09/2015.
 */
public class CustomerDetailsPresenter {
    // productsView
    ICustomerDetailsView _view;

    //service
    CustomerService _srv;

    // model
    Customer _cust;

    public CustomerDetailsPresenter(ICustomerDetailsView view, long id) {
        _view = view;
        _srv = new CustomerService(_view.getContext());
        _cust = _srv.getById(id);

        //Todo: chiamare un metodo "make" di una factory
        ContentValues cv = new ContentValues();
        cv.put("name", _cust.getName());
        cv.put("address", _cust.getAddress());
        cv.put("city", _cust.getCity());

        view.setItem(cv);
    }

    public void onNewOrder(long customer_id) {
        Intent newOrderIntent = new Intent(_view.getContext(), OrderDetailsActivity.class);
        newOrderIntent.putExtra("customer_id", customer_id);
        newOrderIntent.putExtra("order_id", 0L);
        _view.getContext().startActivity(newOrderIntent);

    }
}
