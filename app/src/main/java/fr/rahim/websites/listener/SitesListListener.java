package fr.rahim.websites.listener;

import fr.rahim.websites.entities.Website;

public interface SitesListListener {

    void showDetail(Website site);
    void onCloseRequested();
}
