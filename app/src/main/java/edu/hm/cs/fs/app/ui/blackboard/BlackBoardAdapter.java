package edu.hm.cs.fs.app.ui.blackboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import edu.hm.cs.fs.app.datastore.model.News;

/**
 * Created by Fabio on 04.03.2015.
 */
public class BlackBoardAdapter extends ArrayAdapter<News> {
    public BlackBoardAdapter(final Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    static class ViewHolder {

    }
}
