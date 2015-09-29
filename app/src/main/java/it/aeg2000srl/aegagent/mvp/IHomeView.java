package it.aeg2000srl.aegagent.mvp;

import android.view.View;

/**
 * Created by tiziano.michelessi on 29/09/2015.
 */
public interface IHomeView extends IView {
    void onGoToCustomers(View.OnClickListener listener);

    void onGoToProducts(View.OnClickListener listener);
}
