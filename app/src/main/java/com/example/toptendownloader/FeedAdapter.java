package com.example.toptendownloader;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter<T extends FeedEntry> extends ArrayAdapter {
	private static final String TAG = "FeedAdapter"; 
	
	private final int layoutResource;
	private final LayoutInflater layoutInflater;
	private List<T> applications;
	
	public FeedAdapter(Context context, int resource, List<T> applications){
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

		ViewHolder viewHolder;

		if (convertView == null){
			Log.d(TAG, "getView: called with null");
			convertView = layoutInflater.inflate(layoutResource, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			Log.d(TAG, "getView: provided convertView");
			viewHolder = (ViewHolder)convertView.getTag();
		}

		T currentApp = applications.get(position);
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
