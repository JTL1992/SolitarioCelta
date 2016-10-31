package es.upm.miw.SolitarioCelta.AlertView;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.upm.miw.SolitarioCelta.Activities.MainActivity;
import es.upm.miw.SolitarioCelta.R;
import es.upm.miw.SolitarioCelta.utils.FileUtils;

/**
 * Created by nuonuo-jtl on 16/10/30.
 */
public class RestoreNameListDialog extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity main  = (MainActivity) getActivity();
        FileUtils fileUtils = new FileUtils(main);
        final JSONArray dataJSONArray = fileUtils.readFile2JsonArray("SolitarioCelta");
        final CharSequence[] items = new CharSequence[dataJSONArray.length()];
        try {
            for (int i = 0; i < dataJSONArray.length(); i++){
                JSONObject jsonObject = dataJSONArray.getJSONObject(i);
                items[i] = jsonObject.getString("name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(main);
        builder
                .setTitle("elige una partida a recuperar")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            JSONObject gameObject = dataJSONArray.getJSONObject(which);
                            main.gameRestart(gameObject.getString("time"),gameObject.getInt("ficha"),gameObject.getString("seria"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(
                        getString(R.string.txtDialogoFinalNegativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                main.timeTaskRestart();
                                dialog.dismiss();
                            }
                        }
                );

        return builder.create();
    }
}
