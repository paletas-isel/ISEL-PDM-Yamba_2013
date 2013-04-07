package pt.isel.pdm.yamba.views.models;

import java.util.ArrayList;

public class TimelineViewModel {

	private ArrayList<TweetViewModel> _tweets; 
	
	public TimelineViewModel(ArrayList<TweetViewModel> tweets) {
		_tweets = tweets;
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
			
			_tweets = t;
		}
	}

	public ArrayList<TweetViewModel> getTweets() {
		return _tweets;
	}		
}
