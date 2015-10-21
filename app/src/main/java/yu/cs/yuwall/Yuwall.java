package yu.cs.yuwall;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import yu.cs.yuwall.utils.ParseConstants;

/**
 * Created by Dell on 10/9/2015.
 */
public class Yuwall extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "I6b99wcqRWRvMidSF0qm6fBDfqxiq9dK8gR85Arg", "C9pDP6hgMUsmMca1DLodci6aXtxvwo3u8eZL0JTR");
        //super nsd
        //another  is testing

    }

    public static void updateParseInstallation(ParseUser user) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();

    }


}
