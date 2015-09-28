package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import java.net.ConnectException;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public class HomePresenter {
    IView _view;

    public HomePresenter(IView view) {
        _view = view;
    }

    public void onUpdateButtonClick() {
        Context ctx = _view.getContext();
        Intent intentUpdate = new Intent(ctx, UpdateDataActivity.class);
        ctx.startActivity(intentUpdate);
    }

    public void onGoToCustomersButtonClick() {
        Context ctx = _view.getContext();
        Intent intentUpdate = new Intent(ctx, CustomersActivity.class);
        ctx.startActivity(intentUpdate);
    }
}
