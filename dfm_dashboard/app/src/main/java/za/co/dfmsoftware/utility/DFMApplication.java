package za.co.dfmsoftware.utility;

import android.app.Application;

import io.realm.Realm;
import za.co.dfmsoftware.utility.realm.DfmRealm;
import za.co.dfmsoftware.utility.service.DfmRetrofit;

/**
 *
 * DFM Application
 * This is the start of the Mobile Application:
 * Here is the initialisation of the Crashlytics, retrofit, DB(Realm)
 */

public class DFMApplication extends Application {
    public void onCreate() {
        super.onCreate();

        // Initialize Realm
        Realm.init(this);

        DfmRealm realmContext = DfmRealm.getInstance();
        realmContext.initRealm(this); //initialise realm with application context

        DfmRetrofit dfmRetrofit = DfmRetrofit.getInstance();
        dfmRetrofit.init(realmContext);

        //TODO add Crashlytics to project
    }
}
