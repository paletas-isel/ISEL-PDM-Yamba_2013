package pt.isel.pdm.yamba.TwitterAsync.helpers;

import java.lang.ref.WeakReference;

public class Extractor {

	public static <T> T extract(WeakReference<T> reference) {
		
		return reference.get();
		
	}
	
}
