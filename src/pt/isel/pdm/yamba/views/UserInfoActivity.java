package pt.isel.pdm.yamba.views;

import pt.isel.pdm.yamba.R;
import pt.isel.pdm.yamba.TwitterAsync.listeners.GetUserInfoCompletedListener;
import pt.isel.pdm.yamba.TwitterAsync.services.AsyncUserInfoPullService;
import pt.isel.pdm.yamba.TwitterAsync.services.UserInfoPullService;
import winterwell.jtwitter.Twitter.User;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class UserInfoActivity extends YambaBaseActivity
{	
	public UserInfoActivity() {
		super(R.menu.userinfo);
		// TODO Auto-generated constructor stub
	}

	private GetUserInfoCompletedListener _listener;
	private AsyncUserInfoPullService _params;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_userinfo);
		
		final TextView 
			_name = (TextView)findViewById(R.id.userInfoName),
			_subscriptions = (TextView)findViewById(R.id.userInfoSubscriptions),
			_subscribers = (TextView)findViewById(R.id.userInfoSubscribers),
			_status = (TextView)findViewById(R.id.userInfoStatus);
		
		if(_listener == null)
		{
			_listener = new GetUserInfoCompletedListener() {
		
				@Override
				public void onGetUsernameCompleted(String username) {
					
					if(username == null)
						return;
					
					_name.setText(username);				
				}
				
				@Override
				public void onGetSubscriptionsCountCompleted(int subscriptions) {
					_subscriptions.setText(Integer.toString(subscriptions));					
				}
				
				@Override
				public void onGetSubscribersCountCompleted(int subscribers) {
					_subscribers.setText(Integer.toString(subscribers));						
				}
				
				@Override
				public void onGetStatusCountCompleted(int status) {
					_status.setText(Integer.toString(status));					
				}

				@Override
				public void onGetUserInfoCompleted(User user) {
					// TODO Auto-generated method stub
					
				}
			};
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(_params == null)
		{
			_params = new AsyncUserInfoPullService();
		}
		
		bindService(new Intent(this, UserInfoPullService.class), _params, BIND_AUTO_CREATE);
		
		fillActivityWithUserInfo();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		unbindService(_params);
	}
	
	private void fillActivityWithUserInfo()
	{
		_params.getUserInfo(_listener);
	}
}
