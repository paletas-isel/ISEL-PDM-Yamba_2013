package pt.isel.java;

import android.annotation.SuppressLint;

public class Version {
	private int _major, _minor, _revision;
	
	public Version(int major, int minor, int revision) {
		_major = major;
		_minor = minor;
		_revision = revision;
	}
	
	public int getMajor() {
		return _major;
	}
	
	public int getMinor() {
		return _minor;
	}
	
	public int getRevision() {
		return _revision;
	}
	
	@SuppressLint("DefaultLocale")
	public String toString() {
		return String.format("%d.%d.%d", _major, _minor, _revision);		
	}
	
	@SuppressLint("DefaultLocale")
	public int toSQLiteVersion() {
		return Integer.parseInt(String.format("%3d%3d%3d", _major, _minor, _revision).replace(' ', '0'));
	}
	
	public static Version fromSQLiteVersion(int version) {
		int major = version / 1000000;
		version = version % 1000000;		
		int minor = version / 1000;
		version = version % 1000;		
		int revision = version % 1000;
		return new Version(major, minor, revision);
	}
}
