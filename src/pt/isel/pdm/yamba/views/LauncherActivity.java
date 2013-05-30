package pt.isel.pdm.yamba.views;

import pt.isel.pdm.yamba.twitter.TwitterAsync;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Class<? extends Activity> startupActivity =  (TwitterAsync.getUsername() == null || TwitterAsync.getServiceUri() == null)?
				PreferencesActivity.class :
				TimelineActivity.class;
		
		startActivity(new Intent(this, startupActivity));
	}
}
