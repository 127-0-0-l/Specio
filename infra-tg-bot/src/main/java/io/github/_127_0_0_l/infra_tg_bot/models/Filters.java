package io.github._127_0_0_l.infra_tg_bot.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Filters {
    private final Integer MIN_YEAR_VALUE = 1900;
    private Set<String> regions = new HashSet<>();
    private Set<String> cities = new HashSet<>();
    private Integer priceFrom = 0;
    private Integer priceTo = Integer.MAX_VALUE;
    private Integer yearFrom = MIN_YEAR_VALUE;
    private Integer yearTo = LocalDate.now().getYear();

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
        if (price <= priceTo && price >= 0){
            priceFrom = price;
        }
    }

    public void setPriceTo (int price){
        if (price >= priceFrom){
            priceTo = price;
        }
    }

    public void setYearFrom (int year){
        if (year <= yearTo && yearFrom >= MIN_YEAR_VALUE){
            yearFrom = year;   
        }
    }

    public void setYearTo (int year){
        if (year >= yearFrom && year <= LocalDate.now().getYear()){
            yearTo = year;
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        if (regions.size() > 0){
            sb.append("regions: " + String.join(", ", regions));
        } else {
            sb.append("regions: all");
        }
        sb.append("\n");

        if (cities.size() > 0){
            sb.append("cities: " + String.join(", ", cities));
        } else {
            sb.append("cities: all");
        }
        sb.append("\n");

        sb.append(String.format("price: from %d to %d%n", priceFrom, priceTo));
        sb.append(String.format("year: from %d to %d%n", yearFrom, yearTo));

        return sb.toString();
    }
}
