package it.aeg2000srl.aegagent.mvp;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.core.Customer;

/**
 * Created by tiziano.michelessi on 26/09/2015.
 */
public interface ICustomersView {
    void setItems(ArrayList<String> items);
}
