package pt.isel.pdm.yamba.views;

import pt.isel.pdm.yamba.R;
import pt.isel.pdm.yamba.views.models.TweetViewModel;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class DetailsActivity extends Activity {	

	public static final String TWEET_VIEW_PARAMETER = "tweet_param";
	
	private TextView _tvAuthor, _tvTweet, _tvDate, _tvId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		View v = getLayoutInflater().inflate(R.layout.activity_details, null);
		_tvAuthor = (TextView) v.findViewById(R.id.details_author);
		_tvTweet = (TextView) v.findViewById(R.id.details_tweet);
		_tvDate = (TextView) v.findViewById(R.id.details_date);
		_tvId = (TextView) v.findViewById(R.id.details_id);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}

	private void showTweet(TweetViewModel model) {
		_tvAuthor.setText(model.getUsername());
		_tvTweet.setText(model.getTweet());
		_tvDate.setText(model.getTweetDate().toString());
		_tvId.setText(String.valueOf(model.getID()));
	}
}
