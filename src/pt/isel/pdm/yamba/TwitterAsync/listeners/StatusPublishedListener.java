package pt.isel.pdm.yamba.TwitterAsync.listeners;

import winterwell.jtwitter.Twitter.Status;

public interface StatusPublishedListener {

	public void onStatusPublished(Status status);
	
}
