package com.example.covash_demo;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.covash_demo.Adapter.CategoryAdapter;
import com.example.covash_demo.Adapter.NewsAdapter;
import com.example.covash_demo.Modle.Articles;
import com.example.covash_demo.Modle.CategoryRVModel;
import com.example.covash_demo.Modle.NewsModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity implements CategoryAdapter.CategoryClick{

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private androidx.appcompat.widget.SearchView searchView;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private NewsAdapter newsAdapter;
    private CategoryAdapter categoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    String API_KEY = "46067344d8c64b0191b72ecd204594bd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategory);
        loadingPB = findViewById(R.id.idPBLoading);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNews("All","");
            }
        });
        articlesArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();

        newsAdapter = new NewsAdapter(articlesArrayList, this);
        categoryAdapter = new CategoryAdapter(categoryRVModelArrayList, this, this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsAdapter);
        categoryRV.setAdapter(categoryAdapter);
        getCategory();
        onLoadingRefresh("");
        newsAdapter.notifyDataSetChanged();

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onLoadingRefresh(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getCategory(){
        categoryRVModelArrayList.add(new CategoryRVModel("All", "https://images.unsplash.com/photo-1588681664899-f142ff2dc9b1?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1074&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports", "https://images.unsplash.com/photo-1471295253337-3ceaaedca402?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1168&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology", "https://images.unsplash.com/photo-1518770660439-4636190af475?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Business", "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health", "https://images.unsplash.com/photo-1498837167922-ddd27525d352?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("entertainment", "https://images.unsplash.com/photo-1532094349884-543bc11b234d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"));
        categoryAdapter.notifyDataSetChanged();
    }


    private void getNews(String category, String keyword){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        swipeRefreshLayout.setRefreshing(true);
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=us&category="+category+"&apiKey=46067344d8c64b0191b72ecd204594bd";
        String url = "https://newsapi.org/v2/top-headlines?country=us&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=46067344d8c64b0191b72ecd204594bd";
        String BASE_URL = "https://newsapi.org/";
        String searchUrl = "https://newsapi.org/v2/everything?q=" +keyword + "&sortBy=publishedAt&apiKey=46067344d8c64b0191b72ecd204594bd" ;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<NewsModel> call;
        if(keyword.length()>0){
            call = retrofitAPI.getNewsSearch(searchUrl);
        }
        else if(category.equals("All")){
            call = retrofitAPI.getAllNews(url);
        }else {
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if(response.isSuccessful() && response.body().getArticles() !=null){
                    loadingPB.setVisibility(View.GONE);
                    ArrayList<Articles> articles = response.body().getArticles();
                    for(int i=0; i<articles.size(); i++){
                        articlesArrayList.add(new Articles(articles.get(i).getTitle(), articles.get(i).getDesc(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent(),articles.get(i).getPublishedAt(),articles.get(i).getStatus()));
                    }
                    newsAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }else {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(NewsActivity.this, "No Result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(NewsActivity.this, "Loi load", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModelArrayList.get(position).getCategory();
        onLoadingRefreshCategory(category,"");
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
    private void onLoadingRefresh(final String keyword){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getNews("All",keyword);
                    }
                }
        );
    }
    private void onLoadingRefreshCategory(String category, String keyword){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getNews(category,keyword);
                    }
                }
        );
    }
}