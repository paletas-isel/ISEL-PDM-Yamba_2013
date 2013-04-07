package pt.isel.pdm.yamba.views.models;

import java.util.List;

public class TimelineViewModel {

	private List<TweetViewModel> _tweets; 
	
	public TimelineViewModel(List<TweetViewModel> tweets) {
		_tweets = tweets;
	}

	public List<TweetViewModel> getTweets() {
		return _tweets;
	}		
}
