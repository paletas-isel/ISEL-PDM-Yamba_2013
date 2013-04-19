package pt.isel.pdm.yamba.views.models;

import pt.isel.pdm.yamba.TwitterAsync.TwitterAsync;
import pt.isel.pdm.yamba.TwitterAsync.listeners.StatusPublishedListener;
import winterwell.jtwitter.Twitter.Status;
import android.content.Context;
import android.util.Log;

public class StatusViewModel {
	
	private final Object SendStatusLockObj = new Object();
	
	private static final int STATUS_MAX_LENGTH = 140;
	private static final int STATUS_PUBLISH_TIMEOUT = 3000;
	
	private final Context _context;
	private String _message;
	
	public StatusViewModel(Context context) {
		
		this._context = context;
		setMessage("");
	}
	
	public void setMessage(String message) {
		_message = message;
	}
	
	public String getMessage() {
		return _message;
	}
	
	public int getRemainingCharacters() {
		return STATUS_MAX_LENGTH - _message.length();
	}
	
	public void sendStatusCommand() {
		
		TwitterAsync twitter = TwitterAsync.connect();
		
		final StatusPublishedListener listener = new StatusPublishedListener() {
			
			@Override
			public void onStatusPublished(Status status) {
				
				synchronized(SendStatusLockObj) {
					SendStatusLockObj.notify();
				}
			}
		};
		
		twitter.setStatusPublishedListener(listener);
		
		synchronized(SendStatusLockObj) {
			
			twitter.updateStatusAsync(_context, _message);
			
			try {
				SendStatusLockObj.wait(STATUS_PUBLISH_TIMEOUT);
			} catch (Exception e) {
				Log.d("StatusViewModel", e.getMessage());
			}
		}
	}
}
