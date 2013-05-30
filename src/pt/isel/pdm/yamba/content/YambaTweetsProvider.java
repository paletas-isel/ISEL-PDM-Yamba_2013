package pt.isel.pdm.yamba.content;

import pt.isel.pdm.yamba.data.DatabaseManager;
import pt.isel.pdm.yamba.data.model.TimelineStatusDataSource;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class YambaTweetsProvider extends ContentProvider {

	private DatabaseManager _manager;
	private static String BASE_PATH = "/yamba/timeline/";

	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		SQLiteDatabase sqlDB = _manager.getWritableDatabase();
	    int rowsDeleted = sqlDB.delete(TimelineStatusDataSource.TABLE_NAME, whereClause, whereArgs);
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	}

	@Override
	public String getType(Uri arg0) {
		return "vnd.android.cursor.item.timeline_status";
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
	    queryBuilder.appendWhere(TimelineStatusDataSource.ID_COLUMN + "=" + uri.getLastPathSegment());

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
