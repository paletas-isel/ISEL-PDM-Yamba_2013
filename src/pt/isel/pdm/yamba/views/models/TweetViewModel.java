package pt.isel.pdm.yamba.views.models;

import java.util.Date;

public class TweetViewModel {

	private String _tweet;
	
	private String _username;

	private Date _tweetDate;
	
	public TweetViewModel(String tweet, String username, Date tweetDate) {
		_tweet = tweet;
		_username = username;
		_tweetDate = tweetDate;
	}
	
	/**
	 * @return the _tweet
	 */
	public String getTweet() {
		return _tweet;
	}

	/**
	 * @param _tweet the _tweet to set
	 */
	public void setTweet(String _tweet) {
		this._tweet = _tweet;
	}

	/**
	 * @return the _username
	 */
	public String getUsername() {
		return _username;
	}

	/**
	 * @param _username the _username to set
	 */
	public void setUsername(String _username) {
		this._username = _username;
	}

	/**
	 * @return the _tweetDate
	 */
	public Date getTweetDate() {
		return _tweetDate;
	}

	/**
	 * @param _tweetDate the _tweetDate to set
	 */
	public void setTweetDate(Date _tweetDate) {
		this._tweetDate = _tweetDate;
	}	
}
