package pt.isel.pdm.yamba.data.versions;

import pt.isel.java.Version;

public abstract class DefaultVersionProcess implements DatabaseVersion {

	protected static String EmptyString = "";
	
	protected abstract String createDDLScript();
	protected abstract String createDMLScript();
	
	@Override
	public String createScript() {
		return String.format("%s ; %s", createDDLScript(), createDMLScript());
	}
	
	protected abstract String upgradeDDLScript(Version version);
	protected abstract String upgradeDMLScript(Version version);

	@Override
	public String upgradeScript(Version version) {
		return String.format("%s ; %s", upgradeDDLScript(version), upgradeDMLScript(version));
	}
}
