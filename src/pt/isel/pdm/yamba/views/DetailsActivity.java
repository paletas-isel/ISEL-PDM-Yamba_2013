package pt.isel.pdm.yamba.views;

import pt.isel.pdm.yamba.R;
import pt.isel.pdm.yamba.views.models.TweetViewModel;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class DetailsActivity extends Activity {	

	public static final String TWEET_VIEW_PARAMETER = "tweet_param";
	
	private TextView _tvAuthor, _tvTweet, _tvDate, _tvId;
	
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
		TweetViewModel model = intent.getParcelableExtra(TWEET_VIEW_PARAMETER);
		showTweet(model);		
		super.onNewIntent(intent);
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
}
