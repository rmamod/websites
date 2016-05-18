package fr.rahim.websites.entities;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Website extends RealmObject implements Parcelable {

    @PrimaryKey
    private String mId;
    private String mName;
    private String mThumbUrl;

    public Website(){
        // No-args constructor.
    }

    public Website(Parcel source){
        mId = source.readString();
        mName = source.readString();
        mThumbUrl = source.readString();
    }

    public Website(String name, String thumbUrl){
        mName = name;
        mThumbUrl = thumbUrl;
        mId = UUID.randomUUID().toString();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getThumbUrl() {
        return mThumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        mThumbUrl = thumbUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mThumbUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Website createFromParcel(Parcel in) {
            return new Website(in);
        }

        public Website[] newArray(int size) {
            return new Website[size];
        }
    };
}
