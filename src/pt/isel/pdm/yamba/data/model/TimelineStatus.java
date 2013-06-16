package pt.isel.pdm.yamba.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;

public class TimelineStatus {
	private long _id;
	private long _serverId;
	private String _message, _username;
	private Date _publicationDate;
	private long _replyTo;
	private boolean _published;

	public TimelineStatus(long serverId, String username, String message, Date publicationDate) {
		_serverId = serverId;
		_username = username;
		_message = message;
		_publicationDate = publicationDate;
	}
	
	public TimelineStatus(long serverId, String username, String message, Date publicationDate, int replyTo) {
		this(serverId, username, message, publicationDate);
		_replyTo = replyTo;
	}
	
	public TimelineStatus(long serverId, String username, String message, Date publicationDate, int replyTo, boolean published) {
		this(serverId, username, message, publicationDate, replyTo);
		_published = published;
	}
	
	public long getID() {
		return _id;
	}
	
	public void setID(long id) {
		_id = id;
	}
	
	public long getServerID() {
		return _serverId;
	}
	
	public void setServerID(long id) {
		_serverId = id;
	}
	
	public String getUsername() {
		return _username;
	}
	
	public void setUsername(String username) {
		_username = username;
	}
	
	public String getMessage() {
		return _message;
	}
	
	public void setMessage(String message) {
		_message = message;
	}
	
	public Date getDate() {
		return _publicationDate;
	}
	
	public void setDate(Date publicationDate) {
		_publicationDate = publicationDate;
	}
	
	public long getReplyTo() {
		return _replyTo;
	}
	
	public void setReplyTo(long replyTo) {
		_replyTo = replyTo;
	}
	
	public boolean isPublished() {
		return _published;
	}
	
	public void setPublished(boolean published) {
		_published = published;
	}
	
	public static TimelineStatus from(Twitter.Status status) {
		TimelineStatus timelineStatus = new TimelineStatus(status.getId(), status.getUser().getName(), status.getText(), status.getCreatedAt());
		if(status.inReplyToStatusId == null)
			timelineStatus.setReplyTo(0);
		else
			timelineStatus.setReplyTo(status.inReplyToStatusId);
		timelineStatus.setPublished(true);
		return timelineStatus;
	}
	
	public static List<TimelineStatus> from(Iterable<Status> statuses) {
		List<TimelineStatus> timeline = new ArrayList<TimelineStatus>();
		for(Status status : statuses) {
			timeline.add(TimelineStatus.from(status));
		}
		return timeline;
	}
}
