package pt.isel.pdm.yamba.twitter.services;

import java.util.Collection;
import java.util.LinkedList;

import pt.isel.android.content.SharedPreferencesListener;
import pt.isel.java.Action;
import android.app.Service;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public abstract class YambaBaseService extends Service {

	private Collection<Object> _foreverAlone;
		
	protected YambaBaseService() {
		this._foreverAlone = new LinkedList<Object>();
	}
	
	public <T> void registerAndTriggerFirst(String key, T defaultValue, Action<T> func, Class<T> type) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		_foreverAlone.add(SharedPreferencesListener.registerAndTriggerFirst(prefs, key, defaultValue, func, type));		
	}	
}
