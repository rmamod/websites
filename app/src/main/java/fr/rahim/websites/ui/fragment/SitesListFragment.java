package fr.rahim.websites.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.rahim.websites.R;
import fr.rahim.websites.adapter.WebsiteAdapter;
import fr.rahim.websites.entities.Website;
import fr.rahim.websites.listener.SitesListListener;
import fr.rahim.websites.controller.WebsiteController;
import fr.rahim.websites.view.FastScroller;


public class SitesListFragment extends Fragment {


    private SitesListListener mSitesListListener;
    private ArrayList<Website> mWebsites = new ArrayList<>();
    private WebsiteAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FastScroller mFastScroller;
    private WebsiteController mWebsiteController;

    AlertDialog mErrorDialog;

    public static SitesListFragment newInstance(){
        return new SitesListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SitesListListener){
            mSitesListListener = (SitesListListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_sites_list, container, false);
        setHasOptionsMenu(true);
        mFastScroller = (FastScroller) v.findViewById(R.id.fast_scroller);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        buildAlertDialog();
        mFastScroller.setRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.columns)));
        mAdapter = new WebsiteAdapter(mWebsites, getActivity());
        mAdapter.setOnItemClickListener(position -> showDetail(mWebsites.get(position)));
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    public void buildAlertDialog(){
        mErrorDialog =  new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_error_title))
                .setMessage(R.string.dialog_error_message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(android.R.string.ok), (DialogInterface dialog, int which) -> requestClosing())
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        mWebsiteController = new WebsiteController();
        mWebsiteController.getData(mWebsites, mAdapter, mErrorDialog);
    }

    private void requestClosing(){
        if(mSitesListListener != null){
            mSitesListListener.onCloseRequested();
        }
    }

    private void showDetail(Website site){
        if(mSitesListListener != null){
            mSitesListListener.showDetail(site);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mWebsiteController.search(mWebsites, mAdapter, mErrorDialog, newText);
                return false;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mWebsiteController.cancelLatestSubscription();
    }
}
