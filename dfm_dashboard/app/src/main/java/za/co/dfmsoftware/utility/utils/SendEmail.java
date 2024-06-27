package za.co.dfmsoftware.utility.utils;

import android.content.Intent;

public class SendEmail {

    public static Intent createEmailIntent(String recipientEmail, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        return emailIntent;
    }
}
