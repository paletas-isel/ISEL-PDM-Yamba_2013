package pt.isel.pdm.yamba;

import pt.isel.android.content.SharedPreferencesListener;
import pt.isel.java.Func;
import pt.isel.pdm.yamba.TwitterAsync.TwitterAsync;
import pt.isel.pdm.yamba.TwitterAsync.listeners.TwitterExceptionListener;
import pt.isel.pdm.yamba.exceptions.TwitterException;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class YambaApplication extends Application{
	
	private static final String
		PREFKEY_USERNAME = "prefkey_yamba_username",
		PREFKEY_PASSWORD = "prefkey_yamba_password",
		PREFKEY_URI = "prefkey_yamba_uri";
	
	private static YambaApplication _application;
	
	public static YambaApplication getApplication() { return _application; }
	
	@Override
	public void onCreate() {	

		_application = this;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		SharedPreferencesListener.registerAndTriggerFirst(prefs, PREFKEY_USERNAME, "student", 
				new Func<Void, String>() {

					@Override
					public Void execute(String param) {
						TwitterAsync.setUsername(param);
						return null;
					}
				}
		);
		
		SharedPreferencesListener.registerAndTriggerFirst(prefs, PREFKEY_PASSWORD, "password", 
				new Func<Void, String>() {

					@Override
					public Void execute(String param) {
						TwitterAsync.setPassword(param);
						return null;
					}
				}
		);
		
		SharedPreferencesListener.registerAndTriggerFirst(prefs, PREFKEY_URI, "http://yamba.marakana.com/api", 
				new Func<Void, String>() {

					@Override
					public Void execute(String param) {
						TwitterAsync.setServiceUri(param);
						return null;
					}
				}
		);
		
		TwitterAsync.setTwitterExceptionListener(new TwitterExceptionListener() {
			
			@Override
			public void onExceptionThrown(TwitterException e) {
				Toast.makeText(YambaApplication.getApplication(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
			}
		});
	}
	
}
