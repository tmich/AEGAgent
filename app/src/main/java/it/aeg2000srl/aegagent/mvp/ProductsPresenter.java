package it.aeg2000srl.aegagent.mvp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import it.aeg2000srl.aegagent.mvp.ProductViewModel;

import java.util.Collection;

//import it.aeg2000srl.aegagent.core.Product;
import it.aeg2000srl.aegagent.services.ProductService;

/**
 * Created by tiziano.michelessi on 29/09/2015.
 */
public class ProductsPresenter implements IProductsPresenter {
    // productsView
    IProductsView productsView;

    // service
    ProductService service;

    // Adapter
//    ProductsArrayAdapter adapter;

    public ProductsPresenter(final IProductsView view) {
        this.productsView = view;
        this.service = new ProductService(this.productsView.getContext());

        this.productsView.setOnSelectedItem(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productsView.showMessage(productsView.getAdapter().getItem(position).Name);
            }
        });

        updateView();
    }

    protected void updateView()
    {
        productsView.getAdapter().clear();
        productsView.getAdapter().addAll((Collection<? extends ProductViewModel>) service.getAll());
        productsView.update();
    }

    @Override
    public void onSearch(String text) {
        productsView.getAdapter().clear();
        productsView.getAdapter().addAll((Collection<? extends ProductViewModel>) ( text.equals("") ? service.getAll() : service.findByName(text) ));
        productsView.update();
    }
}
