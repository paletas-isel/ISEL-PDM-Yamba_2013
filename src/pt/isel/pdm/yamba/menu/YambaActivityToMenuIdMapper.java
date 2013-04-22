package pt.isel.pdm.yamba.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pt.isel.pdm.yamba.R;
import pt.isel.pdm.yamba.views.PreferencesActivity;
import pt.isel.pdm.yamba.views.StatusActivity;
import pt.isel.pdm.yamba.views.TimelineActivity;
import android.app.Activity;

public class YambaActivityToMenuIdMapper {

	private static final Map<Class<? extends Activity>, Integer> _activityToMenuIdMapper;
	
	static {
		
		_activityToMenuIdMapper = new HashMap<Class<? extends Activity>, Integer>();
		_activityToMenuIdMapper.put(StatusActivity.class, R.id.action_status);
		_activityToMenuIdMapper.put(PreferencesActivity.class, R.id.action_preferences);
		_activityToMenuIdMapper.put(TimelineActivity.class, R.id.action_timeline);
	}
	
	private YambaActivityToMenuIdMapper() {
		
	}
	
	public static int getMenuIdForActivity(Class<? extends Activity> klass) {
		return (_activityToMenuIdMapper.containsKey(klass))?_activityToMenuIdMapper.get(klass):0;
	}
	
	public static Class<? extends Activity> getActivityForMenuId(int itemId) {
		
		for(Entry<Class<? extends Activity>, Integer> entry: _activityToMenuIdMapper.entrySet()) {
			
			if(entry.getValue() == itemId) {
				return entry.getKey();
			}
		}
		
		return null;
	}
}
