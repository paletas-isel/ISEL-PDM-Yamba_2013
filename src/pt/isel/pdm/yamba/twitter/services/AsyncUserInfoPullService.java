package pt.isel.pdm.yamba.twitter.services;

import pt.isel.pdm.yamba.twitter.listeners.GetUserInfoCompletedListener;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class AsyncUserInfoPullService implements ServiceConnection, AsyncUserInfoParams
{
	private static final String TAG = AsyncUserInfoPullService.class.getName();

	private static void log(String txt) {
		Log.d(TAG,"Service pid="+android.os.Process.myPid()+
				    " thr="+Thread.currentThread().getId()+": "+txt);
	}
	
	private Messenger _userInfoPullService;
	private GetUserInfoCompletedListener _listener;
	
	private void sendOperation(int op, Messenger callback) {
		
		if(_userInfoPullService == null)
			return;
		
		try {
			
			Message m = Message.obtain();
			m.replyTo = callback;
			m.what = op;
			
			_userInfoPullService.send(m);
			
		} catch (RemoteException e) {
			log("Remote exception="+e);
		}
	}
	
	@Override
	public void onServiceConnected(ComponentName arg0, IBinder arg1) {
		_userInfoPullService = new Messenger(arg1);
		
		sendOperation
		(
			UserInfoPullService.GET_USER_INFO,
			new Messenger( 
				new Handler() {
					public void handleMessage(Message msg) {
						_listener.onGetUsernameCompleted(msg.getData().getString(UserInfoPullService.GET_USER_NAME_BUNDLE_KEY));
						_listener.onGetStatusCountCompleted(msg.getData().getInt(UserInfoPullService.GET_STATUS_COUNT_BUNDLE_KEY));
						_listener.onGetSubscribersCountCompleted(msg.getData().getInt(UserInfoPullService.GET_SUBSCRIBERS_COUNT_BUNDLE_KEY));
						_listener.onGetSubscriptionsCountCompleted(msg.getData().getInt(UserInfoPullService.GET_SUBSCRIPTIONS_COUNT_BUNDLE_KEY));
					}
				}
			)
		);	
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		_userInfoPullService = null;
	}

	@Override
	public void getUserInfo(final GetUserInfoCompletedListener listener) {
		_listener = listener;
		
		sendOperation
		(
			UserInfoPullService.GET_USER_INFO,
			new Messenger( 
				new Handler() {
					public void handleMessage(Message msg) {
						_listener.onGetUsernameCompleted(msg.getData().getString(UserInfoPullService.GET_USER_NAME_BUNDLE_KEY));
						_listener.onGetStatusCountCompleted(msg.getData().getInt(UserInfoPullService.GET_STATUS_COUNT_BUNDLE_KEY));
						_listener.onGetSubscribersCountCompleted(msg.getData().getInt(UserInfoPullService.GET_SUBSCRIBERS_COUNT_BUNDLE_KEY));
						_listener.onGetSubscriptionsCountCompleted(msg.getData().getInt(UserInfoPullService.GET_SUBSCRIPTIONS_COUNT_BUNDLE_KEY));
					}
				}
			)
		);		
	}
}
