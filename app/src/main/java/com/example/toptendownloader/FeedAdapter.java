package com.example.toptendownloader;


public class FeedAdapter extends ArrayAdapter {
	private static final String TAG = "FeedAdapter"; 
	Log.d(TAG, "FeedAdapter: ");
	
	private final int layoutResource;
	private final LayoutInflater layoutInflater;
	
	private List<FeedEntry> applications;
	
	public FeedAdapter(Context context, int resource, List<FeedEntry> applications){
		super(context, resource);
		this.layoutResource = resource;
		this.layoutInflater = LayoutInflater.from(context);
		this.applications = applications;
		
	}
		
	
}
