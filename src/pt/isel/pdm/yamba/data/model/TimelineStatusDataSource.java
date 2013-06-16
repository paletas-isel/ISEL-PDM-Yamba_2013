package pt.isel.pdm.yamba.data.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pt.isel.pdm.yamba.data.DatabaseManager;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TimelineStatusDataSource implements DatabaseTable {

	public static final String[] COLUMNS = new String[] { "ID", "SERVERID", "AUTHOR", "MESSAGE", "PUBLICATION_DATE", "REPLY_TO", "PUBLISHED" };
	public static final String TABLE_NAME = "Timeline";
	
	@Override
	public String[] getColumns() {
		return COLUMNS;
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	// Database fields
	@SuppressLint("SimpleDateFormat")
	private static DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
	
	private SQLiteDatabase _database;
	private DatabaseManager _dbHelper;

	private static int ID_COLUMNIDX = 0;
	private static int SERVERID_COLUMNIDX = 1;
	private static int AUTHOR_COLUMNIDX = 2;
	private static int MESSAGE_COLUMNIDX = 3;
	private static int PUBLICATIONDATE_COLUMNIDX = 4;
	private static int REPLYTO_COLUMNIDX = 5;
	private static int PUBLISHED_COLUMNIDX = 6;
	public static String ID_COLUMN = COLUMNS[ID_COLUMNIDX];
	public static String SERVERID_COLUMN = COLUMNS[SERVERID_COLUMNIDX];
	public static String AUTHOR_COLUMN = COLUMNS[AUTHOR_COLUMNIDX];
	public static String MESSAGE_COLUMN = COLUMNS[MESSAGE_COLUMNIDX];
	public static String PUBLICATIONDATE_COLUMN = COLUMNS[PUBLICATIONDATE_COLUMNIDX];
	public static String REPLYTO_COLUMN = COLUMNS[REPLYTO_COLUMNIDX];
	public static String  PUBLISHED_COLUMN = COLUMNS[PUBLISHED_COLUMNIDX];
  
	public TimelineStatusDataSource(Context context) {
		_dbHelper = new DatabaseManager(context);
	}

	public void open() throws SQLException {
		_database = _dbHelper.getWritableDatabase();
	}

	public void close() {
		_database.close();
		_dbHelper.close();
	}
	
	private Cursor queryByServerId(long serverId) {
		return _database.query(getTableName(), getColumns(), SERVERID_COLUMN + " = " + serverId, null, null, null, null);
	}
	
	public boolean existsStatus(long serverId) {
		Cursor cursor = queryByServerId(serverId);
		return cursor.getCount() > 0;
	}

	public TimelineStatus getStatus(long serverId) {
		Cursor cursor = queryByServerId(serverId);
		cursor.moveToFirst();
		TimelineStatus status = cursorToStatus(cursor);
		cursor.close();
		return status;
	}

	public TimelineStatus createStatus(TimelineStatus status) {
		if(!existsStatus(status.getServerID())) {
			ContentValues values = getValues(status);	
			long insertId = _database.insert(getTableName(), null, values);
			
			Cursor cursor = _database.query(getTableName(), getColumns(), ID_COLUMN + " = " + insertId, null, null, null, null);
			cursor.moveToFirst();
			status = cursorToStatus(cursor);
			cursor.close();
		}
		return status;
	}

	public void deleteStatus(TimelineStatus status) {
		long id = status.getID();
		_database.delete(getTableName(), ID_COLUMN + " = " + id, null);
	}

	public List<TimelineStatus> getTimeline() {
		return getStatus(true);
	}
	
	public List<TimelineStatus> getUnpublishedStatuses() {
		return getStatus(false);
	}
	
	private List<TimelineStatus> getStatus(boolean published) {
				
		Cursor queryResult = _database.query(
				getTableName(), 
				getColumns(), 
				String.format("%s=%d", PUBLISHED_COLUMN, (published)?1:0), 
				null, 
				null,
				null,
				TimelineStatusDataSource.PUBLICATIONDATE_COLUMN + " DESC"
			);
			
		List<TimelineStatus> unpublished = new ArrayList<TimelineStatus>();
		
		while(queryResult.moveToNext()) {
			unpublished.add(TimelineStatusDataSource.cursorToStatus(queryResult));
		}
		queryResult.close();
		
		return unpublished;
	}
	
	private static TimelineStatus cursorToStatus(Cursor cursor) {
		TimelineStatus status;
		try {
			status = new TimelineStatus
			(
				cursor.getLong(SERVERID_COLUMNIDX),
				cursor.getString(AUTHOR_COLUMNIDX),
				cursor.getString(MESSAGE_COLUMNIDX), 
				df.parse(cursor.getString(PUBLICATIONDATE_COLUMNIDX)),
				cursor.getInt(REPLYTO_COLUMNIDX),
				cursor.getInt(PUBLISHED_COLUMNIDX) != 0
			);
		} catch (ParseException e) {
			status = new TimelineStatus
			(
				cursor.getLong(SERVERID_COLUMNIDX),
				cursor.getString(AUTHOR_COLUMNIDX),
				cursor.getString(MESSAGE_COLUMNIDX), 
				new Date(),
				cursor.getInt(REPLYTO_COLUMNIDX),
				cursor.getInt(PUBLISHED_COLUMNIDX) != 0
			);
		}
		status.setID(cursor.getLong(ID_COLUMNIDX));
		return status;
	}
	
	private static ContentValues getValues(TimelineStatus status) {
		ContentValues values = new ContentValues();
		values.put(SERVERID_COLUMN, status.getServerID());
		values.put(AUTHOR_COLUMN, status.getUsername());
		values.put(MESSAGE_COLUMN, status.getMessage());
		values.put(PUBLICATIONDATE_COLUMN,  df.format(status.getDate()));
		values.put(REPLYTO_COLUMN, status.getReplyTo());
		values.put(PUBLISHED_COLUMN,status.isPublished());
		return values;
	}
	
	private static ContentValues getValuesWithID(TimelineStatus status) {
		ContentValues values = getValues(status);
		values.put(ID_COLUMN, status.getID());
		return values;
	}
}
