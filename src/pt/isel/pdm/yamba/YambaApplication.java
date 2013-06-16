package pt.isel.pdm.yamba;

import java.util.Collection;
import java.util.LinkedList;

import pt.isel.android.content.SharedPreferencesListener;
import pt.isel.android.net.Network;
import pt.isel.java.Action;
import pt.isel.pdm.yamba.exceptions.TwitterException;
import pt.isel.pdm.yamba.settings.Settings;
import pt.isel.pdm.yamba.twitter.TwitterAsync;
import pt.isel.pdm.yamba.twitter.listeners.TwitterExceptionListener;
import pt.isel.pdm.yamba.twitter.services.StatusUploadService;
import pt.isel.pdm.yamba.twitter.services.TimelinePullService;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class YambaApplication extends Application{
	
	private final Collection<Object> _foreverAlone = new LinkedList<Object>();
		
	private static YambaApplication _application;
	
	public static YambaApplication getApplication() { return _application; }
	
	private final BroadcastReceiver _connectivityBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			if(Network.isNetworkAvailable(context)) {
				
				Intent serviceIntent = new Intent(context, StatusUploadService.class);
				
				serviceIntent.putExtra(StatusUploadService.Values.Intent.Type, StatusUploadService.Values.Intent.Op.PublishPending);
				context.startService(serviceIntent);
				
				
				//Restart timelinepullservice
				Intent serviceTimelineIntent = new Intent(context, TimelinePullService.class);
				
				serviceTimelineIntent.putExtra(TimelinePullService.INTENT_CONNECTIVITY_RESTABLISHED, true);
				context.startService(serviceTimelineIntent);	
			}
		}
	};
	
	@Override
	public void onCreate() {	

		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);        
		registerReceiver(_connectivityBroadcastReceiver, filter);
		
		_application = this;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		_foreverAlone.add(SharedPreferencesListener.registerAndTriggerFirst(prefs, Settings.Yamba.Username, null, 
				new Action<String>() {

					@Override
					public void execute(String param) {
						TwitterAsync.setUsername(param);
					}
				}
		, String.class));
		
		_foreverAlone.add(SharedPreferencesListener.registerAndTriggerFirst(prefs, Settings.Yamba.Password, null, 
				new Action<String>() {

					@Override
					public void execute(String param) {
						TwitterAsync.setPassword(param);
					}
				}
		, String.class));
		
		_foreverAlone.add(SharedPreferencesListener.registerAndTriggerFirst(prefs, Settings.Yamba.Uri, null, 
				new Action<String>() {

					@Override
					public void execute(String param) {
						TwitterAsync.setServiceUri(param);
					}
				}
		, String.class));
		
		TwitterAsync.setTwitterExceptionListener(new TwitterExceptionListener() {
			
			@Override
			public void onExceptionThrown(TwitterException e) {
				
				try {
					
					Toast.makeText(YambaApplication.getApplication(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
				}
				catch(Exception ex) {
					Log.d(this.getClass().getSimpleName(), "Uncaught Exception: " + ex.getStackTrace().toString());
				}
			}
		});
	
	}
}
