package pt.isel.pdm.yamba.views;

import java.util.Date;

import pt.isel.android.Mailer;
import pt.isel.pdm.yamba.R;
import pt.isel.pdm.yamba.twitter.TwitterAsync;
import pt.isel.pdm.yamba.views.models.TweetViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsActivity extends YambaBaseActivity {	

	public static final String TWEET_VIEW_PARAMETER = "tweet_param";
	
	private TextView _tvAuthor, _tvTweet, _tvDate, _tvId;
	private TweetViewModel _viewModel;
	
	public DetailsActivity() {
		super(R.menu.details);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		_tvAuthor = (TextView) findViewById(R.id.details_author);
		_tvTweet = (TextView) findViewById(R.id.details_tweet);
		_tvDate = (TextView) findViewById(R.id.details_date);
		_tvId = (TextView) findViewById(R.id.details_id);
		
		onNewIntent(getIntent());
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		_viewModel = intent.getParcelableExtra(TWEET_VIEW_PARAMETER);
		showTweet(_viewModel);		
		super.onNewIntent(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		final int itemId = item.getItemId();		
		if(itemId == R.id.action_sendstatus_email) {
			mailStatus(_viewModel);
		}
		return super.onOptionsItemSelected(item);
	}

	private void showTweet(final TweetViewModel model) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				_tvAuthor.setText(model.getUsername());
				_tvTweet.setText(model.getTweet());
				_tvDate.setText(model.getTweetDate().toString());
				_tvId.setText(String.valueOf(model.getID()));				
		}});
	}
	
	private void mailStatus(TweetViewModel status) {
		final String subject = getString(R.string.mail_timeline_subject, TwitterAsync.getUsername(), new Date());
		final String body = getString(R.string.mail_timeline_body_tweet, status.getTweet(), status.getUsername());	
		
		Mailer.pickDestinataryAndSend(this, subject, body);
	}
}
