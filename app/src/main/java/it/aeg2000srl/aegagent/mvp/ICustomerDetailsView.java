package it.aeg2000srl.aegagent.mvp;

import android.content.ContentValues;

/**
 * Created by tiziano.michelessi on 26/09/2015.
 */
public interface ICustomerDetailsView extends IView {
    void setItem(ContentValues data);
}
