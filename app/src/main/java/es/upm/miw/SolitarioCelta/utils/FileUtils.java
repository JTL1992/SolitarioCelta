package es.upm.miw.SolitarioCelta.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by nuonuo-jtl on 16/10/29.
 *
 */
public class FileUtils {
    private Context context;

    public FileUtils(Context ct){
        this.context = ct;
    }

    public void saveString2File(String s, String fileName) {
        FileOutputStream outputStream;
        try {
            outputStream =  context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
//            Toast.makeText(ct, "Ha guardado en " + getFileStreamPath(filename), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveJsonArray2File(JSONArray jsonArray, String fileName){
        String data = jsonArray.toString();
        saveString2File(data, fileName);
    }

    public  String getFileStreamPath(String fileName){
        String path = "";
        try{
            path = context.getFileStreamPath(fileName).toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return path;
    }

    public String readFile2String(String fileName){
        StringBuilder data = new StringBuilder("");
        try{
            FileInputStream fin = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader( fin ) ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;
            String fileString = buffreader.readLine();
            while (fileString != null){
                data.append(fileString);
                fileString = buffreader.readLine();
            }
            fin.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return data.toString();
    }

    public JSONArray readFile2JsonArray(String fileName){
        String data = readFile2String(fileName);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public void deleteFile(String fileName){
        String dir = context.getFilesDir().getAbsolutePath();
        File file = new File(dir, fileName);
        boolean b = file.delete();
        Log.w("Delete Check", "File deleted: " + dir + "/"+ fileName + b);
    }

}
