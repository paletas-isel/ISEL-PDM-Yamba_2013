package pt.isel.pdm.yamba.twitter.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pt.isel.java.Action;
import pt.isel.pdm.yamba.data.model.TimelineStatus;
import pt.isel.pdm.yamba.data.model.TimelineStatusDataSource;
import pt.isel.pdm.yamba.twitter.TwitterAsync;
import pt.isel.pdm.yamba.twitter.listeners.TimelineObtainedListener;
import pt.isel.pdm.yamba.views.models.TimelineViewModel;
import winterwell.jtwitter.Twitter;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class TimelinePullService extends YambaBaseService {

	public final static String PARAM_TAG = "PARAM_TAG";
	
	private Iterable<TimelineStatus> _timeline;	
	private long _lastSchedule = 0;	
	private TimelineViewModel _viewModel;
	private boolean _initializing = false;
	private TimelineStatusDataSource _dataSource;
	
	@Override
	public void onCreate() {
		_viewModel = new TimelineViewModel();
		initializeSettingsRefresh();
		
		_dataSource = new TimelineStatusDataSource(this.getBaseContext());
		_dataSource.open();
		
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		_dataSource.close();
		super.onDestroy();
	}

	private void initializeSettingsRefresh() {
		_initializing = true;
		
		registerAndTriggerFirst(
			pt.isel.pdm.yamba.settings.Settings.Timeline.Size, 
			TimelineViewModel.MAX_SAVED_TWEETS, 
			new Action<Integer>() {
				@Override
				public void execute(Integer param) {
					_viewModel.setMaxSavedTweets(param);
				}
			},
			Integer.class
		);	
		
		registerAndTriggerFirst(
			pt.isel.pdm.yamba.settings.Settings.Timeline.RefreshRate, 
			TimelineViewModel.AUTO_REFRESH_RATE, 
			new Action<Integer>() {
				@Override
				public void execute(Integer param) {
					_viewModel.setAutoRefreshRate(param);

					if(!_initializing) restartTimer();
				}
			},
			Integer.class
		);
		
		registerAndTriggerFirst(
			pt.isel.pdm.yamba.settings.Settings.Timeline.AutoRefresh, 
			TimelineViewModel.AUTO_REFRESH, 
			new Action<Boolean>() {
				@Override
				public void execute(Boolean param) {
					_viewModel.setAutoRefreshEnabled(param);

					if(!_initializing) restartTimer();
				}
			},
			Boolean.class
		);
		
		_initializing = false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private final TwitterAsync _twitterAsync = TwitterAsync.connect();
		
	private Handler _handler = new Handler(); 
	
	private Timer _timer;
	
	private TimerTask _task;
	
	private TimerTask createTask() {
		return new TimerTask() {

			@Override
			public void run() {
				Twitter connection = _twitterAsync.getInnerConnection();
				
				_timeline = TimelineStatus.from(connection.getUserTimeline().subList(0, _viewModel.getMaxSavedTweets()));
										
				_handler.post(new Thread() {
					
					@Override
					public void run() {
						  
						saveStatuses(_timeline);
						
						TimelineObtainedListener listener = _twitterAsync.getTimelineObtainedListener();
						if(listener != null && _timeline != null) {
							listener.onTimelineObtained(_timeline);
						}
					}
					
					private void saveStatuses(Iterable<TimelineStatus> statuses) {
						for(TimelineStatus status : statuses) {
							_dataSource.createStatus(status);
						}
					}
				});
			}		
		};
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
				
		int initialDelay = 0;
		if(loadSavedTweets()) {
			initialDelay = _viewModel.getAutoRefreshRate();
		}
		
		startTimer(initialDelay);	
			
		return Service.START_REDELIVER_INTENT;
	}
	
	private boolean loadSavedTweets() {
		boolean hasSavedTweets = false;
		
		if(_timeline == null) {
			List<TimelineStatus> timeline = _dataSource.getTimeline();
			if(!timeline.isEmpty()) { 
				_timeline = timeline;
				hasSavedTweets = true;
			}
		}
		
		if(_timeline != null)  _twitterAsync.getTimelineObtainedListener().onTimelineObtained(_timeline);
		return hasSavedTweets;
	}
	
	private void startTimer(int initialDelay) {
		if(_timer != null) {
			_task.cancel();
			_timer.purge();
		}
		else {
			_timer = new Timer();
		}
		_task = createTask();		
		_lastSchedule = System.currentTimeMillis() + (initialDelay - _viewModel.getAutoRefreshRate());
		
		if(_viewModel.isAutoRefreshEnabled())
			scheduleLooped(_task);
		else
			scheduleOnce(_task);
	}
	
	private void restartTimer() {
		startTimer(0);
	}
	
	private void scheduleLooped(TimerTask task) {
		long curr, left = (curr = System.currentTimeMillis()) - _lastSchedule;
		left = (_viewModel.getAutoRefreshRate() - (left / 1000)) * 1000;
		if(left < 0) left = 0;				
		
		_timer.scheduleAtFixedRate(task, left, _viewModel.getAutoRefreshRate() * 1000);		
		_lastSchedule = curr;
	}
	
	private void scheduleOnce(TimerTask task) {
		long curr, left = (curr = System.currentTimeMillis()) - _lastSchedule;
		left = (_viewModel.getAutoRefreshRate() - (left / 1000)) * 1000;
		if(left < 0) left = 0;
		
		_timer.schedule(task, left);		
		_lastSchedule = curr;		
	}
}
