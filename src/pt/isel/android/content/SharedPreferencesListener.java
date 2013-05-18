package pt.isel.android.content;

import pt.isel.java.Action;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class SharedPreferencesListener {

	private SharedPreferencesListener() {}
	
	private static <T> OnSharedPreferenceChangeListener createListener(String key, T defaultValue, Action<T> func, Class<T> type) {
		
		if(type == String.class) {
			return new OnStringSharedPreferencesChangeFunc(
					key, 
					(Action<String>)func,
					(String)defaultValue
			);
		}
		else if(type == Boolean.class) {
			return new OnBooleanSharedPreferencesChangeFunc(
					key, 
					(Action<Boolean>)func,
					(Boolean)defaultValue
			);
		}
		else if(type == Integer.class) {
			return new OnIntegerSharedPreferencesChangeFunc(
					key, 
					(Action<Integer>)func,
					(Integer)defaultValue
			);
		}
		
		return null;
	}
	
	private static <T> T extractValue(SharedPreferences prefs, String key, T defaultValue, Class<T> type){
		if(type == String.class) {
			return (T) prefs.getString(key, (String) defaultValue);
		}
		else if(type == Boolean.class) {
			return (T) Boolean.valueOf(prefs.getBoolean(key, (Boolean) defaultValue));
		}
		else if(type == Integer.class) {
			return (T) Integer.valueOf(prefs.getString(key, String.valueOf(defaultValue)));
		}
		
		return null;
	}
	
	public static <T> OnSharedPreferenceChangeListener registerAndTriggerFirst(SharedPreferences prefs, String key, T defaultValue, Action<T> func, Class<T> type) {
		
		OnSharedPreferenceChangeListener listener = createListener(key, defaultValue, func, type);
		
		prefs.registerOnSharedPreferenceChangeListener(listener);
		
		T value = extractValue(prefs, key, defaultValue, type);
		func.execute(value);
		
		return listener;
	}
}
