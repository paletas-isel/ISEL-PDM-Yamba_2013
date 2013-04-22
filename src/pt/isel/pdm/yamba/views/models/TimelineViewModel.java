package pt.isel.pdm.yamba.views.models;

import java.util.ArrayList;
import java.util.List;

public class TimelineViewModel {

	public static int MAX_SAVED_TWEETS = 20;	
	
	private ArrayList<TweetViewModel> _tweets; 
	private boolean _refreshing = false;
	private int _maxSavedTweets = MAX_SAVED_TWEETS; 
	
	public TimelineViewModel() {
		
	}
	
	public TimelineViewModel(ArrayList<TweetViewModel> tweets) {
		setTweets(tweets);
	}
	
	public TimelineViewModel(Iterable<TweetViewModel> tweets) {
		if(tweets instanceof ArrayList) {
			_tweets = (ArrayList<TweetViewModel>) tweets;
		}
		else {
			ArrayList<TweetViewModel> t = new ArrayList<TweetViewModel>();
			
			for(TweetViewModel s : tweets) {
				t.add(s);
			}

			setTweets(t);
		}
	}

	private void limitTweetsInMemory(ArrayList<TweetViewModel> tweets) {
		if(tweets == null || tweets.isEmpty()) return;
		
		if(tweets.size() > getMaxSavedTweets()) {
			List<TweetViewModel> subTweets = _tweets.subList(0, getMaxSavedTweets());
			if(subTweets instanceof ArrayList) {
				setTweets((ArrayList<TweetViewModel>) subTweets);
			}
			else {
				ArrayList<TweetViewModel> t = new ArrayList<TweetViewModel>();
				for(TweetViewModel s : subTweets) {
					t.add(s);
				}
				setTweets(t);
			}
		}
	}
	
	public void setTweets(ArrayList<TweetViewModel> tweets) {
		_tweets = tweets;
		limitTweetsInMemory(tweets);
	}

	public ArrayList<TweetViewModel> getTweets() {
		return _tweets;
	}

	public boolean isRefreshing() {
		return _refreshing;
	}

	public void setRefreshing(boolean refreshing) {
		this._refreshing = refreshing;
	}

	public int getMaxSavedTweets() {
		return _maxSavedTweets;
	}

	public void setMaxSavedTweets(int maxSavedTweets) {
		this._maxSavedTweets = maxSavedTweets;
		limitTweetsInMemory(_tweets);
	}	
}
