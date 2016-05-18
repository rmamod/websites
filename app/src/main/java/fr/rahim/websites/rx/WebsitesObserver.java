package fr.rahim.websites.rx;


import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.rahim.websites.adapter.WebsiteAdapter;
import fr.rahim.websites.entities.Website;
import io.realm.Realm;
import rx.Observer;


public class WebsitesObserver implements Observer<List<Website>> {

    private static final String TAG = WebsitesObserver.class.getSimpleName();

    private ArrayList<Website> mWebsites;
    private WebsiteAdapter mAdapter;
    private Realm mRealmDatabase;
    private AlertDialog mErrorDialog;

    public WebsitesObserver(ArrayList<Website> websites, WebsiteAdapter adapter, AlertDialog errorDialog){
        mWebsites = websites;
        mAdapter = adapter;
        mErrorDialog = errorDialog;
        mRealmDatabase = Realm.getDefaultInstance();
    }

    @Override
    public void onCompleted() {
        if(mRealmDatabase.isEmpty()) {
            save();
        }
        mAdapter.notifyDataSetChanged();
    }

    public void save(){
        mRealmDatabase.beginTransaction();
        mRealmDatabase.copyToRealm(mWebsites);
        mRealmDatabase.commitTransaction();
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, e.getMessage());
        mErrorDialog.show();
    }

    @Override
    public void onNext(List<Website> sites) {
        mWebsites.clear();
        mWebsites.addAll(sites);
    }
}
