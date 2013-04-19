package pt.isel.pdm.yamba.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pt.isel.pdm.yamba.R;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

abstract class YambaBaseActivity extends Activity {
	
	private static final Map<Integer, Class<? extends Activity>> _activityMenuResMapper;
	
	static {
		_activityMenuResMapper = new HashMap<Integer, Class<? extends Activity>>();
		_activityMenuResMapper.put(R.id.action_status, StatusActivity.class);
		_activityMenuResMapper.put(R.id.action_timeline, TimelineActivity.class);
	}
	
	private final MenuInflater _menuInflater;
	private final int _menuRes;
	private final Class<? extends YambaBaseActivity> _activity;
	
	protected YambaBaseActivity(Class<? extends YambaBaseActivity> activity, int menuRes) {
		this._activity = activity;
		this._menuRes = menuRes;
		this._menuInflater = super.getMenuInflater();
	}
	
	@Override
	public final boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(_menuRes, menu);
		
		int order = menu.size();
		for(Entry<Integer, Class<? extends Activity>> entry : _activityMenuResMapper.entrySet()) {
						
			if(entry.getValue() != _activity)
				menu.add(0, entry.getKey(), ++order, entry.getValue().getSimpleName());
		}
		
		return true;
	}

	@Override
	public final boolean onOptionsItemSelected(MenuItem item) {
		
		final int itemId = item.getItemId();
		
		if(_activityMenuResMapper.containsKey(itemId)) {
			startActivity(new Intent(this, _activityMenuResMapper.get(itemId)));
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public final MenuInflater getMenuInflater() {
		return _menuInflater;
	}
}
