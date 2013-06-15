package pt.isel.pdm.yamba.menu.inflater;

import pt.isel.pdm.yamba.R;
import pt.isel.pdm.yamba.menu.YambaActivityToMenuIdMapper;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;

public class YambaMenuInflater extends MenuInflater {

	private static final int TEMPLATE_MENURES = R.menu.yamba_base;
	
	private Class<? extends Activity> _class;
	
	public YambaMenuInflater(Activity activity) {
		super(activity);
		
		this._class = activity.getClass();
	}
	
	@Override
	public void inflate(int menuRes, Menu menu) {
		super.inflate(TEMPLATE_MENURES, menu);
		
		int menuIdToRemove = YambaActivityToMenuIdMapper.getMenuIdForActivity(_class);
		
		if(menuIdToRemove != 0)
			menu.findItem(menuIdToRemove).setVisible(false);
	}
}
