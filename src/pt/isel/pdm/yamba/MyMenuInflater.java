package pt.isel.pdm.yamba;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;

public class MyMenuInflater extends MenuInflater {
	
	public MyMenuInflater(Context context) {
		super(context);
	}
	
	@Override
	public void inflate(int menuRes, Menu menu) {
		
		super.inflate(menuRes, menu);
	}
}
