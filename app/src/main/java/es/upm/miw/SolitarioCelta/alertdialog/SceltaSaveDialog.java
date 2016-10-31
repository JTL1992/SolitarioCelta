package es.upm.miw.SolitarioCelta.alertdialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.upm.miw.SolitarioCelta.activities.MainActivity;
import es.upm.miw.SolitarioCelta.R;
import es.upm.miw.SolitarioCelta.utils.FileUtils;

/**
 * Created by nuonuo-jtl on 16/10/29.
 */
public class SceltaSaveDialog extends AlertDialog {

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
        i[AlertDialog.TITLE] = R.string.txtNameDialogoTitulo;
        i[AlertDialog.MESSAGE] = R.string.txtNameDialogoPregunta;
        return i;
    }

    @Override
    protected void onConfirmClick(Activity activity, DialogInterface dialog, int which) {
        MainActivity main = (MainActivity) activity;
        FileUtils fileUtils = new FileUtils(main);
        String name = editText.getText().toString();
        String seria = main.juego.serializaTablero();
        JSONArray oldJsonArray = fileUtils.readFile2JsonArray("SolitarioCelta");
        try {
            JSONObject dataJSONObject = new JSONObject().put("name", name);
            dataJSONObject.put("seria",seria);
            dataJSONObject.put("time",main.getTimerTime());
            dataJSONObject.put("ficha",main.juego.numeroFichas());
            if (null == oldJsonArray)
                oldJsonArray = new JSONArray();
            oldJsonArray.put(dataJSONObject);
            Log.v("data Array",oldJsonArray.toString());
            fileUtils.saveJsonArray2File(oldJsonArray, "SolitarioCelta");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        main.timeTaskRestart();
        Toast.makeText(main, "Ha guardado en " + fileUtils.getFileStreamPath("SolitarioCelta"), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDenyClick(Activity activity, DialogInterface dialog, int which) {
        MainActivity main = (MainActivity) activity;
        main.timeTaskRestart();
        dialog.dismiss();
    }
}
