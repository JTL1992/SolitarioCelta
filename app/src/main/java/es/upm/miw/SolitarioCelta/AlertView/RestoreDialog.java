package es.upm.miw.SolitarioCelta.AlertView;

import android.app.Activity;
import android.content.DialogInterface;

import es.upm.miw.SolitarioCelta.Activities.MainActivity;
import es.upm.miw.SolitarioCelta.R;

/**
 * Created by nuonuo-jtl on 16/10/29.
 */
public class RestoreDialog extends AlertDialog {
    @Override
    protected int[] setAlertDialog() {
        int[] i = new int[2];
        i[AlertDialog.TITLE] = R.string.txtDialogoRestoreFinalTitulo;
        i[AlertDialog.MESSAGE] = R.string.txtDialogoRestoreFinalPregunta;
        return i;
    }

    @Override
    protected void onConfirmClick(Activity activity, DialogInterface dialog, int which) {
        MainActivity main = (MainActivity) activity;
        main.showGameListDialog();
    }

    @Override
    protected void onDenyClick(Activity activity, DialogInterface dialog, int which) {
        MainActivity main = (MainActivity) activity;
        main.timeTaskRestart();
        dialog.dismiss();
    }
}
