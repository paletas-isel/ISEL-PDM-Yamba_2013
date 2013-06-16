package pt.isel.pdm.yamba.twitter.services;

import java.util.Date;

import pt.isel.pdm.yamba.data.model.TimelineStatus;
import pt.isel.pdm.yamba.data.model.TimelineStatusDataSource;
import pt.isel.pdm.yamba.twitter.TwitterAsync;
import pt.isel.pdm.yamba.twitter.tasks.StatusPublicationAsync;
import android.app.IntentService;
import android.content.Intent;
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
		
		final int type = intent.getIntExtra(Values.Intent.Type, Values.Intent.Op.PublishMessage);

		switch(type) {
		
			case Values.Intent.Op.PublishMessage:
				sendMessage(intent, (publishMessage(intent))? MessagePublishingStatus.Published : MessagePublishingStatus.Pending);
			break;
			
			case Values.Intent.Op.PublishPending:
				dispatchPendingStatus();				
			break;
		}
	}
	
	private boolean publishMessage(String message, boolean isPresentInBd) {
		
		TwitterAsync twitter = TwitterAsync.connect();
		
		boolean publishLater;
		
		try {
			publishLater = (twitter.updateStatusAsync(this, message).get() == StatusPublicationAsync.Failed);
		} catch (Exception e) {
			publishLater = true;
		} 
		
		if(!isPresentInBd && publishLater) {
			
			TimelineStatusDataSource source = new TimelineStatusDataSource(this);
			source.open();
			
			source.createStatus(new TimelineStatus(-1, message, new Date(System.currentTimeMillis()), 1, false));
			
			source.close();
		}
		
		return !publishLater;
	}

	private boolean publishMessage(Intent intent) {
		
		return publishMessage(intent.getStringExtra(Values.Intent.Message), false);
	}
	
	private void sendMessage(Intent intent, int status) {
		
		final Messenger messenger = (Messenger) intent.getExtras().get(Values.Intent.Messenger);
		final Message msg = Message.obtain();
		msg.what = status;
						
		try {
			messenger.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void dispatchPendingStatus() {
		
		TimelineStatusDataSource source = new TimelineStatusDataSource(this);
		source.open();

		Iterable<TimelineStatus> collection = source.getUnpublishedStatuses();
		
		for (TimelineStatus pending : collection) {
			
			if(pending.isPublished())
				continue;
			
			boolean published = publishMessage(pending.getMessage(), true);
			
			if(!published)
				return;
			
			source.deleteStatus(pending);
		}
		
		source.close();
	}

	public static class Values {
		
		public static class Intent {
			
			public static final String Type = "type"; 
			public static final String Message = "message";
			public static final String Messenger = "messenger";
			
			public static class Op {
				
				public static final int PublishMessage = 0;
				public static final int PublishPending = 1;
			}
		}
	}
	
	public static class MessagePublishingStatus {
		
		public static final int 
			Published = 0,
			Pending = 1,
			Failed = 2;
	}
}
