package pt.isel.pdm.yamba.views;

import pt.isel.java.Action;
import pt.isel.pdm.yamba.R;
import pt.isel.pdm.yamba.settings.Settings;
import pt.isel.pdm.yamba.views.models.StatusViewModel;
import android.content.SharedPreferences;
import winterwell.jtwitter.Twitter.Status;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StatusActivity extends YambaBaseActivity {
	
	private final StatusViewModel _viewModel;
	
	private TextView _remainingCharacters; 
	
	public StatusActivity() {
		super(R.menu.status);
		_viewModel = new StatusViewModel(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		
		registerAndTriggerFirst(
			Settings.Status.MaxCharacters, 
			Integer.toString(StatusViewModel.STATUS_MAXSIZE_DEFAULT), 
			new Action<String>() {
				@Override
				public void execute(String param) {
					_viewModel.setStatusMaxSize(Integer.parseInt(param));
				}
			},
			String.class
		);
		
		_remainingCharacters = (TextView) findViewById(R.id.status_remaining_characters);
		
		final TextView remainingCharacters = _remainingCharacters;
		final EditText statusMsg = (EditText) findViewById(R.id.status_message);
		final Button send = (Button) findViewById(R.id.status_send);
		
		statusMsg.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				_viewModel.setMessage(s.toString());
				remainingCharacters.setText(Integer.toString(_viewModel.getRemainingCharacters()));
				
				send.setEnabled(_viewModel.getRemainingCharacters() >= 0);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
			}
		});
		
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!send.isEnabled())
					return;
				
				send.setEnabled(false);
				statusMsg.setEnabled(false);
				
				_viewModel.sendStatusCommandAsync(new Action<Status>() {

					public void execute(Status param) {
						statusMsg.setText("");
						statusMsg.setEnabled(true);
						send.setEnabled(true);
					}
				});
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		_remainingCharacters.setText(Integer.toString(_viewModel.getRemainingCharacters()));
	}
}
