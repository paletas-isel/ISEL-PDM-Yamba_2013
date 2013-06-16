package pt.isel.pdm.yamba.data.versions;

import pt.isel.java.Version;

public class Version1_0_0 extends DefaultVersionProcess {

	private static Version _version = new Version(1, 0, 0);
	
	private String TableTimeline = "CREATE TABLE Timeline ( ID INTEGER PRIMARY KEY AUTOINCREMENT, SERVERID INTEGER NOT NULL, AUTHOR TEXT NOT NULL, MESSAGE TEXT NOT NULL, PUBLICATION_DATE TEXT NOT NULL, REPLY_TO INT NOT NULL, PUBLISHED INTEGER NOT NULL)";
	
	@Override
	public Version getVersion() {
		return _version;
	}

	@Override
	protected String createDDLScript() {
		return String.format("%s", TableTimeline);
	}

	@Override
	protected String createDMLScript() {
		return EmptyString;
	}

	@Override
	protected String upgradeDDLScript(Version version) {
		return EmptyString;
	}

	@Override
	protected String upgradeDMLScript(Version version) {
		return EmptyString;
	}

}
