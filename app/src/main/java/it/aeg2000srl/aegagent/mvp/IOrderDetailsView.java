package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.core.Order;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public interface IOrderDetailsView extends IView {
//    void setItems(ArrayList<ContentValues> items);
    ArrayAdapter getAdapter();

    void showMessage(String message);
    void setOrderViewModel(OrderViewModel orderViewModel);
    OrderViewModel getOrderViewModel();
    void setActionOnNewButton(View.OnClickListener listener);
    void startActivityForResult(Intent intent, int requestCode);
}
