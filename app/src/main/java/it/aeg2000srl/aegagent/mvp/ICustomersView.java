package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;
import java.util.ArrayList;

/**
 * Created by tiziano.michelessi on 26/09/2015.
 */
public interface ICustomersView extends IView {
    void setItems(ArrayList<ContentValues> items);

}