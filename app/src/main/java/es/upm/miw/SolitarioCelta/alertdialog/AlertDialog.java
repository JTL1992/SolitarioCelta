package es.upm.miw.SolitarioCelta.alertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import es.upm.miw.SolitarioCelta.R;

/**
 * Created by nuonuo-jtl on 16/10/29.
 */
public abstract class AlertDialog extends DialogFragment {

    protected Activity activity;

    protected android.app.AlertDialog.Builder builder;

    protected static final int TITLE = 0;

    protected static final int MESSAGE = 1;

    private int  titleId;

    private int messageId;

    protected abstract  int[] setAlertDialog();

    protected abstract void onConfirmClick(Activity activity, DialogInterface dialog, int which);

    protected abstract void onDenyClick(Activity activity, DialogInterface dialog, int which);

    private void alertDialog(){
        activity  =  getActivity();
        builder = new android.app.AlertDialog.Builder(activity);
        int[] s = setAlertDialog();
        this.titleId = s[TITLE];
        this.messageId = s[MESSAGE];
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        alertDialog();
        builder
                .setTitle(titleId)
                .setMessage(messageId)
                .setPositiveButton(
                        getString(R.string.txtDialogoFinalAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onConfirmClick(activity, dialog, which);
                            }
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoFinalNegativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onDenyClick(activity, dialog, which);
                            }
                        }
                );

        return builder.create();
    }
}
