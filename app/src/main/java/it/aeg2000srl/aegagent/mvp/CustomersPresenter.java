package it.aeg2000srl.aegagent.mvp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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

    public CustomersPresenter(final ICustomersView view) {
        this.customersView = view;
        service = new CustomerService(this.customersView.getContext());
        customersView.setOnSelectedItem(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomerViewModel customerViewModel = customersView.getAdapter().getItem(i);
                Intent detailsView = new Intent(customersView.getContext(), CustomerDetailsActivity.class);
                detailsView.putExtra("id", customerViewModel.Id);
                customersView.getContext().startActivity(detailsView);
            }
        });

        updateView();
        Log.w("CustomersPresenter", "instantiated!");
    }

    public void updateView()
    {
        customersView.getAdapter().clear();
        customersView.getAdapter().addAll((List<CustomerViewModel>) service.getAll());
        customersView.update();
    }


    public void onSearch(String text) {
        customersView.getAdapter().clear();
        customersView.getAdapter().addAll((Collection<? extends CustomerViewModel>) ( text.equals("") ? service.getAll() : service.findByName(text) ));
        customersView.update();
    }
}
