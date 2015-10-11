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
        Parse.initialize(this, "De8VXyenCy7VefysTTW8XccrxhNrxzg4tlW9ibmE", "puH5gzDQp2d3R80lsgXzsB0NScyjjrUMWRU734qi");
        //super noob
    }
    public static void updateParseInstallation(ParseUser user) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

       installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();

    }


}
