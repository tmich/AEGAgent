package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.core.Order;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public interface IOrderDetailsView extends IView {
//    void setItems(ArrayList<ContentValues> items);
    ArrayAdapter getAdapter();

    void showMessage(String message);
    void setOrder(Order order);
    Order getOrder();

    void setCustomerName(String name);
//    void finish();
}
