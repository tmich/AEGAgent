package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import java.util.ArrayList;
import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.services.CustomerService;

/**
 * Created by tiziano.michelessi on 26/09/2015.
 */
public class CustomersPresenter {

    // view
    ICustomersView _view;

    //service
    CustomerService _srv;

    //items
    ArrayList<ContentValues> _customers;

    public CustomersPresenter(ICustomersView view) {
        _view = view;
        _srv = new CustomerService(_view.getContext());
        _customers = new ArrayList<>();

        for (Customer c : _srv.getAll()) {
            ContentValues cv = new ContentValues();
            cv.put("id", c.getId());
            cv.put("name", c.getName());
            _customers.add(cv);
        }

        view.setItems(_customers);
    }

    public void onItemClick(int position) {
        ContentValues item = _customers.get(position);
        //String name = item.getAsString("name");
        long id = item.getAsLong("id");
        //Log.d("tiziano", name);
        Intent detailsView = new Intent(_view.getContext(), CustomerDetailsActivity.class);
        detailsView.putExtra("id", id);
        //detailsView.putExtra("name", name);
        _view.getContext().startActivity(detailsView);
    }
}
