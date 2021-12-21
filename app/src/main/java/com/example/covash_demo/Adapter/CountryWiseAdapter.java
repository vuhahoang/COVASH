package com.example.covash_demo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.covash_demo.CountryDashboard;
import com.example.covash_demo.CountrySwitchData;
import com.example.covash_demo.Modle.CountryWishModel;
import com.example.covash_demo.R;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CountryWiseAdapter extends RecyclerView.Adapter<CountryWiseAdapter.ViewHolder> {
    private Context mContext;
        private ArrayList<CountryWishModel> countryWishModels;
    private String searchText="";
    private SpannableStringBuilder sb;

    public CountryWiseAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setdata(ArrayList<CountryWishModel> list){
        this.countryWishModels = list;
        notifyDataSetChanged();
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_country_wise,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  CountryWiseAdapter.ViewHolder holder, int position) {
        CountryWishModel conCountryWishModel = countryWishModels.get(position);
        String countryName = conCountryWishModel.getCountry();
        String countryTotal = conCountryWishModel.getCases();
        String countryFlag = conCountryWishModel.getFlag();
        String countryRank = String.valueOf(position+1);
        int countryTotalInt = Integer.parseInt(countryTotal);
        holder.tv_rankTextView.setText(countryRank+".");
        holder.tv_countryTotalCases.setText(NumberFormat.getInstance().format(countryTotalInt));
        holder.tv_countryName.setText(countryName);
        Glide.with(mContext).load(countryFlag).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.iv_flagImage);

        holder.lin_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    CountryWishModel clickitem = countryWishModels.get(position);
                Intent i = new Intent(mContext, CountryDashboard.class);
                i.putExtra("country_name",clickitem.getCountry());
                i.putExtra("Cases",clickitem.getCases());
                i.putExtra("Recovored",clickitem.getRecovored());
                i.putExtra("Critical",clickitem.getCritical());
                i.putExtra("Active",clickitem.getActive());
                i.putExtra("Today_Cases",clickitem.getTodayDeaths());
                i.putExtra("Total_Deaths",clickitem.getTotalDeaths());
                i.putExtra("Today_Deaths",clickitem.getTodayDeaths());
                i.putExtra("Test",clickitem.getAffectedCountries());
                mContext.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return countryWishModels.size();
    }

    public void filteredList(ArrayList<CountryWishModel> filteredList, String text) {
        countryWishModels = filteredList;
        this.searchText = text;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_countryName, tv_countryTotalCases, tv_rankTextView;
        ImageView iv_flagImage;
        LinearLayout lin_country;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            tv_countryName = itemView.findViewById(R.id.layout_country_wise_country_name_textview);
            tv_countryTotalCases = itemView.findViewById(R.id.layout_country_wise_confirmed_textview);
            iv_flagImage = itemView.findViewById(R.id.layout_country_wise_flag_imageview);
            tv_rankTextView = itemView.findViewById(R.id.layout_country_wise_country_rank);
            lin_country = itemView.findViewById(R.id.layout_country_wise_lin);
        }
    }
}
