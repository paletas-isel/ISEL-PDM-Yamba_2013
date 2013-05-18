package pt.isel.pdm.yamba.TwitterAsync.listeners;

public interface GetUserInfoCompletedListener {

	public void onGetUsernameCompleted(String username);
	
	public void onGetStatusCountCompleted(int status);
	
	public void onGetSubscribersCountCompleted(int subscribers);
	
	public void onGetSubscriptionsCountCompleted(int subscriptions);
}
