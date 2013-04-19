package pt.isel.pdm.yamba.views;

import pt.isel.pdm.yamba.MyMenuInflater;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

abstract class YambaBaseActivity extends Activity {
	
	private final MenuInflater _menuInflater;
	private final int _menuRes;
	
	private final Class<? extends YambaBaseActivity> _activity;
	
	protected YambaBaseActivity(Class<? extends YambaBaseActivity> activity) {
		this._activity = activity;
		this._menuRes = menuRes;
		this._menuInflater = new MyMenuInflater(this, super.getMenuInflater());
	}
	
	@Override
	public final boolean onCreateOptionsMenu(Menu menu) {
		
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(_menuActivity.get(_activity), menu);
				
		return true;
	}

	@Override
	public final boolean onOptionsItemSelected(MenuItem item) {
		
		return true;
	}
	
	@Override
	public final MenuInflater getMenuInflater() {
		return _menuInflater;
	}
}
