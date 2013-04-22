package pt.isel.android.content;

import pt.isel.java.Func;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

abstract class OnSharedPreferencesChangeFunc<T> implements OnSharedPreferenceChangeListener {
	
	private final String _key;
	private final Func<Void, T> _func;
	
	protected OnSharedPreferencesChangeFunc(String key, Func<Void, T> func) {
		
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
