package pt.isel.pdm.yamba.views;

import java.util.ArrayList;
import java.util.List;

import pt.isel.pdm.yamba.R;
import pt.isel.pdm.yamba.TweetDateFormat;
import pt.isel.pdm.yamba.TwitterAsync.TwitterAsync;
import pt.isel.pdm.yamba.TwitterAsync.listeners.TimelineObtainedListener;
import pt.isel.pdm.yamba.views.models.TimelineViewModel;
import pt.isel.pdm.yamba.views.models.TweetViewModel;
import winterwell.jtwitter.Twitter.Status;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TimelineActivity extends Activity implements TimelineObtainedListener, OnItemClickListener {

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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TweetViewModel tweet = getItem(position);
			View v = convertView;
						
			TweetHolder dataHolder;
			
			if(v == null) {
				v = getLayoutInflater().inflate(R.layout.timeline_tweet, null);
				dataHolder = new TweetHolder(); 
				
				dataHolder.author = (TextView) v.findViewById(R.id.tweet_text);
				dataHolder.status = (TextView) v.findViewById(R.id.username);
				dataHolder.publicationTime = (TextView) v.findViewById(R.id.date);
			
				v.setTag(dataHolder);
			}
			else {
				dataHolder = (TweetHolder) v.getTag();
			}
			
			dataHolder.author.setText(tweet.getUsername());
			dataHolder.status.setText(tweet.getTweet());
			dataHolder.publicationTime.setText(_DateFormat.format(tweet.getTweetDate()));
			
			return v;
		}		
	}
	
	private TwitterAsync _connection;
	private TweetAdapter _adapter;
	
	private TimelineViewModel _viewModel;
	
	private View _loading, _timeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        
        _connection = TwitterAsync.connect();
        
        _connection.setTimelineObtainedListener(this);
        
        ListView list;
        
        _timeline = list = (ListView) findViewById(R.id.timeline);
		list.setAdapter(_adapter = new TweetAdapter());
		
		_loading = findViewById(R.id.timeline_loading);
		
		refreshTimeline();
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }
    
	private void refreshTimeline() {		
		_connection.getUserTimelineAsync(this);
	}
	
	private void updateViewModel(Iterable<TweetViewModel> tweets) {

		_viewModel = new TimelineViewModel(tweets);		
		
		_adapter.setData(_viewModel);
		
		_loading.setVisibility(View.GONE);
		_timeline.setVisibility(View.VISIBLE);
	}

	@Override
	public void onTimelineObtained(Iterable<Status> timeline) {
		
		List<TweetViewModel> tweets = new ArrayList<TweetViewModel>();
		
		for(Status s : timeline) {
			tweets.add(new TweetViewModel(s.id, s.text, s.user.name, s.createdAt));
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(this, StatusActivity.class));
		return true;
	}
}
