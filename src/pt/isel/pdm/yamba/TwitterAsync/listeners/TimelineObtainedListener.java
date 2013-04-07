package pt.isel.pdm.yamba.TwitterAsync.listeners;

import winterwell.jtwitter.Twitter.Status;

public interface TimelineObtainedListener {

	public void onTimelineObtained(Iterable<Status> timeline);
	
}
