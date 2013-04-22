package pt.isel.pdm.yamba.views;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import pt.isel.android.content.SharedPreferencesListener;
import pt.isel.android.view.MenuInflaterComposite;
import pt.isel.java.Func;
import pt.isel.pdm.yamba.menu.YambaActivityToMenuIdMapper;
import pt.isel.pdm.yamba.menu.inflater.YambaMenuInflater;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class YambaBaseActivity extends Activity {
	
	private Collection<Object> _keepAlive;
	
	private final int _menuRes;
	private Menu _menu;
	private final MenuInflater _menuInflater;
	
	protected Menu getMenu() {
		return _menu;		
	}
	
	protected YambaBaseActivity(int menuRes) {
		this._menuRes = menuRes;
		this._menuInflater = new MenuInflaterComposite(this, Arrays.asList(super.getMenuInflater(), new YambaMenuInflater(this)));
		this._keepAlive = new LinkedList<Object>();
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
		return _menuInflater;
	}
	
	public void showError(String error) {
		Toast.makeText(this, error, Toast.LENGTH_LONG).show();
	}
	
	public void registerAndTriggerFirst(SharedPreferences prefs, String key, String defaultValue, Func<Void, String> func) {
		_keepAlive.add(SharedPreferencesListener.registerAndTriggerFirst(prefs, key, defaultValue, func));		
	}
}
