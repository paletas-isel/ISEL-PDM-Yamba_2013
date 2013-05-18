package pt.isel.pdm.yamba.TwitterAsync.tasks;

import pt.isel.pdm.yamba.TwitterAsync.TwitterAsync;
import pt.isel.pdm.yamba.TwitterAsync.TwitterAsyncTask;
import pt.isel.pdm.yamba.TwitterAsync.listeners.GetUserInfoCompletedListener;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.User;

public class GetUserAsync extends TwitterAsyncTask<Void, Twitter.User, Void> {

	public GetUserAsync(TwitterAsync connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Void doExecute(Void... arg0) {
		Twitter.User user = getConnection().getUser(TwitterAsync.getUsername());
		
		publishProgress(user);
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Twitter.User... values) {

		GetUserInfoCompletedListener listener = getTwitterAsync().getUserInfoObtainedListener();
		if(listener != null) {
			for(User value : values) {
				listener.onGetStatusCountCompleted(value.getStatusesCount());
				listener.onGetSubscribersCountCompleted(value.getFollowersCount());
				listener.onGetSubscriptionsCountCompleted(value.getFriendsCount());
				listener.onGetUsernameCompleted(value.getName());
				
				listener.onGetUserInfoCompleted(value);
			}		
		}
		
		super.onProgressUpdate(values);
	}
	
}
