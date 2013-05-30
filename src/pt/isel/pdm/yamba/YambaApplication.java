package pt.isel.pdm.yamba;

import java.util.Collection;
import java.util.LinkedList;

import pt.isel.android.content.SharedPreferencesListener;
import pt.isel.java.Action;
import pt.isel.pdm.yamba.exceptions.TwitterException;
import pt.isel.pdm.yamba.settings.Settings;
import pt.isel.pdm.yamba.twitter.TwitterAsync;
import pt.isel.pdm.yamba.twitter.listeners.TwitterExceptionListener;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class YambaApplication extends Application{
	
	private final Collection<Object> _foreverAlone = new LinkedList<Object>();
		
	private static YambaApplication _application;
	
	public static YambaApplication getApplication() { return _application; }
	
	@Override
	public void onCreate() {	

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
				Toast.makeText(YambaApplication.getApplication(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
			}
		});
	}
}
