package es.upm.miw.SolitarioCelta.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by nuonuo-jtl on 16/10/29.
 */
public class Result {

    private int score;

    private String name;

    private String time;

    private String date;

    public Result(String name, int score, String time){
        this.time = time;
        this.name = name;
        this.score = score;
        this.date = getTimeString();

    }

    private String  getTimeString() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int data = c.get(Calendar.DAY_OF_MONTH);
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        return data+"-"+month+"-"+year+" "+hora+":"+min;
    }

    public int getScore() {
        return score;
    }

    public JSONObject getResultJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name).put("score",score).put("date",date).put("time",time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONArray getOrderedResults(JSONArray results) {
        ArrayList<JSONObject> resultList = new ArrayList<>();
        for (int i = 0; i < results.length(); i++){
            try {
                resultList.add(results.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < resultList.size(); i++){
            try {
                if (this.getScore() <= resultList.get(i).getInt("score")){
                    resultList.add(i,this.getResultJSONObject());
                    break;
                }else{
                    if (i == resultList.size()){
                        resultList.add(this.getResultJSONObject());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (JSONObject jsonObject: resultList){
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
