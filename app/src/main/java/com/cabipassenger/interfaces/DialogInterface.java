package com.cabipassenger.interfaces;

import android.app.Dialog;
import android.view.View;

/**
 * Created by developer on 3/22/16.
 * Interface to get result on dialog action done
 */
public interface DialogInterface {
    public void onSuccess(View view,Dialog dialog,String resultcode);
    public void onFailure(View view,Dialog dialog,String resultcode);
}
