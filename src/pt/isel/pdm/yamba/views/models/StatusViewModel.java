package pt.isel.pdm.yamba.views.models;


public class StatusViewModel {
	
	public static final int STATUS_MAXSIZE_DEFAULT = 140;
	
	private final Context _context;
	private String _message;
	private int _statusMaxSize;
	
	public StatusViewModel(Context context) {
		
		this._context = context;
		setMessage("");
		setStatusMaxSize(STATUS_MAXSIZE_DEFAULT);
	}
	
	public void setMessage(String message) {
		_message = message;
	}
	
	public String getMessage() {
		return _message;
	}
	
	public void setStatusMaxSize(int size) {
		_statusMaxSize = size;
	}
	
	public int getRemainingCharacters() {
		return _statusMaxSize - _message.length();
	}
	

	public void sendStatusCommandAsync(final Action<Status> callback) {
		
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				callback.execute(null);				
			}
		};
		
		Messenger messenger = new Messenger(handler);
		
		Intent intent = new Intent(_context, StatusUploadService.class);
		intent.putExtra(StatusUploadService.Keys.Intent.Message, _message);
		intent.putExtra(StatusUploadService.Keys.Intent.Messenger, messenger);
		
		_context.startService(intent);
	}
}
