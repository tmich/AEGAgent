package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by tiziano.michelessi on 26/09/2015.
 */
public interface ICustomersView extends IView {
    void update();

    void setOnSelectedItem(AdapterView.OnItemClickListener listener);

//    ListView getListView();

    CustomersArrayAdapter getAdapter();
//    void setAdapter(CustomersArrayAdapter adapter);
}
