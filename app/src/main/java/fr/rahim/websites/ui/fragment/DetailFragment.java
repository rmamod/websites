package fr.rahim.websites.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.rahim.websites.R;
import fr.rahim.websites.entities.Website;


public class DetailFragment extends DialogFragment {

    private static final String BUNDLE_WEBSITE = "fr.rahim.websites.website";
    private ImageView mThumbImageView;
    private TextView mNameTextView;
    private Website mSite;

    public static DetailFragment newInstance(Website site) {
        DetailFragment fragment = new DetailFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_WEBSITE, site);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = savedInstanceState != null ? savedInstanceState : getArguments();
        mSite = bundle.getParcelable(BUNDLE_WEBSITE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        mThumbImageView = (ImageView) v.findViewById(R.id.image_view_full);
        mNameTextView = (TextView) v.findViewById(R.id.text_view_full);

        mNameTextView.setText(mSite.getName());
        Picasso.with(getActivity()).load(mSite.getThumbUrl()).into(mThumbImageView);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_WEBSITE, mSite);
    }
}
