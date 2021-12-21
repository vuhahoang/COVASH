package com.example.covash_demo;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    String title, desc, content, image, url,time,publishedAt,status;

    private TextView tvTitle, tvContent,time_news,publishedAt_news;
    private ImageView ivImg,favorite;
    private Button btnReadfull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        desc = getIntent().getStringExtra("desc");
        image = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");
        time = getIntent().getStringExtra("time");
        status = getIntent().getStringExtra("status");
        publishedAt=getIntent().getStringExtra("publishedAt");

        tvTitle = findViewById(R.id.TVTitle);
        tvContent = findViewById(R.id.TVContent);
        ivImg = findViewById(R.id.IVNews);
        time_news = findViewById(R.id.time_news);
        publishedAt_news = findViewById(R.id.publishedAt_news);
        btnReadfull = findViewById(R.id.btnReadfull);
        favorite = findViewById(R.id.favorite_icon);

        tvTitle.setText(title);
        time_news.setText(time);
        publishedAt_news.setText(publishedAt);
        tvContent.setText(content);
        Picasso.get().load(image).into(ivImg);
        if(status =="0"){
            favorite.setImageResource(R.drawable.ic_favorite);
        }else if(status =="1"){
            favorite.setImageResource(R.drawable.ic_favorite_red);
        }
        btnReadfull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsDetailActivity.this,NewsDetailsActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("desc",desc);
                intent.putExtra("image",image);
                intent.putExtra("url",url);
                intent.putExtra("time",time);
                intent.putExtra("publishedAt",publishedAt);
                intent.putExtra("status",status);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}