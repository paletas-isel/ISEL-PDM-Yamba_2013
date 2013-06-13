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
import android.util.Log;

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
				Log.d(getClass().getName(), "Obtaining timeline from the service..");
				
				Twitter connection = _twitterAsync.getInnerConnection();
				
				_timeline = TimelineStatus.from(connection.getUserTimeline().subList(0, _viewModel.getMaxSavedTweets()));
				
				Log.d(getClass().getName(), "Timeline obtained, updating in memory statuses..");
										
				_handler.post(new Thread() {
					
					@Override
					public void run() {						  
						updateSavedStatus(_timeline, _viewModel.getMaxSavedTweets());
						
						TimelineObtainedListener listener = _twitterAsync.getTimelineObtainedListener();
						if(listener != null && _timeline != null) {
							listener.onTimelineObtained(_timeline);
						}
					}
					
					private void updateSavedStatus(Iterable<TimelineStatus> downloadedTimeline, int maxSaved) {
						Log.d(getClass().getName(), String.format("Updating saved statuses (Max. Allowed %d)..", maxSaved));

						List<TimelineStatus> timeline = _dataSource.getTimeline();
						
						if(!timeline.isEmpty()) {
							TimelineStatus first = timeline.get(0);
							int savedCount = saveStatuses(downloadedTimeline, first);
							int totalSaved = timeline.size() + savedCount;
							if(totalSaved > maxSaved)
								deleteLastStatuses(timeline, maxSaved, totalSaved - savedCount);
						}
						else {
							saveStatuses(downloadedTimeline, null);
						}
					}
					
					private int saveStatuses(Iterable<TimelineStatus> statuses, TimelineStatus lastSavedStatus) {
						Log.d(getClass().getName(), "Saving new statuses..");
						
						int saved = 0;
						for(TimelineStatus status : statuses) {
							if(lastSavedStatus == null || status.getDate().compareTo(lastSavedStatus.getDate()) > 0) {
								_dataSource.createStatus(status);
								saved++;
							}
						}

						Log.d(getClass().getName(), String.format("Saved new %d statuses!", saved));
						
						return saved;
					}
					
					private void deleteLastStatuses(List<TimelineStatus> statuses, int maxSaved, int toDelete) {
						Log.d(getClass().getName(), String.format("Deleting %d excess statuses..", toDelete));
						for(TimelineStatus status : statuses.subList(statuses.size() - toDelete, statuses.size())) {
							_dataSource.deleteStatus(status);
						}
					}
				});
			}		
		};
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {				
		if(loadSavedTweets()) {
			_viewModel.getAutoRefreshRate();
		}
		
		startTimer();	
			
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
	
	private void startTimer() {
		if(_timer != null) {
			_task.cancel();
			_timer.purge();
		}
		else {
			_timer = new Timer();
		}
		_task = createTask();		
		
		if(_viewModel.isAutoRefreshEnabled())
			scheduleLooped(_task);
		else
			scheduleOnce(_task);
	}
	
	private void restartTimer() {
		startTimer();
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
