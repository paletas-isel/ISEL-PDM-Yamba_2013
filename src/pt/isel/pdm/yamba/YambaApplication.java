package pt.isel.pdm.yamba;

import java.util.Collection;
import java.util.LinkedList;

import pt.isel.android.content.SharedPreferencesListener;
import pt.isel.java.Func;
import pt.isel.pdm.yamba.TwitterAsync.TwitterAsync;
import pt.isel.pdm.yamba.TwitterAsync.listeners.TwitterExceptionListener;
import pt.isel.pdm.yamba.exceptions.TwitterException;
import pt.isel.pdm.yamba.settings.Settings;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class YambaApplication extends Application{
	
	private final Collection<Object> _keepAlive = new LinkedList<Object>();
		
	private static YambaApplication _application;
	
	public static YambaApplication getApplication() { return _application; }
	
	@Override
	public void onCreate() {	

		_application = this;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		_keepAlive.add(SharedPreferencesListener.registerAndTriggerFirst(prefs, Settings.Yamba.Username, null, 
				new Func<Void, String>() {

					@Override
					public Void execute(String param) {
						TwitterAsync.setUsername(param);
						return null;
					}
				}
		));
		
		_keepAlive.add(SharedPreferencesListener.registerAndTriggerFirst(prefs, Settings.Yamba.Password, null, 
				new Func<Void, String>() {

					@Override
					public Void execute(String param) {
						TwitterAsync.setPassword(param);
						return null;
					}
				}
		));
		
		_keepAlive.add(SharedPreferencesListener.registerAndTriggerFirst(prefs, Settings.Yamba.Uri, null, 
				new Func<Void, String>() {

					@Override
					public Void execute(String param) {
						TwitterAsync.setServiceUri(param);
						return null;
					}
				}
		));
		
		TwitterAsync.setTwitterExceptionListener(new TwitterExceptionListener() {
			
			@Override
			public void onExceptionThrown(TwitterException e) {
				Toast.makeText(YambaApplication.getApplication(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
			}
		});
	}
}
