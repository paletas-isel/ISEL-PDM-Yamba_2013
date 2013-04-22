package pt.isel.pdm.yamba.views.models;

import pt.isel.java.Func;
import pt.isel.pdm.yamba.TwitterAsync.TwitterAsync;
import pt.isel.pdm.yamba.TwitterAsync.listeners.StatusPublishedListener;
import winterwell.jtwitter.Twitter.Status;
import android.content.Context;

public class StatusViewModel {
	
	public static final int STATUS_MAXSIZE_DEFAULT = 140;
	
	private final Context _context;
	private String _message;
	private int _statusMaxSize;
	
	public StatusViewModel(Context context) {
		
		this._context = context;
		setMessage("");
		setStatusMaxSize(STATUS_MAXSIZE_DEFAULT);
	}
	
	public void setMessage(String message) {
		_message = message;
	}
	
	public String getMessage() {
		return _message;
	}
	
	public void setStatusMaxSize(int size) {
		_statusMaxSize = size;
	}
	
	public int getRemainingCharacters() {
		return _statusMaxSize - _message.length();
	}
	
	public void sendStatusCommandAsync(final Func<Void, Status> callback) {
		
		TwitterAsync twitter = TwitterAsync.connect();
		
		final StatusPublishedListener listener = new StatusPublishedListener() {
			
			@Override
			public void onStatusPublished(Status status) {
				callback.execute(status);
			}
		};
		
		twitter.setStatusPublishedListener(listener);
		twitter.updateStatusAsync(_context, _message);
	}
}
