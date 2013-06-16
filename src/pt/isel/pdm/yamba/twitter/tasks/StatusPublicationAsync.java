package pt.isel.pdm.yamba.twitter.tasks;

import pt.isel.pdm.yamba.twitter.TwitterAsync;
import pt.isel.pdm.yamba.twitter.TwitterAsyncTask;
import pt.isel.pdm.yamba.twitter.helpers.StatusContainer;
import pt.isel.pdm.yamba.twitter.listeners.StatusPublishedListener;
import winterwell.jtwitter.Twitter;

public class StatusPublicationAsync extends TwitterAsyncTask<StatusContainer, Twitter.Status, Integer> {
	
	public static final int Published = 0, Failed = 1;
	
	public StatusPublicationAsync(TwitterAsync connection) {
		super(connection);
	}

	@Override
	protected Integer doExecute(StatusContainer... params) {
		Twitter connection = getConnection();
		
		for(StatusContainer status : params) {
			Twitter.Status s;			
			if(status.isReply()) {
				s = connection.updateStatus(status.getStatus(), status.inReplyTo());
			}
			else {
				s = connection.updateStatus(status.getStatus());
			}
			
			publishProgress(s);
		}
		
		return (getInnerException() == null)?Published:Failed;
	}

	@Override
	protected void onProgressUpdate(
			winterwell.jtwitter.Twitter.Status... values) {

		StatusPublishedListener listener = getTwitterAsync().getStatusPublishedListener();
		if(listener != null) {
			for(Twitter.Status status : values) {
				listener.onStatusPublished(status);
			}
		}
		
		super.onProgressUpdate(values);
	}
}
