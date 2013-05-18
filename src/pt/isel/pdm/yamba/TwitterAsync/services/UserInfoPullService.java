package pt.isel.pdm.yamba.TwitterAsync.services;

import pt.isel.pdm.yamba.TwitterAsync.TwitterAsync;
import pt.isel.pdm.yamba.TwitterAsync.helpers.UserInfoParams;
import winterwell.jtwitter.Twitter;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class UserInfoPullService extends Service implements UserInfoParams {
	private static final String TAG = UserInfoPullService.class.getName();

	private static void log(String txt) {
		Log.v(TAG, "Service pid=" + android.os.Process.myPid() + " thr="
				+ Thread.currentThread().getId() + ": " + txt);
	}

	private Twitter.User _user;

	public static final int GET_USER_INFO = 0;

	public static final String 
			GET_USER_NAME_BUNDLE_KEY = "UserName",
			GET_STATUS_COUNT_BUNDLE_KEY = "Status",
			GET_SUBSCRIBERS_COUNT_BUNDLE_KEY = "Subscribers",
			GET_SUBSCRIPTIONS_COUNT_BUNDLE_KEY = "Subscriptions";

	private Messenger _messenger = new Messenger(new UserInfoPullOperations());

	public class UserInfoPullOperations extends Handler {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case GET_USER_INFO:

				UserInfoPullService.this._user = TwitterAsync.connect()
						.getInnerConnection()
						.getUser(TwitterAsync.getUsername());

				sendResponse(
						msg, 
						getUserName(), 
						getStatusCount(),
						getSubscribersCount(), 
						getSubscriptionsCount()
				);
				
				_user = null;
				
				break;

			default:
				super.handleMessage(msg);
			}
		}

		private void sendResponse(
				Message msg, 
				String username, 
				int status,
				int subscribers, 
				int subscriptions
		) {

			try {
				Message mResp = Message.obtain();
				mResp.what = msg.what;

				Bundle bundle = new Bundle();
				
				bundle.putString(GET_USER_NAME_BUNDLE_KEY, username);
				bundle.putInt(GET_STATUS_COUNT_BUNDLE_KEY, status);
				bundle.putInt(GET_SUBSCRIBERS_COUNT_BUNDLE_KEY, subscribers);
				bundle.putInt(GET_SUBSCRIPTIONS_COUNT_BUNDLE_KEY, subscriptions);
						
				mResp.setData(bundle);
				msg.replyTo.send(mResp);
			} catch (RemoteException e) {
				Log.e(TAG, "Error send resp. e=" + e);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return _messenger.getBinder();
	}

	@Override
	public String getUserName() {
		return TwitterAsync.getUsername();
	}

	@Override
	public int getStatusCount() {
		return _user.getStatusesCount();
	}

	@Override
	public int getSubscribersCount() {
		return _user.getFollowersCount();
	}

	@Override
	public int getSubscriptionsCount() {
		return _user.getFriendsCount();
	}
}
