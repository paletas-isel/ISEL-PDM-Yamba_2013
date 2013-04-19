package pt.isel.pdm.yamba.views;

import pt.isel.pdm.yamba.R;
import pt.isel.pdm.yamba.views.models.StatusViewModel;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StatusActivity extends YambaBaseActivity {
	
	private final Object _sendTweetLockObj = new Object();
	
	private final StatusViewModel _viewModel;
	
	public StatusActivity() {
		super(StatusActivity.class, R.menu.status);
		
		_viewModel = new StatusViewModel(this);
	}

	// TODO Study InputFilters for message validation
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		
		final TextView remainingCharacters = (TextView) findViewById(R.id.status_remaining_characters);
		final EditText statusMsg = (EditText) findViewById(R.id.status_message);
		final Button send = (Button) findViewById(R.id.status_send);
		
		statusMsg.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				_viewModel.setMessage(s.toString());
				remainingCharacters.setText(Integer.toString(_viewModel.getRemainingCharacters()));
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
					
				synchronized(_sendTweetLockObj) {
						
					if(!send.isEnabled())
						return;
					
					new AsyncTask<Void, Void, Void>() {

						@Override
						protected void onPreExecute() {
							send.setEnabled(false);
							statusMsg.setEnabled(false);
							super.onPreExecute();
						}
						
						@Override
						protected Void doInBackground(Void... params) {
							_viewModel.sendStatusCommand();
							return null;
						}
						
						protected void onPostExecute(Void result) {
							statusMsg.setText("");
							statusMsg.setEnabled(true);
							send.setEnabled(true);
						}
					}.execute();
				}
			}
		});
	}
}
