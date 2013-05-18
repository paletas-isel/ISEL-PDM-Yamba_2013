package pt.isel.pdm.yamba.TwitterAsync;

import java.lang.ref.WeakReference;

import pt.isel.pdm.yamba.TwitterAsync.helpers.IntentHelpers;
import pt.isel.pdm.yamba.TwitterAsync.helpers.StatusContainer;
import pt.isel.pdm.yamba.TwitterAsync.listeners.GetUserInfoCompletedListener;
import pt.isel.pdm.yamba.TwitterAsync.listeners.StatusPublishedListener;
import pt.isel.pdm.yamba.TwitterAsync.listeners.TimelineObtainedListener;
import pt.isel.pdm.yamba.TwitterAsync.listeners.TwitterExceptionListener;
import pt.isel.pdm.yamba.TwitterAsync.services.TimelinePullService;
import pt.isel.pdm.yamba.TwitterAsync.tasks.GetUserAsync;
import pt.isel.pdm.yamba.TwitterAsync.tasks.StatusPublicationAsync;
import pt.isel.pdm.yamba.exceptions.TwitterException;
import winterwell.jtwitter.Twitter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class TwitterAsync {
		
	private static String _ServiceUri;
	private static String _Username;
	private static String _Password;
	
	private static Twitter _Connection;
	
	public Twitter getInnerConnection() {
		
		if(_Connection == null || !_Connection.isValidLogin()) {
			_Connection = new Twitter(_Username, _Password);
			_Connection.setAPIRootUrl(_ServiceUri);
		}
		return _Connection;
	}
	
	public synchronized static void setServiceUri(String uri) {
		_Connection = null;		
		_ServiceUri = uri;
	}
	
	public synchronized static String getUsername() {
		return _Username;
	}
	
	public synchronized static String getServiceUri() {
		return _ServiceUri;
	}
	
	public synchronized static void setUsername(String username) {
		_Connection = null;
		_Username = username;
	}
	
	public synchronized static void setPassword(String password) {
		_Connection = null;		
		_Password = password;
	}
	
	private TwitterAsync(String username, String password) {
		_Username = username;
		_Password = password;
	}
	
	/*
	 * EXCEPTION HANDLING
	 */
	private static TwitterExceptionListener _twitterExceptionListener;
	
	public static synchronized void setTwitterExceptionListener(TwitterExceptionListener listener) {
				
		_twitterExceptionListener = listener;
	
	}
	
	public static synchronized void clearTwitterExceptionListener() {
		_twitterExceptionListener = null;
	}	
	
	static void notifyException(TwitterException e) {
		if(_twitterExceptionListener != null) {
			_twitterExceptionListener.onExceptionThrown(e);
		}
	}
	
	/*
	 * PUBLISH STATUS
	 */
	private WeakReference<StatusPublishedListener> _statusPublishedListener;
	
	public synchronized void setStatusPublishedListener(StatusPublishedListener listener) {
				
		_statusPublishedListener = new WeakReference<StatusPublishedListener>(listener);		
	
	}
	
	public synchronized void clearStatusPublishedListener() {
		_statusPublishedListener = null;
	}	
	
	public synchronized StatusPublishedListener getStatusPublishedListener() {
		return _statusPublishedListener.get();
	}
	
	public AsyncTask<?, ?, ?> updateStatusAsync(Context context, String statusText, long inReplyToStatusId)
			throws TwitterException {

		return new StatusPublicationAsync(this)
			.execute(new StatusContainer(statusText, inReplyToStatusId));		
	}

	public AsyncTask<?, ?, ?> updateStatusAsync(Context context, String statusText) {		

		return new StatusPublicationAsync(this)
			.execute(new StatusContainer(statusText));		
	}		
	
	/*
	 * GET TIMELINE
	 */
	private WeakReference<TimelineObtainedListener> _timelineObtainedListener;
	
	public synchronized void setTimelineObtainedListener(TimelineObtainedListener listener) {
				
		_timelineObtainedListener = new WeakReference<TimelineObtainedListener>(listener);		
	
	}
	
	public synchronized TimelineObtainedListener getTimelineObtainedListener() {
		return _timelineObtainedListener.get();
	}
	
	public synchronized void clearTimelineObtainedListener() {
		_timelineObtainedListener = null;
	}	
	
	public void getUserTimelineAsync(Context context) {		
		Intent intent = IntentHelpers.generateIntent(context, TimelinePullService.class);

		context.startService(intent);
	}
	
	public void getUserTimelineAsync(Context context, String user) {
		Intent intent = IntentHelpers.generateIntentWithString(context, TimelinePullService.class, TimelinePullService.PARAM_TAG, user);

		context.startService(intent);
	}
	
	/*
	 * GET USERINFO
	 */
	private WeakReference<GetUserInfoCompletedListener> _getUserInfoObtainedListener;
	
	public synchronized void setUserInfoObtainedListener(GetUserInfoCompletedListener listener) {
				
		_getUserInfoObtainedListener = new WeakReference<GetUserInfoCompletedListener>(listener);
	}
	
	public synchronized GetUserInfoCompletedListener getUserInfoObtainedListener() {
		return _getUserInfoObtainedListener.get();
	}
	
	public synchronized void clearUserInfoCompletedListener() {
		_getUserInfoObtainedListener = null;
	}	
	
	public void getUserInfoAsync(Context context) {		

		new GetUserAsync(this).execute();	
	}
	
	/*
	 * SINGLETON
	 */
	
	private static TwitterAsync _Instance;
	
	public synchronized static TwitterAsync connect() {
		
		if(_Instance == null) {
			Log.d("TwitterAsync", "" + _Username + " - " + _Password + " - " + _ServiceUri);
			_Instance = new TwitterAsync(_Username, _Password);
		}
		
		return _Instance;
	}
}
