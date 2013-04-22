package pt.isel.android.content;

import pt.isel.java.Func;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class SharedPreferencesListener {

	private SharedPreferencesListener() {}
	
	public static OnSharedPreferenceChangeListener registerAndTriggerFirst(SharedPreferences prefs, String key, String defaultValue, Func<Void, String> func) {
		
		OnSharedPreferenceChangeListener listener = new OnStringSharedPreferencesChangeFunc(
				key, 
				func,
				defaultValue
		);
		
		prefs.registerOnSharedPreferenceChangeListener(listener);
		
		String value = prefs.getString(key, defaultValue);
		func.execute(value);
		
		return listener;
	}
}
