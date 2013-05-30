package pt.isel.pdm.yamba.twitter.listeners;

import winterwell.jtwitter.Twitter.Status;

public interface TimelineObtainedListener {

	public void onTimelineObtained(Iterable<Status> timeline);
	
}
