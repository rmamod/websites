package fr.rahim.websites.controller;


import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.rahim.websites.adapter.WebsiteAdapter;
import fr.rahim.websites.entities.Website;
import fr.rahim.websites.rx.IOOperations;
import fr.rahim.websites.rx.WebsitesObserver;
import io.realm.Realm;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WebsiteController {

    private static final String THUMB_BASE_URL  = "https://s3-eu-west-1.amazonaws.com/static-icons/_android48/";
    private static final int DEBOUNCE_TIME = 200;

    private Realm mRealmDatabase;
    private List<Subscription> mSubscriptions = new ArrayList<>();

    public WebsiteController(){
        mRealmDatabase = Realm.getDefaultInstance();
    }

    /**
     * Get the data from the database. If the database is empty, get the
     * data from the external link.
     * @param list
     * @param adapter
     */
    public void getData(ArrayList<Website> list, WebsiteAdapter adapter, AlertDialog errorDialog) {
        WebsitesObserver observer = new WebsitesObserver(list, adapter, errorDialog);
        if(mRealmDatabase.isEmpty()) {
            Subscription subscription =  IOOperations.read()
                    .flatMap(sites -> Observable.from(sites))
                    .filter(s -> !s.contains("@2x"))
                    .map(s -> new Website(getName(s), getThumbUrlFromName(s)))
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
            mSubscriptions.add(subscription);

        } else {
            Subscription subscription =  mRealmDatabase.where(Website.class).findAllAsync().asObservable()
                    .filter(results -> results.isLoaded())
                    .first()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new WebsitesObserver(list, adapter, errorDialog));
            mSubscriptions.add(subscription);

        }
    }

    /**
     * Search for websites that begins with the supplied query
     * @param list
     * @param adapter
     * @param query
     */
    public void search(ArrayList<Website> list, WebsiteAdapter adapter, AlertDialog errorDialog, String query){
        Subscription subscription = mRealmDatabase.where(Website.class).beginsWith("mName", query).findAllAsync().asObservable()
                .filter(results -> results.isLoaded())
                .first()
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(new WebsitesObserver(list, adapter, errorDialog));
        mSubscriptions.add(subscription);
    }

    private String getThumbUrlFromName(String s){
        return  new StringBuilder(THUMB_BASE_URL).append(s).toString();
    }

    private String getName(String s){
        String firstPart = s.split("\\.")[0];
        return firstPart.replace("_", ".");
    }

    /**
     * Cancel latest subscription
     */
    public void cancelLatestSubscription() {
        for (Subscription subscription : mSubscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
        mSubscriptions.clear();
    }

}
