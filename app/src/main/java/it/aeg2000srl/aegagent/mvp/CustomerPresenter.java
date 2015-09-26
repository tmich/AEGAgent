package it.aeg2000srl.aegagent.mvp;

import android.app.Activity;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.services.CustomerService;

/**
 * Created by tiziano.michelessi on 26/09/2015.
 */
public class CustomerPresenter {

    // view
    ICustomersView _view;

    //service
    CustomerService _srv;

    public CustomerPresenter(ICustomersView view) {
        _view = view;
        _srv = new CustomerService((Activity)view);
        ArrayList<String> names = new ArrayList<>();
        for (Customer c : _srv.getAll()) {
            names.add(c.getName());
        }

        view.setItems(names);
    }
}
