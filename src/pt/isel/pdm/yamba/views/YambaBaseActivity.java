package pt.isel.pdm.yamba.views;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import pt.isel.android.content.SharedPreferencesListener;
import pt.isel.android.view.MenuInflaterComposite;
import pt.isel.java.Action;
import pt.isel.pdm.yamba.menu.YambaActivityToMenuIdMapper;
import pt.isel.pdm.yamba.menu.inflater.YambaMenuInflater;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class YambaBaseActivity extends Activity {
	
	private Collection<Object> _foreverAlone;
	
	private final int _menuRes;
	private Menu _menu;
	private MenuInflater _menuInflater;
	
	protected Menu getMenu() {
		return _menu;		
	}
	
	protected YambaBaseActivity(int menuRes) {
		this._menuRes = menuRes;
		this._foreverAlone = new LinkedList<Object>();
	}
	
	@Override
	public final boolean onCreateOptionsMenu(Menu menu) {
		
		_menu = menu;
		
		getMenuInflater().inflate(_menuRes, menu);
		
		onMenuLoaded(menu);
		
		return true;
	}
	
	protected void onMenuLoaded(Menu menu)
	{
		
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
		if(_menuInflater == null) {
			_menuInflater = new MenuInflaterComposite(this, Arrays.asList(super.getMenuInflater(), new YambaMenuInflater(this)));
		}
		return _menuInflater;
	}
	
	public void showError(String error) {
		Toast.makeText(this, error, Toast.LENGTH_LONG).show();
	}
	
	public <T> void registerAndTriggerFirst(String key, T defaultValue, Action<T> func, Class<T> type) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		_foreverAlone.add(SharedPreferencesListener.registerAndTriggerFirst(prefs, key, defaultValue, func, type));		
	}	
}
