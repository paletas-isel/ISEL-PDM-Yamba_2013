package pt.isel.android.content;

import pt.isel.java.Action;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

abstract class OnSharedPreferencesChangeFunc<T> implements OnSharedPreferenceChangeListener {
	
	private final String _key;
	private final Action<T> _func;
	
	protected OnSharedPreferencesChangeFunc(String key, Action<T> func) {
		
		this._key = key;
		this._func = func;
	}
	
	@Override
	public final void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
		if(_key.equals(key)) {
			_func.execute(getValueFromSharedPreferences(sharedPreferences, key));
		}
	}
	
	protected abstract T getValueFromSharedPreferences(SharedPreferences preferences, String key);
}
