package com.fk07.mensa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fk07.R;

public class InfoDialog {
	
	final Dialog dialog;
	
	public InfoDialog(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		LayoutInflater inflater = activity.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.food_dialog_info, null);
		
		Button positiveButton = (Button) layout.findViewById(R.id.positiveButton);
		positiveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		builder.setView(layout);
		dialog = builder.create();
	}
	
	public void show() {
		dialog.show();
	}

}
