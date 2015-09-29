package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public interface IOrderDetailsView extends IView {
//    void setItems(ArrayList<ContentValues> items);
    ArrayAdapter getAdapter();
}
