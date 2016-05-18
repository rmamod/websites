package fr.rahim.websites.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.rahim.websites.R;
import fr.rahim.websites.entities.Website;
import fr.rahim.websites.listener.OnItemClickListener;


public class WebsiteAdapter extends RecyclerView.Adapter<WebsiteAdapter.ViewHolder> {


    private ArrayList<Website> mWebsites;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public WebsiteAdapter(ArrayList<Website> sites, Context context){
        mWebsites = sites;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mWebsites.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_website, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.container.setOnClickListener((View v) -> mOnItemClickListener.onClick((int) v.getTag()));
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Website site = mWebsites.get(position);
        holder.nameTextView.setText(site.getName());
        holder.container.setTag(position);
        Picasso.with(mContext).load(site.getThumbUrl()).into(holder.thumbImageView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView thumbImageView;
        ViewGroup container;

        public ViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.text_view_name);
            thumbImageView = (ImageView) view.findViewById(R.id.image_view_thumb);
            container = (ViewGroup) view.findViewById(R.id.container);
        }
    }
}
