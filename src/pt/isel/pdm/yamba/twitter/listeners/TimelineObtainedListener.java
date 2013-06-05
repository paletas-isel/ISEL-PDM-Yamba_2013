package pt.isel.pdm.yamba.twitter.listeners;

import pt.isel.pdm.yamba.data.model.TimelineStatus;

public interface TimelineObtainedListener {

	public void onTimelineObtained(Iterable<TimelineStatus> timeline);
	
}
