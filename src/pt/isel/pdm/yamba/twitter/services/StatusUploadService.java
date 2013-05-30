package pt.isel.pdm.yamba.twitter.services;

import pt.isel.pdm.yamba.twitter.TwitterAsync;
import pt.isel.pdm.yamba.twitter.listeners.StatusPublishedListener;
import winterwell.jtwitter.Twitter.Status;
import android.app.IntentService;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class StatusUploadService extends IntentService {
	
	private static final String _name = StatusUploadService.class.getSimpleName();
	
	public StatusUploadService() {
		super(_name);
	}
	
	@Override
	protected void onHandleIntent(final android.content.Intent intent) {
		
		final String message = intent.getStringExtra(Keys.Intent.Message);
		
		TwitterAsync twitter = TwitterAsync.connect();
		
		twitter.setStatusPublishedListener(new StatusPublishedListener() {

			@Override
			public void onStatusPublished(Status status) {
				
				final Messenger messenger = (Messenger) intent.getExtras().get(Keys.Intent.Messenger);
				final Message msg = Message.obtain();
				
				try {
					messenger.send(msg);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		twitter.updateStatusAsync(this, message);
	}
	
	public static class Keys {
		
		public static class Intent {
			
			public static final String Message = "message";
			public static final String Messenger = "messenger";
		}
	}
}
