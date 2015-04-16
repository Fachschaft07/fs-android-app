package edu.hm.cs.fs.app.ui.blackboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.fk07.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.News;
import edu.hm.cs.fs.app.datastore.model.constants.Study;

/**
 * Created by Fabio on 04.03.2015.
 */
public class BlackBoardAdapter extends ArrayAdapter<News> {
    private static final int FONT_SIZE = 37;
    private final Map<Study, Integer> studyColorMap = new HashMap<>();
    private int mColorMixed;
    private int mColorAll;

    public BlackBoardAdapter(final Context context) {
        super(context, android.R.layout.simple_list_item_1);
        studyColorMap.put(Study.IF, context.getResources().getColor(R.color.study_group_if));
        studyColorMap.put(Study.GO, context.getResources().getColor(R.color.study_group_go));
        studyColorMap.put(Study.IB, context.getResources().getColor(R.color.study_group_ib));
        studyColorMap.put(Study.IC, context.getResources().getColor(R.color.study_group_ic));
        studyColorMap.put(Study.IG, context.getResources().getColor(R.color.study_group_ig));
        studyColorMap.put(Study.IN, context.getResources().getColor(R.color.study_group_in));
        studyColorMap.put(Study.IS, context.getResources().getColor(R.color.study_group_is));
        mColorMixed = context.getResources().getColor(R.color.study_group_mixed);
        mColorAll = context.getResources().getColor(R.color.study_group_all);
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
        StringBuilder studyGroupsStr = new StringBuilder();
        List<Study> studyGroups = new ArrayList<>();
        for (Group group : news.getGroups()) {
            if(!studyGroups.contains(group.getStudy())) {
                if(!studyGroups.isEmpty()) {
                    studyGroupsStr.append(", ");
                }
                studyGroups.add(group.getStudy());
                studyGroupsStr.append(group.getStudy().toString());
            }
        }

        final TextDrawable drawable;
        if(studyGroups.isEmpty() || studyGroups.size() == Study.values().length) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .fontSize(FONT_SIZE)
                    .endConfig()
                    .buildRoundRect(getContext().getString(R.string.all), mColorAll, 5);
        } else if(studyGroups.size() == 1) {
            final Study study = studyGroups.get(0);
            int color = studyColorMap.get(study);

            drawable = TextDrawable.builder()
                    .beginConfig()
                    .fontSize(FONT_SIZE)
                    .endConfig()
                    .buildRoundRect(studyGroupsStr.toString(), color, 5);
        } else {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .fontSize(FONT_SIZE)
                    .endConfig()
                    .buildRoundRect(studyGroupsStr.toString(), mColorMixed, 5);
        }

        holder.image.setImageDrawable(drawable);
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
