package com.example.covash_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

public class CountryDashboard extends AppCompatActivity {

    BarChart barChart;
    TextView tvCases,tvRecovered,tvCritical,tvActive,tvTodayCases,tvTotalDeaths,tvTodayDeaths,tvTest,tvCountries;
    private Context context;
    Button btnGlobaldata,btnCountryData;
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
        setContentView(R.layout.activity_country_dashboard);
        GetIntent();


        tvCountries = (TextView) findViewById(R.id.tvCountries);
        tvCases = (TextView) findViewById(R.id.tvCasesc);
        tvRecovered = (TextView) findViewById(R.id.tvRecoveredc);
        tvCritical = (TextView) findViewById(R.id.tvCriticalc);
        tvActive = (TextView) findViewById(R.id.tvActivec);
        tvTodayCases = (TextView) findViewById(R.id.tvTodayCasesc);
        tvTotalDeaths = (TextView) findViewById(R.id.tvTotalDeathsc);
        tvTodayDeaths = (TextView) findViewById(R.id.tvTodayDeathsc);
        tvTest = (TextView) findViewById(R.id.tvAffectedCountriesc);
        barChart = (BarChart) findViewById(R.id.barchartc);
        btnGlobaldata = (Button) findViewById(R.id.btnGlobalData);
        btnCountryData = (Button) findViewById(R.id.btnCountri);

        btnGlobaldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(CountryDashboard.this,homepage.class);
                startActivity(main);
            }
        });

        btnCountryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent coun = new Intent(CountryDashboard.this,CountrySwitchData.class);
                startActivity(coun);
            }
        });
        loadCountryData();
    }

    private void loadCountryData() {
        tvCountries.setText("Static of "+ Country);
        tvCases.setText(Cases);
        tvRecovered.setText(Recovored);
        tvCritical.setText(Critical);
        tvActive.setText(Active);
        tvTodayCases.setText(TodayCases);
        tvTotalDeaths.setText(TotalDeaths);
        tvTodayDeaths.setText(TodayDeaths);
        tvTest.setText(Test);

        barChart.addBar(new BarModel("Cases",Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#FFA726")));
        barChart.addBar(new BarModel("Recoverd",Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
        barChart.addBar(new BarModel("Deaths",Integer.parseInt(tvTotalDeaths.getText().toString()), Color.parseColor("#EF5350")));
        barChart.addBar(new BarModel("Active",Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
        barChart.startAnimation();
    }

    private void GetIntent() {
        Intent intent = getIntent();
        Country = intent.getStringExtra("country_name");
        Cases = intent.getStringExtra("Cases");
        Recovored = intent.getStringExtra("Recovored");
        Critical = intent.getStringExtra("Critical");
        Active = intent.getStringExtra("Active");
        TodayCases = intent.getStringExtra("Today_Cases");
        TotalDeaths = intent.getStringExtra("Total_Deaths");
        TodayDeaths = intent.getStringExtra("Today_Deaths");
        Test = intent.getStringExtra("Test");

    }


}