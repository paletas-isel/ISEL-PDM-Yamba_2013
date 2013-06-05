package pt.isel.pdm.yamba.twitter.tasks;

import java.util.List;

import pt.isel.pdm.yamba.data.model.TimelineStatus;
import pt.isel.pdm.yamba.twitter.TwitterAsync;
import pt.isel.pdm.yamba.twitter.TwitterAsyncTask;
import pt.isel.pdm.yamba.twitter.listeners.TimelineObtainedListener;
import winterwell.jtwitter.Twitter;

public class GetTimelineAsync extends TwitterAsyncTask<String, List<winterwell.jtwitter.Twitter.Status>, Void>{
	
	public GetTimelineAsync(TwitterAsync connection) {
		super(connection);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Void doExecute(String... arg0) {

		Twitter connection = getConnection();

		List<winterwell.jtwitter.Twitter.Status> userTimeline;
		
		if(arg0 != null && arg0.length > 0) {	
			
			for(String user : arg0) {
								
				userTimeline = connection.getUserTimeline(user);			
				
				publishProgress(userTimeline);
			}
		}
		else {
			
			userTimeline = connection.getUserTimeline();			

			publishProgress(userTimeline);
		}
		
		return null;		
	}

	@Override
	protected void onProgressUpdate(List<winterwell.jtwitter.Twitter.Status>... values) {

		TimelineObtainedListener listener = getTwitterAsync().getTimelineObtainedListener();
		if(listener != null) {
			for(List<winterwell.jtwitter.Twitter.Status> value : values) {
				listener.onTimelineObtained(TimelineStatus.from(value));
			}		
		}
		
		super.onProgressUpdate(values);
	}
}
