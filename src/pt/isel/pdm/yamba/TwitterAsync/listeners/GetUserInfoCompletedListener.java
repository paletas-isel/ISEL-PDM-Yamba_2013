package pt.isel.pdm.yamba.TwitterAsync.listeners;

import winterwell.jtwitter.Twitter.User;

public interface GetUserInfoCompletedListener {

	public void onGetUserInfoCompleted(User user);
	
	public void onGetUsernameCompleted(String username);
	
	public void onGetStatusCountCompleted(int status);
	
	public void onGetSubscribersCountCompleted(int subscribers);
	
	public void onGetSubscriptionsCountCompleted(int subscriptions);
}
