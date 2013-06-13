package pt.isel.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Mailer {
	
	private static String[] prepareDestinariesArray(String destinatary1, String... destinaries) {
		List<String> dest = new ArrayList<String>();
		dest.add(destinatary1);
		for(String destinatary : destinaries) {
			dest.add(destinatary);		
		}
		String[] destArray = new String[dest.size()];
		return dest.toArray(destArray);
	}
	
	public static void sendEmail(Context ctx, String subject, String body, String destinatary1, String... otherDestinaries) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , prepareDestinariesArray(destinatary1, otherDestinaries));
		i.putExtra(Intent.EXTRA_SUBJECT, subject);
		i.putExtra(Intent.EXTRA_TEXT   , body);
		try {
		    ctx.startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(ctx, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}
}
