package hu.ait.android.cevicheteam.ceviche;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Chau on 5/11/2015.
 */
public class CevicheApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "zmTYMIMWbDd5xpLzdBp9ROws1FDrjK6zFR2srpUr", "WmIpzFBYtPJXT87oMhs0lTsG5RIBE83qURfykGmn");
    }
}
