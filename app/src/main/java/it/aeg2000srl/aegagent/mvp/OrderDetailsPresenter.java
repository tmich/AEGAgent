package it.aeg2000srl.aegagent.mvp;

import android.content.Intent;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public class OrderDetailsPresenter {
    private IOrderDetailsView _view;
    private long _order_id;
    private long _customer_id;

    public OrderDetailsPresenter(long order_id, long customer_id, IOrderDetailsView view) {
        _view = view;
        _customer_id = customer_id;
        _order_id = order_id;
    }

}
