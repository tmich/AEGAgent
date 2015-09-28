package it.aeg2000srl.aegagent.mvp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    // adapter
    CustomersArrayAdapter adapter;

    public CustomersPresenter(ICustomersView view) {
        _view = view;
        _srv = new CustomerService(_view.getContext());
        adapter = new CustomersArrayAdapter((Activity)_view, (List<Customer>) _srv.getAll());
        _view.setAdapter(adapter);

        _view.update();
    }

    public void onItemClick(int position) {
        Customer item = adapter.getItem(position);
        //String name = item.getAsString("name");
        //long id = item.getAsLong("id");
        //Log.d("tiziano", name);
        Intent detailsView = new Intent(_view.getContext(), CustomerDetailsActivity.class);
        detailsView.putExtra("id", item.getId());
        //detailsView.putExtra("name", name);
        _view.getContext().startActivity(detailsView);
    }

    public void onSearch(String text) {
        _view.getAdapter().clear();
        _view.getAdapter().addAll((Collection<? extends Customer>) _srv.findByName(text));

        _view.update();
    }
}
