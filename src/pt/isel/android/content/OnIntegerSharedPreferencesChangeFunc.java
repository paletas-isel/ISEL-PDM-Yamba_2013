package pt.isel.android.content;

import pt.isel.java.Action;
import android.content.SharedPreferences;

public class OnIntegerSharedPreferencesChangeFunc extends OnSharedPreferencesChangeFunc<Integer> {

	private final int _defValue;
	
	public OnIntegerSharedPreferencesChangeFunc(String key, Action<Integer> func, int defValue) {
		super(key, func);
		this._defValue = defValue;
	}

	@Override
	protected Integer getValueFromSharedPreferences(SharedPreferences preferences, String key) {
		
		return Integer.valueOf(preferences.getString(key, String.valueOf(_defValue)));
	}
}
