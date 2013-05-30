package pt.isel.pdm.yamba.twitter.listeners;

import pt.isel.pdm.yamba.exceptions.TwitterException;

public interface TwitterExceptionListener {

	void onExceptionThrown(TwitterException e);
	
}
