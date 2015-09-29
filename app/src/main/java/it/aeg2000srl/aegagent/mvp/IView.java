package it.aeg2000srl.aegagent.mvp;

import android.content.Context;

/**
 * Created by tiziano.michelessi on 26/09/2015.
 */
public interface IView {
    Context getContext();
    void update();
}
