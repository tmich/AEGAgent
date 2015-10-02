package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.widget.ArrayAdapter;

import java.util.List;

import it.aeg2000srl.aegagent.core.Order;

/**
 * Created by tiziano.michelessi on 26/09/2015.
 */
public interface ICustomerDetailsView extends IView {
    void setCustomer(CustomerViewModel customerViewModel);
//    void setWaitingOrders(List<Order> waitingOrders);
    ArrayAdapter<OrderViewModel> getWaitingOrdersAdapter();
}
