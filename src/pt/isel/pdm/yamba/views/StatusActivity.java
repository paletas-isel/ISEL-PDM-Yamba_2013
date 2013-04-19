package pt.isel.pdm.yamba.views;

import pt.isel.pdm.yamba.R;
import android.os.Bundle;

public class StatusActivity extends YambaBaseActivity {

	public StatusActivity() {
		super(StatusActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
	}
}
