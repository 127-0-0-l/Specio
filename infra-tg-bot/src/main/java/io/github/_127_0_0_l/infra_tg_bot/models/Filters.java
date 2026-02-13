package io.github._127_0_0_l.infra_tg_bot.models;

import java.util.HashSet;
import java.util.Set;

public class Filters {
    private Set<String> regions = new HashSet<>();
    private Set<String> cities = new HashSet<>();
    private Integer priceFrom;
    private Integer priceTo;
    private Integer yearFrom;
    private Integer yearTo;

    public Set<String> getRegions (){
        return regions;
    }

    public Set<String> getCities (){
        return cities;
    }

    public Integer getPriceFrom (){
        return priceFrom;
    }

    public Integer getPriceTo (){
        return priceTo;
    }

    public Integer getYearFrom (){
        return yearFrom;
    }

    public Integer getYearTo (){
        return yearTo;
    }

    public void setRegions (Set<String> regions){
        this.regions = regions;
    }

    public void addRegion (String region){
        regions.add(region);
    }

    public void removeRegion (String region) {
        if (regions.contains(region)){
            regions.remove(region);
        }
    }

    public void toggleRegion (String region) {
        if (regions.contains(region)) {
            regions.remove(region);
        } else {
            regions.add(region);
        }
    }

    public void setCities (Set<String> cities){
        this.cities = cities;
    }

    public void addCity (String city){
        cities.add(city);
    }

    public void removeCity (String city) {
        if (cities.contains(city)){
            cities.remove(city);
        }
    }

    public void toggleCity (String city) {
        if (cities.contains(city)) {
            cities.remove(city);
        } else {
            cities.add(city);
        }
    }

    public void setPriceFrom (int price){
        priceFrom = price;
    }

    public void setPriceTo (int price){
        priceTo = price;
    }

    public void setYearFrom (int year){
        yearFrom = year;
    }

    public void setYearTo (int year){
        yearTo = year;
    }
}
