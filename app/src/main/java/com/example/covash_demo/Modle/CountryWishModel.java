package com.example.covash_demo.Modle;

public class CountryWishModel {
    private String Country;
    private String Flag;
    private String Cases;
    private String Recovored;
    private String Critical;
    private String Active;
    private String TodayCases;
    private String TotalDeaths;
    private String TodayDeaths;
    private String AffectedCountries;



    public CountryWishModel(String country, String flag, String cases, String recovored, String critical, String active, String todayCases, String totalDeaths, String todayDeaths, String affectedCountries) {
        Country = country;
        Flag = flag;
        Cases = cases;
        Recovored = recovored;
        Critical = critical;
        Active = active;
        TodayCases = todayCases;
        TotalDeaths = totalDeaths;
        TodayDeaths = todayDeaths;
        AffectedCountries = affectedCountries;
    }



    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCases() {
        return Cases;
    }

    public void setCases(String cases) {
        Cases = cases;
    }

    public String getRecovored() {
        return Recovored;
    }

    public void setRecovored(String recovored) {
        Recovored = recovored;
    }

    public String getCritical() {
        return Critical;
    }

    public void setCritical(String critical) {
        Critical = critical;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getTodayCases() {
        return TodayCases;
    }

    public void setTodayCases(String todayCases) {
        TodayCases = todayCases;
    }

    public String getTotalDeaths() {
        return TotalDeaths;
    }

    public void setTotalDeaths(String totalDeaths) {
        TotalDeaths = totalDeaths;
    }

    public String getTodayDeaths() {
        return TodayDeaths;
    }

    public void setTodayDeaths(String todayDeaths) {
        TodayDeaths = todayDeaths;
    }

    public String getAffectedCountries() {
        return AffectedCountries;
    }

    public void setAffectedCountries(String affectedCountries) {
        AffectedCountries = affectedCountries;
    }
}
