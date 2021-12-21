package com.example.covash_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.covash_demo.Adapter.CountryWiseAdapter;
import com.example.covash_demo.Modle.CountryWishModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class CountrySwitchData extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CountryWiseAdapter countryWiseAdapter;
    private ArrayList<CountryWishModel> countryWishModels;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText etSearch;

    private String Country;
    private String Flag;
    private String Cases;
    private String Recovored;
    private String Critical;
    private String Active;
    private String TodayCases;
    private String TotalDeaths;
    private String TodayDeaths;
    private String Test;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_switch_data);

        Init();
        FetchCountryWiseData();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Filter(s.toString());
            }
        });


    }

    private void Filter(String text) {
        ArrayList<CountryWishModel> filteredList = new ArrayList<>();
        for (CountryWishModel item : countryWishModels) {
            if (item.getCountry().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
            countryWiseAdapter.filteredList(filteredList,text);
        }

    }

    private void FetchCountryWiseData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiURL = "https://disease.sh/v2/countries";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            countryWishModels.clear();
                            for (int i=0;i<response.length(); i++){
                                JSONObject countryJSONObject = response.getJSONObject(i);

                                Country = countryJSONObject.getString("country");
                                Cases = countryJSONObject.getString("cases");
                                TodayCases = countryJSONObject.getString("todayCases");
                                Active = countryJSONObject.getString("active");
                                Recovored = countryJSONObject.getString("recovered");
                                TotalDeaths = countryJSONObject.getString("deaths");
                                TodayDeaths = countryJSONObject.getString("todayDeaths");
                                Critical = countryJSONObject.getString("critical");
                                Test = countryJSONObject.getString("tests");
                                JSONObject flagObject = countryJSONObject.getJSONObject("countryInfo");
                                String flagUrl = flagObject.getString("flag");

                                //Creating an object of our country model class and passing the values in the constructor
                                CountryWishModel country  = new CountryWishModel(Country,flagUrl,Cases,Recovored,Critical,Active,TodayCases,TotalDeaths,TodayDeaths,Test);
                                //adding data to our arraylist
                                countryWishModels.add(country);
                            }

                            Handler makeDelay = new Handler();
                            makeDelay.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    countryWiseAdapter.notifyDataSetChanged();

                                }
                            }, 1000);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void Init() {
        swipeRefreshLayout = findViewById(R.id.activity_state_wise_swipe_refresh_layout);
        etSearch = findViewById(R.id.activity_state_wise_search_editText);

        recyclerView = findViewById(R.id.activity_state_wise_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        countryWishModels = new ArrayList<>();
        countryWiseAdapter = new CountryWiseAdapter(CountrySwitchData.this);
        countryWiseAdapter.setdata(countryWishModels);
        recyclerView.setAdapter(countryWiseAdapter);
    }
}