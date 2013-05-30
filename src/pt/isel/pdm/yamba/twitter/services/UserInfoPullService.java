package pt.isel.pdm.yamba.twitter.services;

import pt.isel.pdm.yamba.twitter.TwitterAsync;
import pt.isel.pdm.yamba.twitter.helpers.UserInfoParams;
import pt.isel.pdm.yamba.twitter.listeners.GetUserInfoCompletedListener;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.User;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class UserInfoPullService extends Service {
	private static final String TAG = UserInfoPullService.class.getName();

	private static void log(String txt) {
		Log.v(TAG, "Service pid=" + android.os.Process.myPid() + " thr="
				+ Thread.currentThread().getId() + ": " + txt);
	}

	public static final int GET_USER_INFO = 0;

	public static final String 
			GET_USER_NAME_BUNDLE_KEY = "UserName",
			GET_STATUS_COUNT_BUNDLE_KEY = "Status",
			GET_SUBSCRIBERS_COUNT_BUNDLE_KEY = "Subscribers",
			GET_SUBSCRIPTIONS_COUNT_BUNDLE_KEY = "Subscriptions";

	private static UserInfoPullOperations _handlerClass;
	private Messenger _messenger;

	public class UserInfoPullOperations extends Handler {

		private Messenger _replyMessenger;
		private int _replyWhat;
		
		public UserInfoPullOperations() {
			log("Creating UserInfoPullOperations");
			TwitterAsync.connect().setUserInfoObtainedListener(_listener);			
		}
		
		private final GetUserInfoCompletedListener _listener = new GetUserInfoCompletedListener() {
			
			@Override
			public void onGetUsernameCompleted(String username) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetUserInfoCompleted(User user) {
				sendResponse(
						_replyMessenger, _replyWhat, 
						user.getName(), 
						user.getStatusesCount(),
						user.getFollowersCount(), 
						user.getFriendsCount()
				);
			}
			
			@Override
			public void onGetSubscriptionsCountCompleted(int subscriptions) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetSubscribersCountCompleted(int subscribers) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetStatusCountCompleted(int status) {
				// TODO Auto-generated method stub
				
			}
		};
		
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case GET_USER_INFO:		
				
				_replyMessenger = msg.replyTo;
				_replyWhat = msg.what;
				TwitterAsync.connect().getUserInfoAsync(UserInfoPullService.this);
				
				break;

			default:
				super.handleMessage(msg);
			}
		}

		private void sendResponse(
				Messenger replyTo,
				int replyWhat,
				String username, 
				int status,
				int subscribers, 
				int subscriptions
		) {

			try {
				Message mResp = Message.obtain();
				mResp.what = replyWhat;

				Bundle bundle = new Bundle();
				
				bundle.putString(GET_USER_NAME_BUNDLE_KEY, username);
				bundle.putInt(GET_STATUS_COUNT_BUNDLE_KEY, status);
				bundle.putInt(GET_SUBSCRIBERS_COUNT_BUNDLE_KEY, subscribers);
				bundle.putInt(GET_SUBSCRIPTIONS_COUNT_BUNDLE_KEY, subscriptions);
						
				mResp.setData(bundle);
				replyTo.send(mResp);
			} catch (RemoteException e) {
				Log.e(TAG, "Error send resp. e=" + e);
			}
		}
	}
	
	@Override
	public void onCreate() {
		if(_handlerClass == null) _handlerClass = new UserInfoPullOperations();
		_messenger = new Messenger(_handlerClass);
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return _messenger.getBinder();
	}
}
