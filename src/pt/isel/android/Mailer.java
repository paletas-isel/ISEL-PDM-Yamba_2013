package pt.isel.android;

import java.util.ArrayList;
import java.util.List;

import pt.isel.pdm.yamba.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;
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
	
	public static void pickDestinataryAndSend(final Context ctx, final String subject, final String body) {
		final EditText input = new EditText(ctx);
		new AlertDialog.Builder(ctx)
		    .setTitle(R.string.mail_destinary_title)
		    .setMessage(R.string.mail_destinary_desc)
		    .setPositiveButton(R.string.mail_destinary_okbutton, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String destinatary = input.getText().toString();
					if(destinatary.equals("")) {
						Toast.makeText(ctx, R.string.mail_send_error, Toast.LENGTH_LONG).show();
					}
					else {
						Mailer.sendEmail(ctx, subject, body, destinatary);
					}
				}
			})
		    .setView(input)
		    .show();
	}
}
