package fr.rahim.websites.ui.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import fr.rahim.websites.R;
import fr.rahim.websites.entities.Website;
import fr.rahim.websites.listener.SitesListListener;
import fr.rahim.websites.ui.fragment.DetailFragment;
import fr.rahim.websites.ui.fragment.SitesListFragment;


public class MainActivity extends AppCompatActivity implements SitesListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show(SitesListFragment.newInstance(), false);
    }


    @Override
    public void showDetail(Website site){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DetailFragment f = DetailFragment.newInstance(site);
        if(getResources().getBoolean(R.bool.isTablet)){
            f.show(ft, "detail");
        } else {
            show(f, true);
        }
    }

    @Override
    public void onCloseRequested() {
        onBackPressed();
    }

    public void show(Fragment fragment, boolean addToBackStack){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag_container, fragment);
        if(addToBackStack){
            ft.addToBackStack(null);
        }
        ft.commit();

    }
}