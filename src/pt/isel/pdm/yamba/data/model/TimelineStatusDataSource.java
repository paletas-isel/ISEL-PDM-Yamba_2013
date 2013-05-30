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

	public static final String[] COLUMNS = new String[] { "ID", "MESSAGE", "PUBLICATION_DATE" };
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
	private static int MESSAGE_COLUMNIDX = 1;
	private static int PUBLICATIONDATE_COLUMNIDX = 2;
	private static int REPLYTO_COLUMNIDX = 3;
	private static int PUBLISHED_COLUMNIDX = 4;
	public static String ID_COLUMN = COLUMNS[ID_COLUMNIDX];
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
		_dbHelper.close();
	}

	public TimelineStatus createStatus(String message, Date publicationDate, int replyTo, boolean published) {
		ContentValues values = new ContentValues();
		values.put(TimelineStatusDataSource.MESSAGE_COLUMN, message);
		values.put(TimelineStatusDataSource.PUBLICATIONDATE_COLUMN, df.format(publicationDate));
		values.put(TimelineStatusDataSource.REPLYTO_COLUMN, replyTo);
		values.put(TimelineStatusDataSource.PUBLISHED_COLUMN, published);		
		long insertId = _database.insert(getTableName(), null, values);
		
		Cursor cursor = _database.query(getTableName(), getColumns(), ID_COLUMN + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		TimelineStatus newComment = cursorToStatus(cursor);
		cursor.close();
		return newComment;
	}

	public void deleteStatus(TimelineStatus status) {
		long id = status.getID();
		_database.delete(getTableName(), ID_COLUMN + " = " + id, null);
	}

	public List<TimelineStatus> getAllComments() {
		List<TimelineStatus> statuses = new ArrayList<TimelineStatus>();

		Cursor cursor = _database.query(getTableName(), getColumns(), null, null, null, null, null);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			TimelineStatus status = cursorToStatus(cursor);
			statuses.add(status);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return statuses;
	}

	private static TimelineStatus cursorToStatus(Cursor cursor) {
		TimelineStatus status;
		try {
			status = new TimelineStatus
			(
				cursor.getString(MESSAGE_COLUMNIDX), 
				df.parse(cursor.getString(PUBLICATIONDATE_COLUMNIDX)),
				cursor.getInt(REPLYTO_COLUMNIDX),
				cursor.getInt(PUBLISHED_COLUMNIDX) != 0
			);
		} catch (ParseException e) {
			status = new TimelineStatus
			(
				cursor.getString(MESSAGE_COLUMNIDX), 
				new Date(),
				cursor.getInt(REPLYTO_COLUMNIDX),
				cursor.getInt(PUBLISHED_COLUMNIDX) != 0
			);
		}
		status.setID(cursor.getLong(ID_COLUMNIDX));
		return status;
	}
}
