package pt.isel.pdm.yamba.TwitterAsync.tasks;

import pt.isel.pdm.yamba.TwitterAsync.TwitterAsync;
import pt.isel.pdm.yamba.TwitterAsync.TwitterAsyncTask;
import pt.isel.pdm.yamba.TwitterAsync.helpers.StatusContainer;
import pt.isel.pdm.yamba.TwitterAsync.listeners.StatusPublishedListener;
import winterwell.jtwitter.Twitter;

public class StatusPublicationAsync extends TwitterAsyncTask<StatusContainer, Twitter.Status, Void> {
		
	public StatusPublicationAsync(TwitterAsync connection) {
		super(connection);
	}

	@Override
	protected Void doExecute(StatusContainer... params) {
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
		
		return null;
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
