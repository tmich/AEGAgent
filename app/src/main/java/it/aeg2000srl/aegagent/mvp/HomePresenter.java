package it.aeg2000srl.aegagent.mvp;

import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public class HomePresenter {
    IHomeView view;

    public HomePresenter(IHomeView view) {
        this.view = view;

        this.view.onGoToCustomers(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context ctx = view.getContext();
                    Intent intentUpdate = new Intent(ctx, CustomersActivity.class);
                    ctx.startActivity(intentUpdate);
                }
            });

        this.view.onGoToProducts(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = view.getContext();
                Intent intentUpdate = new Intent(ctx, ProductsActivity.class);
                ctx.startActivity(intentUpdate);
            }
        });

    }

//    public void onUpdateButtonClick() {
//        Context ctx = productsView.getContext();
//        Intent intentUpdate = new Intent(ctx, UpdateDataActivity.class);
//        ctx.startActivity(intentUpdate);
//    }

    public void onGoToCustomersButtonClick() {
        Context ctx = view.getContext();
        Intent intentUpdate = new Intent(ctx, CustomersActivity.class);
        ctx.startActivity(intentUpdate);
    }
}
