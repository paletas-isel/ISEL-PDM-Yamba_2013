package pt.isel.pdm.yamba.data.versions;

import pt.isel.java.Version;

public interface DatabaseVersion {
	Version getVersion();
	
	String createScript();
	
	String upgradeScript(Version version);
}
