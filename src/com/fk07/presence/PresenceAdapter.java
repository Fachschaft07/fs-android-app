package com.fk07.presence;

import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fk07.R;

public class PresenceAdapter extends BaseAdapter {
	
	private final Map<String, String> presenceMap;
	
	private final Object[] presenceNames;
	
	private final Activity activity;
	
	public PresenceAdapter(final Activity activity, final Map<String, String> presenceMap) {
		this.presenceMap = presenceMap;
		this.activity = activity;
		presenceNames = presenceMap.keySet().toArray();
	}

	public int getCount() {
		return presenceNames.length;
	}

	public Object getItem(int position) {
		return presenceNames[position];
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			view = inflater.inflate(R.layout.presence_row, null);
		}
		
		TextView presenceName = (TextView) view.findViewById(R.id.presenceName);
		presenceName.setText((String)getItem(position));
		
		System.out.println(presenceMap.get((String)getItem(position)));
		
		ImageView presenceStatus = (ImageView) view.findViewById(R.id.presenceStatus);
		if (presenceMap.get((String)getItem(position)).equals("Busy")) {
			presenceStatus.setImageResource(R.drawable.circle_yellow_light);
		} else if (presenceMap.get((String)getItem(position)).equals("Present") || presenceMap.get((String)getItem(position)).equals("Anwesend")) {
			presenceStatus.setImageResource(R.drawable.circle_green_dark);
		}
		
		
		return view;
	}

}
