package fr.rahim.websites;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class WebsiteApp extends Application {

    private static final String DATABASE_NAME = "websites.realm";
    @Override
    public void onCreate() {
        super.onCreate();
        setUpRealmConf();
    }

    public void setUpRealmConf(){
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
