package pt.isel.pdm.yamba.data;

import pt.isel.java.Version;
import pt.isel.pdm.yamba.data.versions.DatabaseVersion;
import pt.isel.pdm.yamba.data.versions.Version1_0_0;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "YAMBAPDM_DB";
	
	private static final DatabaseVersion[] DATABASE_VERSIONS = new DatabaseVersion[] 
	{ 
		new Version1_0_0()
	};
	
	private static final DatabaseVersion CURRENT_VERSION = new Version1_0_0();
	
	public DatabaseManager(Context context) {
		super(context, DATABASE_NAME, null, CURRENT_VERSION.getVersion().toSQLiteVersion());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CURRENT_VERSION.createScript());		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		StringBuilder upgradeScript = new StringBuilder();
		for(int ix = 0; ix < DATABASE_VERSIONS.length; ++ix) {
			DatabaseVersion currVersion = DATABASE_VERSIONS[ix];
			if(oldVersion + 1 == currVersion.getVersion().toSQLiteVersion()) {
				upgradeScript.append(currVersion.upgradeScript(Version.fromSQLiteVersion(newVersion)));
				upgradeScript.append(";");
			}
		}
		
		db.execSQL(upgradeScript.toString());
	}
}
