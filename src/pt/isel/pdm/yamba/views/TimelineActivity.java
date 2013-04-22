package pt.isel.pdm.yamba.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pt.isel.pdm.yamba.R;
import pt.isel.pdm.yamba.TweetDateFormat;
import pt.isel.pdm.yamba.TwitterAsync.TwitterAsync;
import pt.isel.pdm.yamba.TwitterAsync.listeners.TimelineObtainedListener;
import pt.isel.pdm.yamba.views.models.TimelineViewModel;
import pt.isel.pdm.yamba.views.models.TweetViewModel;
import winterwell.jtwitter.Twitter.Status;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TimelineActivity extends YambaBaseActivity implements TimelineObtainedListener, OnItemClickListener {

	private static final String TWEET_LIST_TIMELINE = "TIMELINE_TWEETS";
	
	private static int _MaxSavedTweets = 20; 
	
	public TimelineActivity() {
		super(TimelineActivity.class, R.menu.timeline);
	}

	private static final TweetDateFormat _DateFormat = new TweetDateFormat();
	
	private static class TweetHolder {
		
		TextView author, status, publicationTime;
		
	}
	
	private class TweetAdapter extends ArrayAdapter<TweetViewModel> {
		
		public TweetAdapter() {
			super(TimelineActivity.this, 0);
		}
		
		public TweetAdapter(TimelineViewModel model) {
			super(TimelineActivity.this, 0, model.getTweets());
		}
		
		public void setData(TimelineViewModel model) {
			clear();
			
			for(TweetViewModel status : model.getTweets()) {
				add(status);
			}
		}

		private Date getElapsedTimeFrom(Date from) {
			long publicationMillis = from.getTime();
			long currentDateMillis = Calendar.getInstance().getTimeInMillis();
			
			return new Date(currentDateMillis - publicationMillis);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TweetViewModel tweet = getItem(position);
			View v = convertView;
						
			TweetHolder dataHolder;
			
			if(v == null) {
				v = getLayoutInflater().inflate(R.layout.timeline_tweet, null);
				dataHolder = new TweetHolder(); 
				
				dataHolder.author = (TextView) v.findViewById(R.id.tweet_username);
				dataHolder.status = (TextView) v.findViewById(R.id.tweet_text);
				dataHolder.publicationTime = (TextView) v.findViewById(R.id.tweet_date);
			
				v.setTag(dataHolder);
			}
			else {
				dataHolder = (TweetHolder) v.getTag();
			}
			
			dataHolder.author.setText(tweet.getUsername());
			dataHolder.status.setText(tweet.getTweet());
			dataHolder.publicationTime.setText(_DateFormat.format(getElapsedTimeFrom(tweet.getTweetDate())));
			
			return v;
		}		
	}
	
	private TwitterAsync _connection;
	private TweetAdapter _adapter;
	
	private TimelineViewModel _viewModel;
	
	private View _loading, _timeline;
	private boolean _refreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        
        _connection = TwitterAsync.connect();        
        _connection.setTimelineObtainedListener(this);
        
        ListView list;
        
        _timeline = list = (ListView) findViewById(R.id.timeline);
		list.setAdapter(_adapter = new TweetAdapter());
		list.setOnItemClickListener(this);
		
		_loading = findViewById(R.id.timeline_loading);
		WebView loadingWV = (WebView) findViewById(R.id.timeline_loading_wv);
		loadingWV.loadUrl(getString(R.string.loading_image));
		
		if(savedInstanceState != null && savedInstanceState.containsKey(TWEET_LIST_TIMELINE))
		{
			Log.d(this.getClass().getName(), "Loading instance data.");
			updateViewModel(savedInstanceState.getParcelableArrayList(TWEET_LIST_TIMELINE));
		}
		else
		{
			refreshTimeline();
		}
    }
    
    @Override
    protected void onMenuLoaded(Menu menu)
    {
    	menu.findItem(R.id.action_refresh).setEnabled(!_refreshing);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		final int itemId = item.getItemId();		
		if(itemId == R.id.action_refresh)
		{		
			refreshTimeline();
		}		
		return super.onOptionsItemSelected(item);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d(this.getClass().getName(), "Loading instance data.");
		updateViewModel(savedInstanceState.getParcelableArrayList(TWEET_LIST_TIMELINE));
		super.onRestoreInstanceState(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(this.getClass().getName(), "Saving instance data.");
		outState.putParcelableArrayList(TWEET_LIST_TIMELINE, _viewModel.getTweets());
		super.onSaveInstanceState(outState);
	}

	private void refreshTimeline() {	
		final Menu menu = getMenu();					
		runOnUiThread(new Runnable() {
			@Override
			public void run() {					
				_loading.setVisibility(View.VISIBLE);
				_timeline.setVisibility(View.GONE);
				if(menu != null) {
					menu.findItem(R.id.action_refresh).setEnabled(false);	
				}				
			}}
		);
		_refreshing = true;
		_connection.getUserTimelineAsync(this);
	}
	
	@SuppressWarnings("unchecked")
	private void updateViewModel(ArrayList<? super TweetViewModel> tweets) {
		_viewModel = new TimelineViewModel((ArrayList<TweetViewModel>) tweets);				
		_adapter.setData(_viewModel);
		getMenu().findItem(R.id.action_refresh).setEnabled(true);			
		_loading.setVisibility(View.GONE);
		_timeline.setVisibility(View.VISIBLE);
	}

	@Override
	public void onTimelineObtained(Iterable<Status> timeline) {		
		_refreshing = false;
		int count = 0;
		ArrayList<TweetViewModel> tweets = new ArrayList<TweetViewModel>();		
		for(Status s : timeline) {
			if(++count < _MaxSavedTweets) {
				tweets.add(new TweetViewModel(s.id, s.text, s.user.name, s.createdAt));
			}
		}		
		updateViewModel(tweets);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(this, DetailsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.putExtra(DetailsActivity.TWEET_VIEW_PARAMETER, _viewModel.getTweets().get(arg2));
		startActivity(intent);
	}
}
