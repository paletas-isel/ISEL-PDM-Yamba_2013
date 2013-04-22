package pt.isel.pdm.yamba.views;

import java.util.Arrays;

import pt.isel.android.view.MenuInflaterComposite;
import pt.isel.pdm.yamba.R;
import pt.isel.pdm.yamba.menu.YambaActivityToMenuIdMapper;
import pt.isel.pdm.yamba.menu.inflater.YambaMenuInflater;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PreferencesActivity extends PreferenceActivity {

	private final MenuInflater _menuInflater;
	
	public PreferencesActivity() {
		_menuInflater = new MenuInflaterComposite(this, Arrays.asList(super.getMenuInflater(), new YambaMenuInflater(this)));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.preferences, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		final int itemId = item.getItemId();
		
		Class<? extends Activity> activity = YambaActivityToMenuIdMapper.getActivityForMenuId(itemId);
		
		if(activity != null) {
			startActivity(new Intent(this, activity));
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public MenuInflater getMenuInflater() {
		return _menuInflater;
	}
}
