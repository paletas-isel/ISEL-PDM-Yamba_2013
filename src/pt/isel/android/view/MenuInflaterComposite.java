package pt.isel.android.view;

import android.content.Context;
import android.view.MenuInflater;

public class MenuInflaterComposite extends MenuInflater {

	private final Iterable<MenuInflater> _inflaters;
	
	public MenuInflaterComposite(Context context, Iterable<MenuInflater> inflaters) {
		super(context);
		
		this._inflaters = inflaters;
	}
	
	public void inflate(int menuRes, android.view.Menu menu) {
		
		for(MenuInflater inflater : _inflaters) {
			inflater.inflate(menuRes, menu);
		}
	};
}
