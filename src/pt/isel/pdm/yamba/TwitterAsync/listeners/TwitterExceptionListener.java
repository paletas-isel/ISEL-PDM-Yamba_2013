package pt.isel.pdm.yamba.TwitterAsync.listeners;

import pt.isel.pdm.yamba.exceptions.TwitterException;

public interface TwitterExceptionListener {

	void onExceptionThrown(TwitterException e);
	
}
