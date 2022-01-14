package com.example.covash_demo.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covash_demo.Modle.Articles;
import com.example.covash_demo.NewsDetailActivity;
import com.example.covash_demo.R;
import com.example.covash_demo.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<Articles> articlesArrayList;
    private Context context;

    public NewsAdapter(ArrayList<Articles> articlesArrayList, Context context) {
        this.articlesArrayList = articlesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_rv_item, parent, false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        Articles articles = articlesArrayList.get(position);
        holder.subtitle.setText(articles.getDesc());
        holder.title.setText(articles.getTitle());
        Picasso.get().load(articles.getUrlToImage()).into(holder.newsIV);
        holder.time.setText(Utils.DateToTimeFormat(articles.getPublishedAt()));
        holder.publishedAt.setText(Utils.DateFormat(articles.getPublishedAt()));
        if (articlesArrayList.get(position).getStatus() == "0"){
            holder.favorite_btn.setImageResource(R.drawable.ic_favorite);
        }else if (articlesArrayList.get(position).getStatus() == "1"){
            holder.favorite_btn.setImageResource(R.drawable.ic_favorite_red);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("title", articles.getTitle());
                intent.putExtra("content", articles.getContent());
                intent.putExtra("desc", articles.getDesc());
                intent.putExtra("url", articles.getUrl());
                intent.putExtra("image", articles.getUrlToImage());
                intent.putExtra("time"," \u2022 "+ Utils.DateToTimeFormat(articles.getPublishedAt()));
                intent.putExtra("publishedAt",Utils.DateFormat(articles.getPublishedAt()));
                intent.putExtra("status",articles.getStatus());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articlesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title, subtitle,time,publishedAt;
        private ImageView newsIV,favorite_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.idTVNewsHeading);
            subtitle = itemView.findViewById(R.id.idTVSubtitle);
            newsIV = itemView.findViewById(R.id.idIVNews);
            time = itemView.findViewById(R.id.news_time);
            publishedAt = itemView.findViewById(R.id.publishedAt);
            favorite_btn = itemView.findViewById(R.id.favorite_btn);
            favorite_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(articlesArrayList.get(position).getStatus() == "0"){
                        articlesArrayList.get(position).setStatus("1");
                        favorite_btn.setImageResource(R.drawable.ic_favorite_red);
                    }else {
                        articlesArrayList.get(position).setStatus("0");
                        favorite_btn.setImageResource(R.drawable.ic_favorite);
                    }
                }
            });
        }
    }
}

