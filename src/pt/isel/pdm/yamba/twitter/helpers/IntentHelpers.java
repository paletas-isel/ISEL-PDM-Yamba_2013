package pt.isel.pdm.yamba.twitter.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

public class IntentHelpers {

	public static <A extends Parcelable> Intent generateIntentWithParams(Context context, Class<?> target, String paramTag, A params) {
		
		Intent intent = new Intent(context, target);
		intent.putExtra(paramTag, params);
		
		return intent;
	}
	
	public static <A extends Parcelable> Intent generateIntentWithString(Context context, Class<?> target, String paramTag, String params) {
		
		Intent intent = new Intent(context, target);
		intent.putExtra(paramTag, params);
		
		return intent;
	}
	
	public static Intent generateIntent(Context context, Class<?> target) {
		
		Intent intent = new Intent(context, target);
		
		return intent;
	}	
}
