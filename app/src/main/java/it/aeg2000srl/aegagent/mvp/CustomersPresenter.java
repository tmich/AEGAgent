package it.aeg2000srl.aegagent.mvp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.Collection;
import java.util.List;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.services.CustomerService;

/**
 * Created by tiziano.michelessi on 26/09/2015.
 */
public class CustomersPresenter {

    // productsView
    ICustomersView customersView;

    //service
    CustomerService service;

    // adapter
//    CustomersArrayAdapter adapter;

    public CustomersPresenter(final ICustomersView view) {
        this.customersView = view;
        service = new CustomerService(this.customersView.getContext());
        customersView.setOnSelectedItem(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Customer item = customersView.getAdapter().getItem(i);
                Intent detailsView = new Intent(customersView.getContext(), CustomerDetailsActivity.class);
                detailsView.putExtra("id", item.getId());
                customersView.getContext().startActivity(detailsView);
            }
        });

        updateView();
    }

    public void updateView()
    {
        customersView.getAdapter().clear();
        customersView.getAdapter().addAll((List<Customer>) service.getAll());
        customersView.update();
    }

//    public void onItemClick(int position) {
//        Customer item = adapter.getItem(position);
//        //String name = item.getAsString("name");
//        //long id = item.getAsLong("id");
//        //Log.d("tiziano", name);
//        Intent detailsView = new Intent(customersView.getContext(), CustomerDetailsActivity.class);
//        detailsView.putExtra("id", item.getId());
//        //detailsView.putExtra("name", name);
//        customersView.getContext().startActivity(detailsView);
//    }

    public void onSearch(String text) {
        customersView.getAdapter().clear();
        customersView.getAdapter().addAll((Collection<? extends Customer>) ( text.equals("") ? service.getAll() : service.findByName(text) ));
        customersView.update();
    }
}
