package it.aeg2000srl.aegagent.mvp;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by tiziano.michelessi on 29/09/2015.
 */
public interface IProductsView extends IView {
    ProductsArrayAdapter getAdapter();
    void setOnSelectedItem(AdapterView.OnItemClickListener listener);
    void showMessage(String message);
}
