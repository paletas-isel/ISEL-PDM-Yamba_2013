package pt.isel.pdm.yamba.twitter;

import pt.isel.pdm.yamba.exceptions.BadCredentialsException;
import pt.isel.pdm.yamba.exceptions.ExceptionWrapper;
import pt.isel.pdm.yamba.exceptions.InvalidAPIException;
import pt.isel.pdm.yamba.exceptions.TwitterException;
import winterwell.jtwitter.Twitter;
import android.os.AsyncTask;
import android.util.Log;

public abstract class TwitterAsyncTask<A, B, C> extends AsyncTask<A, B, C>{

	private TwitterAsync _connection;
	private TwitterException _innerException;
	
	public TwitterAsyncTask(TwitterAsync connection) {
		_connection = connection;
	}
	
	protected Twitter getConnection() {
		return _connection.getInnerConnection();
	}
	
	protected TwitterAsync getTwitterAsync() {
		return _connection;
	}

	@Override
	protected final C doInBackground(A... arg0) {
		try {
			return doExecute(arg0);			
		}
		catch(winterwell.jtwitter.TwitterException.E404 e) {
			_innerException = new InvalidAPIException();
		}
		catch(TwitterException e) {
			_innerException = e;
		}
		catch (Exception e) {
			
			if(e.getMessage().contains("authentication"))
				_innerException = new BadCredentialsException();
			else {
				Log.e(e.getClass().toString(), e.getMessage());
				_innerException = new ExceptionWrapper(e);
			}
		}
		
		return null;
	}	
	
	@Override
	protected final void onPostExecute(C result) {
		super.onPostExecute(result);
		
		if(_innerException == null)
			doPostExecute(result);
		else {
			TwitterAsync.notifyException(_innerException);
			_innerException = null;
		}
	}

	protected TwitterException getInnerException() {
		return _innerException;
	}
	
	protected abstract C doExecute(A... arg0);
	protected void doPostExecute(C result) {};
}
