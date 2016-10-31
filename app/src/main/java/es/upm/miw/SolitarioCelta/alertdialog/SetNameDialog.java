package es.upm.miw.SolitarioCelta.alertdialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.widget.EditText;

import es.upm.miw.SolitarioCelta.activities.MainActivity;
import es.upm.miw.SolitarioCelta.R;

/**
 * Created by nuonuo-jtl on 16/10/30.
 */
public class SetNameDialog extends AlertDialog{

    private EditText editText;


    public void addInputText(){
        editText = new EditText(activity);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(editText);
    }
    @Override
    protected int[] setAlertDialog() {
        addInputText();
        int[] i = new int[2];
        i[AlertDialog.TITLE] = R.string.txtNameResultDialogoTitulo;
        i[AlertDialog.MESSAGE] = R.string.txtNameDialogoPregunta;
        return i;
    }

    @Override
    protected void onConfirmClick(Activity activity, DialogInterface dialog, int which) {
        MainActivity main = (MainActivity) activity;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(main);
        SharedPreferences.Editor editor = sharedPref.edit();
        String name = editText.getText().toString();
        editor.putString("nombreJugador", name).apply();
        main.saveToFile(name);
    }

    @Override
    protected void onDenyClick(Activity activity, DialogInterface dialog, int which) {
        MainActivity main = (MainActivity) activity;
        main.saveToFile("anonimo");
        dialog.dismiss();
    }
}
