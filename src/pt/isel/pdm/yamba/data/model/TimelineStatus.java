package pt.isel.pdm.yamba.data.model;

import java.util.Date;

public class TimelineStatus {
	private long _id;
	private String _message;
	private Date _publicationDate;
	private int _replyTo;
	private boolean _published;

	public TimelineStatus(String message, Date publicationDate) {
		_message = message;
		_publicationDate = publicationDate;
	}
	
	public TimelineStatus(String message, Date publicationDate, int replyTo) {
		this(message, publicationDate);
		_replyTo = replyTo;
	}
	
	public TimelineStatus(String message, Date publicationDate, int replyTo, boolean published) {
		this(message, publicationDate, replyTo);
		_published = published;
	}
	
	public long getID() {
		return _id;
	}
	
	public void setID(long id) {
		_id = id;
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
	
	public int getReplyTo() {
		return _replyTo;
	}
	
	public void setReplyTo(int replyTo) {
		_replyTo = replyTo;
	}
	
	public boolean isPublished() {
		return _published;
	}
	
	public void setPublished(boolean published) {
		_published = published;
	}
}
