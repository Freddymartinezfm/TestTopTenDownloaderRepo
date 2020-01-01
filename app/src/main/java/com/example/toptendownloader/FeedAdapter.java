package com.example.toptendownloader;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
	private static final String TAG = "FeedAdapter"; 
	
	private final int layoutResource;
	private final LayoutInflater layoutInflater;
	private List<FeedEntry> applications;
	
	public FeedAdapter(Context context, int resource, List<FeedEntry> applications){
		super(context, resource);
		Log.d(TAG, "FeedAdapter: ");
		this.layoutResource = resource;
		this.layoutInflater = LayoutInflater.from(context);
		this.applications = applications;
	}
	
	
	@Override 
	public int getCount(){
		return applications.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

//		// method one
//		View view = layoutInflater.inflate(layoutResource, parent, false);
//		 TextView tvName = view.findViewById(R.id.tvName);
//		 TextView tvArtist = view.findViewById(R.id.tvArtist);
//		 TextView tvSummary = view.findViewById(R.id.tvSummary);
//
//		FeedEntry currentApp = applications.get(position);
//		tvName.setText(currentApp.getName());
//		tvArtist.setText(currentApp.getArtist());
//		tvSummary.setText(currentApp.getSummary());
//
//		return view;
//	}



		// faster method, reusing views and only calling findViewById once
		ViewHolder viewHolder;

		if (convertView == null){
			convertView = layoutInflater.inflate(layoutResource, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		FeedEntry currentApp = applications.get(position);
		viewHolder.tvName.setText(currentApp.getName());
		viewHolder.tvArtist.setText(currentApp.getArtist());
		viewHolder.tvSummary.setText(currentApp.getSummary());

		return convertView;
	}
	
	private class ViewHolder {
		private TextView tvName;
		private TextView tvArtist;
		private TextView tvSummary;
		
		public ViewHolder(View v){
			tvName = v.findViewById(R.id.tvName);
			tvArtist = v.findViewById(R.id.tvArtist);
			tvSummary = v.findViewById(R.id.tvSummary);


		}
	}
	
	
	
}
