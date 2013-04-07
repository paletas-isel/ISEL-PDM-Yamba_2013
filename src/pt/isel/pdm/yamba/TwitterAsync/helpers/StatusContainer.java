package pt.isel.pdm.yamba.TwitterAsync.helpers;

public class StatusContainer{
		
	private String _status;
	private long _inReplyTo;

	public StatusContainer(String status) {
		this(status, -1);
	}
	
	public StatusContainer(String status, long inReplyTo) {
		_status = status;
		_inReplyTo = inReplyTo;
	}
	
	public String getStatus() {
		return _status;
	}
	
	public long inReplyTo() {
		return _inReplyTo;
	}
	
	public boolean isReply() {
		return _inReplyTo != -1;
	}
	
	public static StatusContainer create(String status, long inReplyTo) {
		return new StatusContainer(status, inReplyTo);
	}
	
	public static StatusContainer create(String status) {
		return new StatusContainer(status, -1);
	}
}