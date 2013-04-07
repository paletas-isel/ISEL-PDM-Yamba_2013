package pt.isel.pdm.yamba;

import android.app.Application;
import android.widget.Toast;

import pt.isel.pdm.yamba.TwitterAsync.TwitterAsync;
import pt.isel.pdm.yamba.TwitterAsync.listeners.TwitterExceptionListener;
import pt.isel.pdm.yamba.exceptions.TwitterException;

public class YambaApplication extends Application{
	
	private static YambaApplication _application;
	
	public static YambaApplication getApplication() { return _application; }
	
	@Override
	public void onCreate() {	

		_application = this;
		
		TwitterAsync.setUsername("student");
		TwitterAsync.setPassword("password");
		TwitterAsync.setServiceUri("http://yamba.marakana.com/api");
		
		TwitterAsync.setTwitterExceptionListener(new TwitterExceptionListener() {
			
			@Override
			public void onExceptionThrown(TwitterException e) {
				Toast.makeText(YambaApplication.getApplication(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
			}
		});
	}
	
}
