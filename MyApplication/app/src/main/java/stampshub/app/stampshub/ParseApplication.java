package stampshub.app.stampshub;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;
import com.parse.ParseUser;


public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Data store.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "6md5fSIFNrcpXc0ZStnJos7SW4Rnrok0Hyu7Uoes", "BZxjWEwJogpEgnfxp6LWGIzwQYrBFzKtbszbTBM5");

        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
