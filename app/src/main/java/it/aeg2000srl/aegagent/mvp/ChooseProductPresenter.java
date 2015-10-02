package it.aeg2000srl.aegagent.mvp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.Collection;

import it.aeg2000srl.aegagent.core.Product;
import it.aeg2000srl.aegagent.services.ProductService;

/**
 * Created by tiziano.michelessi on 30/09/2015.
 */
public class ChooseProductPresenter implements IProductsPresenter {
    // productsView
    IProductsView productsView;

    // service
    ProductService service;

    public ChooseProductPresenter(IProductsView productsActivity) {
        this.productsView = productsActivity;
        this.service = new ProductService(this.productsView.getContext());

        this.productsView.setOnSelectedItem(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ProductsArrayAdapter.ViewHolder holder = (ProductsArrayAdapter.ViewHolder)view.getTag(); //productsView.getAdapter().getView(position,view,parent).getTag();
//                long result = Long.parseLong(holder.idView.getText().toString());
                ProductViewModel productViewModel = productsView.getAdapter().getItem(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", productViewModel.Id);
                ((Activity)productsView).setResult(Activity.RESULT_OK, returnIntent);
                ((Activity)productsView).finish();
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
