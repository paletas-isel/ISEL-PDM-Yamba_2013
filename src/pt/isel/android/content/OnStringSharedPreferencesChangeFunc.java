package pt.isel.android.content;

import pt.isel.java.Action;
import android.content.SharedPreferences;

public class OnStringSharedPreferencesChangeFunc extends OnSharedPreferencesChangeFunc<String> {

	private final String _defValue;
	
	public OnStringSharedPreferencesChangeFunc(String key, Action<String> func, String defValue) {
		super(key, func);
		this._defValue = defValue;
	}

	@Override
	protected String getValueFromSharedPreferences(
			SharedPreferences preferences, String key) {
		
		return preferences.getString(key, _defValue);
	}
}
