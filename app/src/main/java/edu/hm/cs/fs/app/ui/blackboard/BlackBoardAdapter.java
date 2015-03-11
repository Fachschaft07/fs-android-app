package edu.hm.cs.fs.app.ui.blackboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.fk07.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.model.News;
import edu.hm.cs.fs.app.datastore.model.constants.Study;

/**
 * Created by Fabio on 04.03.2015.
 */
public class BlackBoardAdapter extends ArrayAdapter<News> {
    private final Map<Study, Integer> studyColorMap = new HashMap<>();
    private int mSelectedPosition;

    public BlackBoardAdapter(final Context context) {
        super(context, android.R.layout.simple_list_item_1);
        studyColorMap.put(Study.IF, Color.BLUE);
        studyColorMap.put(Study.GO, Color.CYAN);
        studyColorMap.put(Study.IB, Color.YELLOW);
        studyColorMap.put(Study.IC, Color.GRAY);
        studyColorMap.put(Study.IG, Color.GREEN);
        studyColorMap.put(Study.IN, Color.MAGENTA);
        studyColorMap.put(Study.IS, Color.RED);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_blackboard, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final News news = getItem(position);

        if(!news.getGroups().isEmpty()) {
            int color = studyColorMap.get(news.getGroups().get(0).getStudy());

            TextDrawable drawable = TextDrawable.builder()
                    .buildRoundRect(news.getGroups().get(0).getStudy().toString(), color, 5);
            holder.image.setImageDrawable(drawable);
        } else {
            holder.image.setImageDrawable(null);
        }

        holder.text.setText(news.getSubject());

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.imageView)
        ImageView image;
        @InjectView(R.id.textView)
        TextView text;

        public ViewHolder(final View view) {
            ButterKnife.inject(this, view);
        }
    }
}
