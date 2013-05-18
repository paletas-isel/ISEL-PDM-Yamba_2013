package pt.isel.pdm.yamba.views.models;

import java.util.Date;

import winterwell.jtwitter.Twitter.Status;

import android.os.Parcel;
import android.os.Parcelable;

public class TweetViewModel implements Parcelable {

	private long _ID;

	private String _tweet;
	
	private String _username;

	private Date _tweetDate;
	
	public TweetViewModel(long id, String tweet, String username, Date tweetDate) {
		_ID = id;
		_tweet = tweet;
		_username = username;
		_tweetDate = tweetDate;
	}
	
	@SuppressWarnings("deprecation")
	private TweetViewModel(Parcel in) {
		_ID = in.readLong();
		_username = in.readString();
		_tweet = in.readString();
		_tweetDate = new Date(in.readString());
	}
	
	public static TweetViewModel createFrom(Status status) {
		return new TweetViewModel(status.getId(), status.getUser().getName(), status.getText(), status.getCreatedAt());
	}
	
	public static final Parcelable.Creator<TweetViewModel> CREATOR = new Parcelable.Creator<TweetViewModel>() {
		public TweetViewModel createFromParcel(Parcel in) {
		    return new TweetViewModel(in);
		}
		
		public TweetViewModel[] newArray(int size) {
		    return new TweetViewModel[size];
		}
	};
	
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
	
	/**
	 * @return the _ID
	 */
	public long getID() {
		return _ID;
	}

	/**
	 * @param _ID the _ID to set
	 */
	public void setID(long ID) {
		this._ID = ID;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeLong(_ID);
		arg0.writeString(_username);
		arg0.writeString(_tweet);
		arg0.writeString(_tweetDate.toString());		
	}
}
