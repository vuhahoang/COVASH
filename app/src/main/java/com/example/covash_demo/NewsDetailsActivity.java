package com.example.covash_demo;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

public class NewsDetailsActivity extends AppCompatActivity {
    ImageView imageView,share,favorite;
    TextView date,time,title;
    boolean isHideTolbarView = false;
    FrameLayout date_behavior;
    LinearLayout titleAppbar;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    String mUrl,mImg,mTitle,mDesc,mTime,mDate,mStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        appBarLayout = findViewById(R.id.appbar);
        date_behavior = findViewById(R.id.date_behavior);
        titleAppbar = findViewById(R.id.title_appbar);
        imageView = findViewById(R.id.backdrop);
        date = findViewById(R.id.date);
        time = findViewById(R.id.timeApi);
        title = findViewById(R.id.title_detail);
        share = findViewById(R.id.share_news);
        favorite = findViewById(R.id.favorite_news);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "Click to read more :" + mUrl;
                String shareSub = "Hot News";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(shareIntent,"Share with"));
            }
        });

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mImg = intent.getStringExtra("image");
        mDesc = intent.getStringExtra("desc");
        mTitle = intent.getStringExtra("title");
        mTime = intent.getStringExtra("time");
        mDate = intent.getStringExtra("publishedAt");
        mStatus = intent.getStringExtra("status");


        title.setText(mTitle);
        Picasso.get().load(mImg).into(imageView);
        time.setText(mTime);
        date.setText(mDate);
        if(mStatus=="0"){
            favorite.setImageResource(R.drawable.ic_favorite);
        }else if(mStatus=="1"){
            favorite.setImageResource(R.drawable.ic_favorite_red);
        }
        initWebView(mUrl);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset)/(float) maxScroll;
                if(percentage == 1f && isHideTolbarView){
                    date_behavior.setVisibility(View.GONE);
                    titleAppbar.setVisibility(View.VISIBLE);
                    isHideTolbarView = !isHideTolbarView;
                }else if(percentage < 1f && isHideTolbarView){
                    date_behavior.setVisibility(View.VISIBLE);
                    titleAppbar.setVisibility(View.GONE);
                    isHideTolbarView = !isHideTolbarView;
                }
            }
        });
    }
    private void initWebView(String url){
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
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