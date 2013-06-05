package pt.isel.pdm.yamba.content;

import pt.isel.pdm.yamba.data.DatabaseManager;
import pt.isel.pdm.yamba.data.model.TimelineStatusDataSource;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class YambaTweetsProvider extends ContentProvider {

	private DatabaseManager _manager;
	
	private static final String AUTHORITY = "vnd.android.cursor.item.timeline";
	private static final String BASE_PATH = "content://" + AUTHORITY + "/yamba/timeline/";
	public static final String ID_QUERY_PATH = BASE_PATH + "id";
	private static final int ID_QUERY_PATH_CODE = 1;
	public static final String ALL_QUERY_PATH = BASE_PATH;
	private static final int ALL_QUERY_PATH_CODE = 2;
	
	private static UriMatcher ACTION_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		ACTION_MATCHER.addURI(AUTHORITY, ID_QUERY_PATH, ID_QUERY_PATH_CODE);
		ACTION_MATCHER.addURI(AUTHORITY, ALL_QUERY_PATH, ALL_QUERY_PATH_CODE);
	}

	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		SQLiteDatabase sqlDB = _manager.getWritableDatabase();
	    int rowsDeleted = sqlDB.delete(TimelineStatusDataSource.TABLE_NAME, whereClause, whereArgs);
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	}

	@Override
	public String getType(Uri arg0) {
		return AUTHORITY;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase sqlDB = _manager.getWritableDatabase();
	    long id = sqlDB.insert(TimelineStatusDataSource.TABLE_NAME, null, values);
	    getContext().getContentResolver().notifyChange(uri, null);
	    return Uri.parse(BASE_PATH + id);
	}

	@Override
	public boolean onCreate() {
		_manager = new DatabaseManager(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

	    queryBuilder.setTables(TimelineStatusDataSource.TABLE_NAME);

	    switch(ACTION_MATCHER.match(uri)) 
	    {
		    case ID_QUERY_PATH_CODE : 
		    	queryBuilder.appendWhere(TimelineStatusDataSource.ID_COLUMN + "=" + uri.getLastPathSegment());
		    	break;
	    }
	    
	    SQLiteDatabase db = _manager.getWritableDatabase();
	    Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);

	    return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase sqlDB = _manager.getWritableDatabase();
	    int rowsUpdated = 0;
	    sqlDB.update(TimelineStatusDataSource.TABLE_NAME, values, selection, selectionArgs);
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsUpdated;
	}
}
