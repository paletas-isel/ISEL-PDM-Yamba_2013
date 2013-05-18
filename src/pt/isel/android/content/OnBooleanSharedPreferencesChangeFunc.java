package pt.isel.android.content;

import pt.isel.java.Action;
import android.content.SharedPreferences;

public class OnBooleanSharedPreferencesChangeFunc extends OnSharedPreferencesChangeFunc<Boolean> {

	private final boolean _defValue;
	
	public OnBooleanSharedPreferencesChangeFunc(String key, Action<Boolean> func, boolean defValue) {
		super(key, func);
		this._defValue = defValue;
	}

	@Override
	protected Boolean getValueFromSharedPreferences(SharedPreferences preferences, String key) {
		
		return preferences.getBoolean(key, _defValue);
	}
}
