package org.izv.aad.proyecto.Dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import org.izv.aad.proyecto.R;

public class DialogCustom {

    public static ProgressDialog showDialog(Context context){
        ProgressDialog progressDialog= new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.msg_loading));
        return progressDialog;
    }

}
