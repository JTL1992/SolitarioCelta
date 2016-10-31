package es.upm.miw.SolitarioCelta.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.upm.miw.SolitarioCelta.R;

/**
 * Created by nuonuo-jtl on 16/10/30.
 */
public class ResultsAdapter extends BaseAdapter {

    private JSONArray results;

    private Context context;

    public ResultsAdapter(Context context, JSONArray games){
        this.results = games;
        this.context = context;
    }

    public void updateAdapter(){
        results = new JSONArray();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return results.length();
    }

    @Override
    public Object getItem(int position) {
        JSONObject data = new JSONObject();
        try {
            data =  results.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.game_restore_list_item, parent, false);
            holder.nameText = (TextView) convertView.findViewById(R.id.name);
            holder.scoreText = (TextView) convertView.findViewById(R.id.score);
            holder.dateText = (TextView) convertView.findViewById(R.id.date);
            holder.timeText = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.nameText.setText(((JSONObject)getItem(position)).getString("name"));
            holder.scoreText.setText(((JSONObject)getItem(position)).getInt("score")+"");
            holder.dateText.setText(((JSONObject)getItem(position)).getString("date"));
            holder.timeText.setText(((JSONObject)getItem(position)).getString("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView nameText, scoreText, dateText ,timeText;
    }
}
