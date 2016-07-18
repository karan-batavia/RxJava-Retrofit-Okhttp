package com.news.home.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infra.managers.home.models.AvatarImage;
import com.infra.managers.home.models.NewsItem;
import com.infra.managers.home.models.User;
import com.news.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hetashah on 7/17/16.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ItemViewHolder> {
    List<NewsItem> newsFeed;

    public NewsListAdapter(List<NewsItem> newsFeed) {
        this.newsFeed = newsFeed;
    }

    @Override
    public NewsListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.news_list_item, parent, false);

        // Return a new holder instance
        ItemViewHolder viewHolder = new ItemViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsListAdapter.ItemViewHolder holder, int position) {
        NewsItem newsItem = newsFeed.get(position);
        String post = newsItem.getText();
        if(!StringUtils.isEmpty(post)) {
            holder.tvNewsPost.setText(Html.fromHtml(post));
        }

        User user = newsItem.getUser();
        if(user != null) {
            AvatarImage avatarImage = user.getAvatarImage();
            if(avatarImage != null) {
                Glide.with(holder.ivUserAvatar.getContext())
                        .load(avatarImage.getUrl())
                        .fitCenter()
                        .into(holder.ivUserAvatar);
            }

            String posterName = user.getName();
            if(!StringUtils.isEmpty(posterName)) {
                holder.tvPosterName.setText(posterName);
            }
        }

        if(newsItem.isAnimate()) {
            // Set the view to fade in
            setupObjectAnimator(holder.itemView).start();
            newsItem.setAnimate(false);
        }
    }

    private ObjectAnimator setupObjectAnimator(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -500, 0);
        animator.setDuration(300);
        return animator;
    }

    @Override
    public int getItemCount() {
        if(newsFeed != null) {
            return newsFeed.size();
        }

        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivUserAvatar)
        ImageView ivUserAvatar;
        @Bind(R.id.tvNewsPost)
        TextView tvNewsPost;
        @Bind(R.id.tvPosterName)
        TextView tvPosterName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
