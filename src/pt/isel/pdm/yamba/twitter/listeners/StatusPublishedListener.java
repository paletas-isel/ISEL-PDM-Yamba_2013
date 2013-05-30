package pt.isel.pdm.yamba.twitter.listeners;

import winterwell.jtwitter.Twitter.Status;

public interface StatusPublishedListener {

	public void onStatusPublished(Status status);
	
}
